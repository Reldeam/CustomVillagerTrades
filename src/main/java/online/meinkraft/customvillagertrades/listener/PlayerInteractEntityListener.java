package online.meinkraft.customvillagertrades.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Merchant;

import online.meinkraft.customvillagertrades.trade.CustomTradeManager;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class PlayerInteractEntityListener implements Listener {

    private final CustomVillagerTrades plugin;

    public PlayerInteractEntityListener(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        CustomTradeManager tradeManager = plugin.getCustomTradeManager();
        Entity entity = event.getRightClicked();
        if(entity.getType() != EntityType.VILLAGER) return;
        if(event.getPlayer().getInventory().getItemInMainHand().getType() != plugin.getToolMaterial()) return;

        if(event.getPlayer().hasPermission("customvillagertrades.item.restore") && 
            event.getPlayer().isSneaking()
        ) {
            tradeManager.restoreMerchant((Merchant) entity);
            event.getPlayer().sendMessage(
                ChatColor.GREEN + "Restored vanilla trades for this villager"
            );
        }
        else if(
            event.getPlayer().hasPermission("customvillagertrades.item.reroll") && 
            !event.getPlayer().isSneaking()
        ) {
            if(tradeManager.rerollMerchant((Merchant) entity)) {
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