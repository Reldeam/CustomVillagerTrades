package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.page.Page;

public class CancelButton extends Button {

    public CancelButton() {
        super(Material.BLAZE_POWDER, "§cDiscard Changes");
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        page.getGUI().close();
        return Result.DENY;
    }
    
}


