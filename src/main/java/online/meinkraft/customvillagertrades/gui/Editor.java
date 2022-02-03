package online.meinkraft.customvillagertrades.gui;

import java.util.List;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.gui.button.CancelButton;
import online.meinkraft.customvillagertrades.gui.button.NextPageButton;
import online.meinkraft.customvillagertrades.gui.button.PrevPageButton;
import online.meinkraft.customvillagertrades.gui.button.SaveButton;
import online.meinkraft.customvillagertrades.gui.icon.DisabledSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.PageIcon;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class Editor extends GUI {

    private static final double TRADES_PER_PAGE = 5;

    private final DisabledSlotIcon disabledSlot;
    private final SaveButton saveButton;
    private final CancelButton cancelButton;

    public Editor(CustomVillagerTrades plugin) {

        super(plugin);

        disabledSlot = new DisabledSlotIcon();
        saveButton = new SaveButton();
        cancelButton = new CancelButton();

        // create custom trade list pages
        List<CustomTrade> customTrades = plugin.getCustomTradeManager().getCustomTrades();
        int numPages = (int) Math.ceil(customTrades.size() / TRADES_PER_PAGE);
        int index = 0;
        for(int pageIndex = 0; pageIndex < numPages; pageIndex++) {
            int currentPage = pageIndex + 1;
            Page page = new Page(this, "CVT Editor - " + currentPage);
            for(int rowIndex = 0; rowIndex < TRADES_PER_PAGE; rowIndex++) {
                if(index >= customTrades.size()) break;
                CustomTradeEntry entry = new CustomTradeEntry(customTrades.get(index));
                setCustomTradeEntry(entry, page, rowIndex);
                index++;
            }

            // add previous page button
            if(pageIndex > 0) {
                page.addButton(45, "prevPage", new PrevPageButton());
            }

            // add next page button
            if(pageIndex < numPages - 1) {
                page.addButton(53, "nextPage", new NextPageButton());
            }

            // add page icon for tracking page number
            page.addIcon(49, new PageIcon(currentPage, numPages));

            // add cancel and save buttons
            page.addButton(51, "cancel", cancelButton);
            page.addButton(52, "save", saveButton);

            addPage("trades" + pageIndex, page);
        }

    }

    public void setCustomTradeEntry(CustomTradeEntry entry, Page page, int row) {

        int index = row * 9;

        page.addButton(index + 0, "rename" + row, entry.getRenameButton());

        page.addIcon(index + 1, disabledSlot);

        page.addItemStack(index + 2, entry.getFirstIngredient());
        page.addItemStack(index + 3, entry.getSecondIngredient());
        page.addItemStack(index + 4, entry.getResult());

        page.addIcon(index + 5, disabledSlot);

        page.addButton(index + 6, "config" + row, entry.getConfigButton());
        page.addButton(index + 7, "blueprint" + row, entry.getBlueprintButton());
        page.addButton(index + 8, "delete" + row, entry.getDeleteButton());
        
    }
    
}
