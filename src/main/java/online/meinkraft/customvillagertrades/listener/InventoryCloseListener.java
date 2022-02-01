
package online.meinkraft.customvillagertrades.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.MerchantInventory;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.task.RemoveMoneyFromInventoryTask;

public class InventoryCloseListener implements Listener {

    private final CustomVillagerTrades plugin;

    public InventoryCloseListener(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event) {

        if(event.getView().getTopInventory().getType() == InventoryType.MERCHANT) {

            MerchantInventory merchantInventory = (MerchantInventory) event.getView().getTopInventory();
            // delete result
            merchantInventory.setItem(2, null);

            Player player = (Player) event.getPlayer();

            plugin.getServer().getScheduler().runTask(
                plugin, 
                new RemoveMoneyFromInventoryTask(
                    plugin, 
                    event.getView().getTopInventory(),
                    player
                )
            );

            plugin.getServer().getScheduler().runTask(
                plugin, 
                new RemoveMoneyFromInventoryTask(
                    plugin, 
                    event.getView().getBottomInventory(),
                    player
                )
            );

        }
         
    }
    
}