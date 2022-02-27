package online.meinkraft.customvillagertrades.task;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class RemoveMoneyFromHandTask implements Runnable {

    private final CustomVillagerTrades plugin;
    private final Player player;

    public RemoveMoneyFromHandTask(
        CustomVillagerTrades plugin, 
        Player player
    ) {
        super();
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {

        if(player != null && player.getItemOnCursor() != null) {
            ItemStack item = player.getItemOnCursor();
            if(depositMoney(item)) {
                player.setItemOnCursor(null);
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
                player.sendMessage(String.format(
                    ChatColor.GREEN + plugin.getMessage("depositedMoney"),
                    plugin.getEconomy().format(totalAmount)
                ));
            }
            
            return true;
            
        }

        return false;

    }

}