package online.meinkraft.customvillagertrades.listener;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.task.RemoveMoneyFromHandTask;
import online.meinkraft.customvillagertrades.task.RemoveMoneyFromInventoryTask;

public class InventoryClickListener implements Listener {

    private final CustomVillagerTrades plugin;

    public InventoryClickListener(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        
        // clicked outside inventory
        if(event.getClickedInventory() == null) return;

        // economy (money) related logic for handling money
        if(plugin.isEconomyEnabled()) {

            InventoryType inventoryType = event.getClickedInventory().getType();

            // check for clicking money ingredients in a merchant inventory
            if(inventoryType == InventoryType.MERCHANT) {

                MerchantInventory inventory = (MerchantInventory) event.getClickedInventory();
                ItemStack item = inventory.getItem(event.getRawSlot());
    
                // stop player from clicking money in merchant inventories
                if(isItemMoney(item) && !plugin.isCurrencyPhysical()) {
                                
                    // add result amount to player balance
                    if(event.getSlotType() == InventoryType.SlotType.RESULT) { 
    
                        Player player = (Player) event.getWhoClicked();

                        plugin.getServer().getScheduler().runTask(
                            plugin, 
                            new RemoveMoneyFromInventoryTask(
                                plugin, 
                                player.getInventory(),
                                player
                            )
                        );

                        plugin.getServer().getScheduler().runTask(
                            plugin, 
                            new RemoveMoneyFromHandTask(
                                plugin, 
                                player
                            )
                        );   

                    }
                    else {
                        event.setCancelled(true);
                    }
    
                }
    
            }
    
            // if money somehow gets into a players inventory, then we need to
            // remove it when they click on it if its not physical
            else if(inventoryType == InventoryType.PLAYER) {
    
                Player player = (Player) event.getWhoClicked();
                ItemStack item = event.getCurrentItem();

                Double money = getMoney(item);
    
                if(money != null) {
    
                    if(!plugin.isCurrencyPhysical()) {
                        event.setCancelled(true);
    
                        plugin.getServer().getScheduler().runTask(
                            plugin, 
                            new RemoveMoneyFromInventoryTask(
                                plugin, 
                                player.getInventory(),
                                player
                            )
                        );
                    }
                    else if(event.getClick() == ClickType.DROP) {

                        event.setCancelled(true);
                        event.getView().setItem(event.getRawSlot(), null);
                        
                        double totalAmount = money * item.getAmount();
                        plugin.getEconomy().depositPlayer(player, totalAmount);
                        player.sendMessage(
                            ChatColor.GREEN +
                            "Deposited " +
                            plugin.getEconomy().format(totalAmount) +
                            " into your account"
                        );

                    }
    
                }
                
            }
        }
             
    } 

    private boolean isItemMoney(ItemStack item) {

        if(item == null) return false;
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return false;
                        
        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
        Double money = itemData.get(
            NamespacedKey.fromString("money", plugin),
            PersistentDataType.DOUBLE
        );

        return money != null;

    }

    private Double getMoney(ItemStack item) {

        if(item == null) return null;
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return null;
                        
        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
        Double money = itemData.get(
            NamespacedKey.fromString("money", plugin),
            PersistentDataType.DOUBLE
        );

        return money;

    }

}