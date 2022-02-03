package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.page.Page;

public class NextPageButton extends Button {

    public NextPageButton(Material material) {
        super(material, "Next Page");
    }

    public NextPageButton() {
        this(Material.ARROW);
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        page.getGUI().nextPage((Player) event.getWhoClicked());
        return Result.DENY;
    }
    
}
