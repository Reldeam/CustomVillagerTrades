package online.meinkraft.customvillagertrades.gui.button;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import online.meinkraft.customvillagertrades.gui.page.Page;

public abstract class Button {

    private ItemStack item;
    
    public Button(
        Material material,
        String label, 
        List<String> lore
    ) {

        item = new ItemStack(material);
        ItemMeta itemMeta = (ItemMeta) item.getItemMeta();

        itemMeta.setDisplayName(label);
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

    }

    public Button(Material material, String label) {
        this(material, label, null);
    }

    public abstract Result onClick(Page page, InventoryClickEvent event);

    public void setKey(NamespacedKey key, String label) {
        ItemMeta itemMeta = (ItemMeta) item.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(key, PersistentDataType.STRING, label);
        item.setItemMeta(itemMeta);
    }

    public ItemStack getItem() {
        return item;
    }
    
}
