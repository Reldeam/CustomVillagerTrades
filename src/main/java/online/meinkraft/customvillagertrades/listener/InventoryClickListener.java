package online.meinkraft.customvillagertrades.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
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
            ItemStack item = event.getClickedInventory().getItem(event.getSlot());
            Player player = (Player) event.getWhoClicked();

            // check for clicking money ingredients in a merchant inventory
            if(inventoryType == InventoryType.MERCHANT) {

                // stop player from clicking money in merchant inventories
                if(isItemMoney(item) && !plugin.isCurrencyPhysical()) {
                                
                    // add result amount to player balance
                    if(event.getSlotType() == InventoryType.SlotType.RESULT) { 
    
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

            // drop click item to deposit it
            else if(event.getClick() == ClickType.DROP && isItemMoney(item)) {
                plugin.getServer().getScheduler().runTask(
                    plugin, 
                    new RemoveMoneyFromInventoryTask(
                        plugin,
                        event.getClickedInventory(),
                        player,
                        event.getSlot()
                    )
                ); 
                event.setCancelled(true);
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

}