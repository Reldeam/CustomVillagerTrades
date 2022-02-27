package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.page.EditorPage;
import online.meinkraft.customvillagertrades.gui.page.Page;

public class NextPageButton extends Button {

    public NextPageButton(EditorPage page, Material material) {
        super(material, page.getEditor().getMessage("previousPageButtonLabel"));
    }

    public NextPageButton(EditorPage page) {
        this(page, Material.ARROW);
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        page.getGUI().nextPage((Player) event.getWhoClicked());
        return Result.DENY;
    }
    
}
