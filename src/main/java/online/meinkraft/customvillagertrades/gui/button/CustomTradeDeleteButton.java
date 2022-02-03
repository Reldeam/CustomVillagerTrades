package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.Page;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class CustomTradeDeleteButton extends CustomTradeButton {

    public CustomTradeDeleteButton(CustomTrade trade) {
        super(trade, Material.BARRIER, "Delete Custom Trade");
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        return Result.DENY;
    }
    
}
