package online.meinkraft.customvillagertrades.gui.button;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.Page;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class CustomTradeRenameButton extends CustomTradeButton {

    public CustomTradeRenameButton(CustomTrade trade) {
        super(
            trade,
            Material.WRITABLE_BOOK, // item material
            trade.getKey(), // display name
            Arrays.asList(new String[]{"Rename"}) // lore
        );
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        return Result.DENY;
    }
    
}
