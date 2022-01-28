package online.meinkraft.customvillagertrades.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Merchant;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class PlayerInteractEntityListener implements Listener {

    private final CustomVillagerTrades plugin;

    public PlayerInteractEntityListener(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        if(!event.getPlayer().hasPermission("customvillagertrades.use.rerollitem")) {
            return;
        }

        if(event.getPlayer().getInventory().getItemInMainHand().getType() != plugin.getRerollMaterial()) {
            return;
        }

        Entity entity = event.getRightClicked();
        if(entity instanceof Merchant) {
            if(plugin.rerollMerchant((Merchant) entity)) {
                event.getPlayer().sendMessage(ChatColor.GREEN + "Rerolled merchant");
            }
        }
        
    }

}