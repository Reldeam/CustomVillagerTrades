package online.meinkraft.customvillagertrades.gui.icon;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Icon {

    private ItemStack item;

    public Icon(
        Material material,
        String label, 
        List<String> lore
    ) {

        item = new ItemStack(material);
        setLabel(label);
        if(lore != null) setLore(lore);
        
    }

    public Icon(
        Material material,
        String label
    ) {
        this(material, label, null);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setLabel(String label) {

        ItemMeta itemMeta = (ItemMeta) item.getItemMeta();
        itemMeta.setDisplayName(label);
        item.setItemMeta(itemMeta);

    }

    public void setLore(List<String> lore) {

        if(lore == null) return;
        ItemMeta itemMeta = (ItemMeta) item.getItemMeta();
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

    }
    
}
