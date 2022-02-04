package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.page.Page;
import online.meinkraft.customvillagertrades.gui.page.TradeConfigPage;

public class CustomTradeConfigButton extends CustomTradeButton {

    private final TradeConfigPage configPage;

    public CustomTradeConfigButton(
        TradeConfigPage configPage,
        CustomTradeEntry tradeEntry
    ) {
        super(tradeEntry, Material.HOPPER, "Configure Custom Trade");
        this.configPage = configPage;
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {
        configPage.setCustomTradeEntry(getCustomTradeEntry());
        page.getGUI().openPage(configPage, page.getGUI().getPlayer());
        return Result.DENY;
    }
    
}
