package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import online.meinkraft.customvillagertrades.gui.Page;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class CustomTradeBlueprintButton extends CustomTradeButton {

    public CustomTradeBlueprintButton(CustomTrade trade) {
        
        super(trade, Material.FILLED_MAP, "Get Blueprint");

        ItemStack item = getItem();
        item.addUnsafeEnchantment(Enchantment.MENDING, 1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        return Result.DENY;
    }
    
}
