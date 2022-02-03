package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.page.Page;

public class CustomTradeConfigButton extends CustomTradeButton {

    public CustomTradeConfigButton(CustomTradeEntry tradeEntry) {
        super(tradeEntry, Material.HOPPER, "Configure Custom Trade");
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        return Result.DENY;
    }
    
}
