package online.meinkraft.customvillagertrades.gui.button;

import java.util.List;

import org.bukkit.Material;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.Editor;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public abstract class CustomTradeButton extends Button {

    protected final Editor editor;
    protected CustomTradeEntry tradeEntry;

    public CustomTradeButton(
        Editor editor,
        CustomTradeEntry tradeEntry, 
        Material material,
        String label, 
        List<String> lore
    ) {
        super(material, label, lore);
        this.tradeEntry = tradeEntry;
        this.editor = editor;
    }

    public CustomTradeButton(
        Editor editor,
        CustomTradeEntry tradeEntry,
        Material material,
        String label
    ) {
        this(editor, tradeEntry, material, label, null);
    }

    public Editor getEditor() {
        return editor;
    }

    public CustomTradeEntry getCustomTradeEntry() {
        return tradeEntry;
    }

    public CustomTrade getCustomTrade() {
        return tradeEntry.getTrade();
    }
    
}
