package online.meinkraft.customvillagertrades.gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Editor {

    private final Inventory inventory;

    public Editor() {
        inventory = Bukkit.createInventory(
            null, 
            54, 
            "Custom Villager Trades Editor"
        );

        ItemStack leftArrow = new ItemStack(Material.PLAYER_WALL_HEAD, 1);

        SkullMeta skullMeta = (SkullMeta) leftArrow.getItemMeta();

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(
            UUID.fromString("35c589bd-7536-4fdb-946f-d872bd843d4a")
        );

        skullMeta.setDisplayName("Previous Page");
        skullMeta.setOwningPlayer(offlinePlayer);
        leftArrow.setItemMeta(skullMeta);

        inventory.setItem(45, leftArrow);
        
    }
    
    public Inventory getInventory() {
        return inventory;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
    
}
