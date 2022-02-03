package online.meinkraft.customvillagertrades.gui.page;

import java.util.ArrayList;
import java.util.List;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.GUI;
import online.meinkraft.customvillagertrades.gui.button.CancelButton;
import online.meinkraft.customvillagertrades.gui.button.NextPageButton;
import online.meinkraft.customvillagertrades.gui.button.PrevPageButton;
import online.meinkraft.customvillagertrades.gui.button.SaveButton;
import online.meinkraft.customvillagertrades.gui.icon.DeletedSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.DisabledSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.PageIcon;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class TradeListPage extends Page {

    private final int COLUMNS_PER_ROW = 9;

    private final DisabledSlotIcon disabledSlot;
    private final DeletedSlotIcon deletedSlot;
    private List<CustomTradeEntry> tradeEntries = new ArrayList<>();

    public TradeListPage(
        GUI editor, 
        String title,
        SaveButton saveButton,
        CancelButton cancelButton
    ) {
        super(editor, title);
        disabledSlot = new DisabledSlotIcon();
        deletedSlot = new DeletedSlotIcon();

        addButton(51, "cancel", cancelButton);
        addButton(52, "save", saveButton);

    }

    public void addNextPageButton() {
        addButton(53, "nextPage", new NextPageButton());
    }

    public void addPrevPageButton() {
        addButton(45, "prevPage", new PrevPageButton());
    }

    public void addPageIcon(int currentPage, int totalPages) {
        addIcon(49, new PageIcon(currentPage, totalPages));
    }

    public boolean addCustomTrade(CustomTrade trade) {

        int row = tradeEntries.size();
        if(row > 4) return false;

        CustomTradeEntry entry = new CustomTradeEntry(this, trade);
        tradeEntries.add(entry);

        int index = row * 9;

        addButton(index + 0, "rename" + row, entry.getRenameButton());

        addIcon(index + 1, disabledSlot);

        addItemStack(index + 2, entry.getFirstIngredient());
        addItemStack(index + 3, entry.getSecondIngredient());
        addItemStack(index + 4, entry.getResult());

        addIcon(index + 5, disabledSlot);

        addButton(index + 6, "config" + row, entry.getConfigButton());
        addButton(index + 7, "blueprint" + row, entry.getBlueprintButton());
        addButton(index + 8, "delete" + row, entry.getDeleteButton());

        return true;
        
    }

    public boolean removeCustomTradeEntry(CustomTradeEntry entry) {
        int row = tradeEntries.indexOf(entry);
        if(row < 0) return false;

        for(int column = 0; column < COLUMNS_PER_ROW; column++) {
            addIcon(row * COLUMNS_PER_ROW + column, deletedSlot);
        }

        return true;
    }
    
}
