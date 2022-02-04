package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.page.Page;

public class CustomTradeDeleteButton extends CustomTradeButton {

    public CustomTradeDeleteButton(CustomTradeEntry tradeEntry) {
        super(tradeEntry, Material.BARRIER, "Delete Custom Trade");
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        getCustomTradeEntry().getTradeListPage().removeCustomTradeEntry(getCustomTradeEntry());
        return Result.DENY;
    }
    
}
