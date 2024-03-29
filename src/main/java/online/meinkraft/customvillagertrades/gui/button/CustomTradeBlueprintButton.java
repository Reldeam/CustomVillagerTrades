package online.meinkraft.customvillagertrades.gui.button;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.Editor;
import online.meinkraft.customvillagertrades.gui.page.Page;

public class CustomTradeBlueprintButton extends CustomTradeButton {

    private final List<String> lore;

    public CustomTradeBlueprintButton(Editor editor, CustomTradeEntry tradeEntry) {
        
        super(
            editor, 
            tradeEntry, 
            Material.FILLED_MAP, 
            editor.getMessage("blueprintButtonLabel")
        );

        ItemStack item = getItem();
        item.addUnsafeEnchantment(Enchantment.MENDING, 1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        lore = editor.getMessage("blueprintButtonDescription").lines().toList();

    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {

        ItemStack item = new ItemStack(Material.FILLED_MAP);

        item.addUnsafeEnchantment(Enchantment.MENDING, 1);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§d" + tradeEntry.getTrade().getKey() + " Blueprint");

        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(
            new NamespacedKey(page.getGUI().getPlugin(), "blueprint"), 
            PersistentDataType.STRING, 
            tradeEntry.getTrade().getKey()
        );

        item.setItemMeta(meta);

        Player player = (Player) event.getWhoClicked();
        player.setItemOnCursor(item);

        return Result.DENY;
    }
    
}
