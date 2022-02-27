package online.meinkraft.customvillagertrades.gui.button;


import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.page.EditorPage;
import online.meinkraft.customvillagertrades.gui.page.Page;
import online.meinkraft.customvillagertrades.gui.page.TradeListPage;

public class ConfigBackButton extends BackButton {
    
    public ConfigBackButton(EditorPage page) {
        super(page);
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {

        if(getPage() instanceof TradeListPage) {
            TradeListPage configPage = (TradeListPage) getPage();
            configPage.updateEntries();
        }

        return super.onClick(page, event);
        
    }

}
