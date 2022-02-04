package online.meinkraft.customvillagertrades.gui.button;

import java.util.List;

import org.bukkit.Material;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public abstract class CustomTradeButton extends Button {

    protected CustomTradeEntry tradeEntry;

    public CustomTradeButton(
        CustomTradeEntry tradeEntry, 
        Material material,
        String label, 
        List<String> lore
    ) {
        super(material, label, lore);
        this.tradeEntry = tradeEntry;
    }

    public CustomTradeButton(
        CustomTradeEntry tradeEntry,
        Material material,
        String label
    ) {
        this(tradeEntry, material, label, null);
    }

    public CustomTradeEntry getCustomTradeEntry() {
        return tradeEntry;
    }

    public CustomTrade getCustomTrade() {
        return tradeEntry.getTrade();
    }
    
}
