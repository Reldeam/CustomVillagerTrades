package online.meinkraft.customvillagertrades.gui.icon;

import org.bukkit.Material;

public class PageIcon extends Icon {

    public PageIcon(int currentPage, int totalPages) {
        super(Material.BOOK, "Page " + currentPage + " of " + totalPages);
    }
    
}
