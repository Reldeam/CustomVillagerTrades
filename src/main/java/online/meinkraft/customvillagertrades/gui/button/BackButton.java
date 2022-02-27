package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.page.EditorPage;
import online.meinkraft.customvillagertrades.gui.page.Page;

public class BackButton extends Button {

    private EditorPage page;

    public BackButton(Material material, EditorPage page) {
        super(material, page.getEditor().getMessage("backButtonLabel"));
        this.page = page;
    }

    public BackButton(EditorPage page) {
        this(Material.ARROW, page);
    }

    public BackButton() {
        this(Material.ARROW, null);
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        if(this.page != null) {
            page.getGUI().openPage(this.page, (Player) event.getWhoClicked());
        }
        return Result.DENY;
    }

    public void setPage(EditorPage page) {
        this.page = page;
    }

    public Page getPage() {
        return page;
    }
    
}

