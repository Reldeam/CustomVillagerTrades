package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.Editor;
import online.meinkraft.customvillagertrades.gui.page.Page;

public class EditorSaveButton extends Button {

    private final Editor editor;

    public EditorSaveButton(Editor editor) {

        super(Material.SLIME_BALL, "§aSave Changes");
        this.editor = editor;

    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        editor.save();
        editor.close();
        return Result.DENY;
    }
    
}

