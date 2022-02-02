package online.meinkraft.customvillagertrades.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;

import online.meinkraft.customvillagertrades.trade.CustomTradeManager;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.exception.VillagerNotMerchantException;

public class PlayerInteractEntityListener implements Listener {

    private final CustomVillagerTrades plugin;

    public PlayerInteractEntityListener(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        CustomTradeManager tradeManager = plugin.getCustomTradeManager();
        Entity entity = event.getRightClicked();

        // check interacted entity is a merchant villager
        if(
            entity.getType() != EntityType.VILLAGER ||
            !(entity instanceof Merchant)
        ) return;

        Villager villager = (Villager) entity;

        // refresh the trades based on their keys
        tradeManager.refreshTrades((Merchant) entity);

        ItemStack toolUsed = event.getPlayer().getInventory().getItemInMainHand();
        if(toolUsed.getType() == plugin.getToolMaterial()) {
            try {
                usePluginTool(event, villager);
            } catch (VillagerNotMerchantException e) {
                return;
            }
        }
   
    }

    private void usePluginTool(PlayerInteractEntityEvent event, Villager villager) throws VillagerNotMerchantException {
        
        CustomTradeManager tradeManager = plugin.getCustomTradeManager();
        
        if(event.getPlayer().hasPermission("customvillagertrades.item.restore") && 
            event.getPlayer().isSneaking()
        ) {
            tradeManager.restoreVanillaTrades((Villager) villager);
            event.getPlayer().sendMessage(
                ChatColor.GREEN + "Restored vanilla trades for this villager"
            );
        }
        else if(
            event.getPlayer().hasPermission("customvillagertrades.item.reroll") && 
            !event.getPlayer().isSneaking()
        ) {
            if(tradeManager.rerollCustomTrades(villager)) {
                if(!plugin.isVanillaTradesAllowed()) {
                    event.getPlayer().sendMessage(
                        ChatColor.GREEN + "Rerolled all trades for this villager"
                    );
                }
                else {
                    event.getPlayer().sendMessage(
                        ChatColor.GREEN + "Rerolled custom trades for this villager"
                    );
                }
            }
        }
    }

}