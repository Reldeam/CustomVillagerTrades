package online.meinkraft.customvillagertrades.gui.button;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.page.Page;

public class CustomTradeRenameButton extends CustomTradeButton {

    public CustomTradeRenameButton(CustomTradeEntry tradeEntry) {
        super(
            tradeEntry,
            Material.WRITABLE_BOOK, // item material
            tradeEntry.getTrade().getKey(), // display name
            Arrays.asList(new String[]{"(click to rename)"}) // lore
        );
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        return Result.DENY;
    }
    
}
