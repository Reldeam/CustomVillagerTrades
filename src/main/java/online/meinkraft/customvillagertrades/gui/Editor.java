package online.meinkraft.customvillagertrades.gui;

import java.util.ArrayList;
import java.util.List;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.gui.button.EditorCancelButton;
import online.meinkraft.customvillagertrades.gui.button.EditorSaveButton;
import online.meinkraft.customvillagertrades.gui.page.TradeListPage;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class Editor extends GUI {

    private static final double TRADES_PER_PAGE = 5;

    private List<TradeListPage> tradeListPages;

    private final EditorSaveButton saveButton;
    private final EditorCancelButton cancelButton;

    public Editor(CustomVillagerTrades plugin) {

        super(plugin);

        tradeListPages = new ArrayList<>();

        saveButton = new EditorSaveButton(this);
        cancelButton = new EditorCancelButton(this);

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

            addTradeListPage(page);

        }

    }

    public void addTradeListPage(TradeListPage page) {
        int pageNum = tradeListPages.size();
        addPage("tradeList" + pageNum, page);
        tradeListPages.add(page);
    }

    public void save() {

        getPlugin().getLogger().info(getPlayer() + " has made changes using the in-game editor");

        for(TradeListPage page : tradeListPages) {
            for(CustomTradeEntry entry : page.getCustomTradeEntries()) {

                // entry is deleted
                if(entry.isDeleted()) {
                    CustomTrade trade = entry.getTrade();
                    getPlugin().getTradesConfig().set(trade.getKey(), null);
                    continue;
                }

                //TODO check if itemstacks are updated

            }
        }

        getPlugin().saveTradesConfig();
        getPlugin().getCustomTradeManager().load();

    }

    @Override
    public CustomVillagerTrades getPlugin() {
        return (CustomVillagerTrades) super.getPlugin();
    }
    
}
