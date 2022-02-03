package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.Page;

public class PrevPageButton extends Button {

    public PrevPageButton(Material material) {
        super(material, "Previous Page");
    }

    public PrevPageButton() {
        this(Material.ARROW);
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        page.getEditor().prevPage((Player) event.getWhoClicked());
        return Result.DENY;
    }
    
}
