package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.page.EditorPage;
import online.meinkraft.customvillagertrades.gui.page.Page;

public class PrevPageButton extends Button {

    public PrevPageButton(EditorPage page, Material material) {
        super(material, page.getEditor().getMessage("previousPageButtonLabel"));
    }

    public PrevPageButton(EditorPage page) {
        this(page, Material.ARROW);
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        page.getGUI().prevPage((Player) event.getWhoClicked());
        return Result.DENY;
    }
    
}
