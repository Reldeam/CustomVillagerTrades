package online.meinkraft.customvillagertrades.gui;

import java.util.List;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.gui.button.CancelButton;
import online.meinkraft.customvillagertrades.gui.button.SaveButton;
import online.meinkraft.customvillagertrades.gui.page.TradeListPage;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class Editor extends GUI {

    private static final double TRADES_PER_PAGE = 5;

    private final SaveButton saveButton;
    private final CancelButton cancelButton;

    public Editor(CustomVillagerTrades plugin) {

        super(plugin);

        saveButton = new SaveButton();
        cancelButton = new CancelButton();

        // create custom trade list pages
        List<CustomTrade> customTrades = plugin.getCustomTradeManager().getCustomTrades();
        int totalPages = (int) Math.ceil(customTrades.size() / TRADES_PER_PAGE);
        int index = 0;

        for(int pageIndex = 0; pageIndex < totalPages; pageIndex++) {

            int currentPage = pageIndex + 1;
            TradeListPage page = new TradeListPage(
                this, 
                "CVT Editor - " + currentPage,
                pageIndex,
                totalPages,
                saveButton,
                cancelButton
            );

            for(int rowIndex = 0; rowIndex < TRADES_PER_PAGE; rowIndex++) {
                if(index >= customTrades.size()) break;
                page.addCustomTrade(customTrades.get(index));
                index++;
            }

            addPage("tradeList" + currentPage, page);

        }

    }
    
}
