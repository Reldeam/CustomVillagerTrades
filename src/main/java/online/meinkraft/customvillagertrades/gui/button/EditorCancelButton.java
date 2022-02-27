package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.Editor;
import online.meinkraft.customvillagertrades.gui.page.Page;

public class EditorCancelButton extends Button {

    private final Editor editor;

    public EditorCancelButton(Editor editor) {
        super(Material.BLAZE_POWDER, editor.getMessage("discardButtonLabel"));
        this.editor = editor;
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        editor.close(true);
        return Result.DENY;
    }
    
}


