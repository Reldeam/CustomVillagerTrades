package online.meinkraft.customvillagertrades.gui.button;

import java.util.List;

import org.bukkit.Material;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public abstract class CustomTradeButton extends Button {

    protected final CustomTrade trade;

    public CustomTradeButton(
        CustomTrade trade, 
        Material material,
        String label, 
        List<String> lore
    ) {
        super(material, label, lore);
        this.trade = trade;
    }

    public CustomTradeButton(
        CustomTrade trade, 
        Material material,
        String label
    ) {
        this(trade, material, label, null);
    }

    public CustomTrade getCustomTrade() {
        return trade;
    }
    
}
