package online.meinkraft.customvillagertrades.task;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class RemoveMoneyTask implements Runnable {

    private final CustomVillagerTrades plugin;
    private final Inventory inventory;
    private final Player player;

    public RemoveMoneyTask(
        CustomVillagerTrades plugin, 
        Inventory inventory,
        Player player
    ) {
        super();
        this.plugin = plugin;
        this.inventory = inventory;
        this.player = player;
    }

    public RemoveMoneyTask(CustomVillagerTrades plugin, Inventory inventory) {
        this(plugin, inventory, null);
    }

    @Override
    public void run() {

        for(int index = 0; index < inventory.getSize(); index++) {

            ItemStack item = inventory.getItem(index);
            if(item == null) continue;
            
            ItemMeta itemMeta = item.getItemMeta();
            if(itemMeta == null) continue;

            PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
            Double money = itemData.get(
                NamespacedKey.fromString("money", plugin),
                PersistentDataType.DOUBLE
            );

            if(money != null) {

                if(plugin.isEconomyEnabled() && player != null) {
                    plugin.getEconomy().depositPlayer(player, money * item.getAmount());
                }
                
                inventory.clear(index);
                
            }

        }

    }

}