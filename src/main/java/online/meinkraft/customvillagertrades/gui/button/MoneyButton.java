package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.page.Page;

public class MoneyButton extends Button {

    public MoneyButton(
        Material material, 
        String currencyPrefix, 
        String currencySuffix
    ) {
        super(material, "Create Money");
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        return Result.DENY;
    }
    
}
