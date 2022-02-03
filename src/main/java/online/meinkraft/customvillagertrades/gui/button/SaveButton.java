package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.page.Page;

public class SaveButton extends Button {

    public SaveButton() {
        super(Material.SLIME_BALL, "§aSave Changes");
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        return Result.DENY;
    }
    
}

