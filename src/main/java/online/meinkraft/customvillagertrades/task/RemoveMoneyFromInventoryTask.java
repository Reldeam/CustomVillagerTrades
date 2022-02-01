package online.meinkraft.customvillagertrades.task;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class RemoveMoneyFromInventoryTask implements Runnable {

    private final CustomVillagerTrades plugin;
    private final Inventory inventory;
    private final Player player;

    public RemoveMoneyFromInventoryTask(
        CustomVillagerTrades plugin, 
        Inventory inventory,
        Player player
    ) {
        super();
        this.plugin = plugin;
        this.inventory = inventory;
        this.player = player;
    }

    public RemoveMoneyFromInventoryTask(CustomVillagerTrades plugin, Inventory inventory) {
        this(plugin, inventory, null);
    }

    @Override
    public void run() {

        for(int index = 0; index < inventory.getSize(); index++) {
            ItemStack item = inventory.getItem(index);
            if(depositMoney(item)) {
                inventory.setItem(index, null);
            }
        }

    }

    private boolean depositMoney(ItemStack item) {
        
        if(item == null) return false;
        
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) return false;

        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();
        Double money = itemData.get(
            NamespacedKey.fromString("money", plugin),
            PersistentDataType.DOUBLE
        );

        if(money != null) {

            if(plugin.isEconomyEnabled() && player != null) {
                double totalAmount = money * item.getAmount();
                plugin.getEconomy().depositPlayer(player, totalAmount);
                player.sendMessage(
                    ChatColor.GREEN +
                    "Deposited " +
                    plugin.getEconomy().format(totalAmount) +
                    " into your account"
                );
            }
            
            return true;
            
        }

        return false;

    }

}