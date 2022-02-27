package online.meinkraft.customvillagertrades.listener;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.TradeSelectEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.task.RemoveMoneyFromInventoryTask;
import online.meinkraft.customvillagertrades.villager.VillagerData;

public class TradeSelectListener implements Listener {

    private final CustomVillagerTrades plugin;

    public TradeSelectListener(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTradeSelectEvent(TradeSelectEvent event) {

        Economy economy = plugin.getEconomy();
        Merchant merchant = event.getMerchant();

        // represents the three slots of the trade
        MerchantInventory inventory = event.getInventory();

        // Check player 
        HumanEntity eventSource = event.getWhoClicked();
        if(!(eventSource instanceof Player)) return;
        Player player = (Player) eventSource;

        //TODO handle wandering taders
        Villager villager = (Villager) inventory.getHolder();
        VillagerData data = plugin.getVillagerManager().loadVillagerData(villager);

        // remove money from inventory before trying to add money to the
        // ingredients since this could cause issues if the denominations
        // are the same
        if(plugin.isEconomyEnabled()) {

            new RemoveMoneyFromInventoryTask(
                plugin, 
                event.getInventory(),
                player,
                0
            ).run();

            new RemoveMoneyFromInventoryTask(
                plugin, 
                event.getInventory(),
                player,
                1
            ).run();

            new RemoveMoneyFromInventoryTask(
                plugin, 
                event.getView().getBottomInventory(),
                player
            ).run();

        }

        // add money ingredients to the trade inventory
        if(data.isCustomTrade(event.getIndex())) {

            MerchantRecipe recipe = merchant.getRecipe(event.getIndex());
            List<ItemStack> ingredients = recipe.getIngredients();

            for(int index = 0; index < ingredients.size(); index++) {

                ItemStack ingredient = ingredients.get(index);

                ItemMeta itemMeta = ingredient.getItemMeta();
                if(itemMeta == null) continue;

                PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
                Double money = itemData.get(
                    NamespacedKey.fromString("money", plugin),
                    PersistentDataType.DOUBLE
                );

                if(money != null) {
                    if(plugin.isEconomyEnabled()) {
                        double balance = economy.getBalance(player);
                        int maxStackSize = (int) Math.floor(Math.min(64, balance / money));
                        if(maxStackSize > 0) {
                            EconomyResponse response = plugin.getEconomy().withdrawPlayer(
                                player, 
                                maxStackSize * money
                            );
                            if(response.transactionSuccess()) {
                                inventory.setItem(index, ingredient);
                                inventory.getItem(index).setAmount(maxStackSize);
                                player.sendMessage(String.format(
                                    ChatColor.YELLOW + plugin.getMessage("withdrewMoney"),
                                    plugin.getEconomy().format(maxStackSize * money)
                                ));
                            }
                            else {
                                inventory.setItem(index, null);
                                event.setCancelled(true);
                                player.sendMessage(
                                    ChatColor.RED +
                                    plugin.getMessage("InsufficientFunds")
                                );
                            }
                            
                        }
                    }
                    else { // economy is not enabled
                        inventory.setItem(index, null);
                        event.setCancelled(true);
                        player.sendMessage(
                            ChatColor.RED +
                            plugin.getMessage("economyDisabled")
                        );
                    }  
                }

            }
        }

        // prevent money getting into players inventories
        if(plugin.isEconomyEnabled() && !plugin.isCurrencyPhysical()) {

            plugin.getServer().getScheduler().runTaskLater(
                plugin, 
                new RemoveMoneyFromInventoryTask(
                    plugin, 
                    event.getView().getBottomInventory(),
                    player
                ),
                0
            );
            
        }
        
    }
    
}