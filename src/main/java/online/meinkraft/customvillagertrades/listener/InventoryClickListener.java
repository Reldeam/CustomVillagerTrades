package online.meinkraft.customvillagertrades.listener;

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

        // economy (money) related logic
        if(event.getClickedInventory().getType() == InventoryType.MERCHANT) {

            MerchantInventory inventory = (MerchantInventory) event.getClickedInventory();
            ItemStack item = inventory.getItem(event.getRawSlot());

            // stop player from clicking money in merchant inventories
            if(item != null) {
                ItemMeta itemMeta = item.getItemMeta();
                    if(itemMeta != null) {
                        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
                        Double money = itemData.get(
                            NamespacedKey.fromString("money", plugin),
                            PersistentDataType.DOUBLE
                        );
        
                        if(money != null) {
                            
                            // add result amount to player balance
                            if(event.getSlotType() == InventoryType.SlotType.RESULT) { 

                                Player player = (Player) event.getWhoClicked();

                                if(event.getClick() == ClickType.SHIFT_LEFT) {
                                    plugin.getServer().getScheduler().runTask(
                                        plugin, 
                                        new RemoveMoneyFromInventoryTask(
                                            plugin, 
                                            player.getInventory(),
                                            player
                                        )
                                    );
                                }
                                else {
                                    plugin.getServer().getScheduler().runTask(
                                        plugin, 
                                        new RemoveMoneyFromHandTask(
                                            plugin, 
                                            player
                                        )
                                    );
                                }      
                            }
                            else {
                                event.setCancelled(true);
                            }
                        }
                    } 

            }

        }

    }  

}