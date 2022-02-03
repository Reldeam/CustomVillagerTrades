package online.meinkraft.customvillagertrades.gui.page;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.GUI;
import online.meinkraft.customvillagertrades.gui.button.CancelButton;
import online.meinkraft.customvillagertrades.gui.button.NextPageButton;
import online.meinkraft.customvillagertrades.gui.button.PrevPageButton;
import online.meinkraft.customvillagertrades.gui.button.SaveButton;
import online.meinkraft.customvillagertrades.gui.icon.DeletedSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.DisabledSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.ModifiedSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.PageIcon;
import online.meinkraft.customvillagertrades.task.UpdateTradeListPageTask;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class TradeListPage extends Page {

    private final int COLUMNS_PER_ROW = 9;

    private final int pageIndex;
    private final int totalPages;

    private final DisabledSlotIcon disabledSlot;
    private final DeletedSlotIcon deletedSlot;
    private final ModifiedSlotIcon modifiedSlot;

    private List<CustomTradeEntry> tradeEntries = new ArrayList<>();

    public TradeListPage(
        GUI gui, 
        String title,
        int pageIndex,
        int totalPages,
        SaveButton saveButton,
        CancelButton cancelButton
    ) {
        super(gui, title);

        this.pageIndex = pageIndex;
        this.totalPages = totalPages;

        disabledSlot = new DisabledSlotIcon();
        deletedSlot = new DeletedSlotIcon();
        modifiedSlot = new ModifiedSlotIcon();

        setButton(51, "cancel", cancelButton);
        setButton(52, "save", saveButton);

        // add previous and next page buttons
        if(pageIndex > 0) setButton(45, "prevPage", new PrevPageButton());
        if(pageIndex < totalPages - 1) setButton(53, "nextPage", new NextPageButton());

        setIcon(49, new PageIcon(pageIndex + 1, totalPages));

    }

    @Override
    @EventHandler
    public void onClick(InventoryClickEvent event) {

        super.onClick(event);

        int slotIndex = event.getRawSlot();
        int row = (int) Math.floor(slotIndex / COLUMNS_PER_ROW);

        CustomTradeEntry tradeEntry = null;
        if(row < tradeEntries.size()) tradeEntry = tradeEntries.get(row);

        if(tradeEntry != null && !tradeEntry.isDeleted()) {
            getGUI().getPlugin().getServer().getScheduler().runTask(
                getGUI().getPlugin(), 
                new UpdateTradeListPageTask(this)
            );
        }

    }

    public void updateEntries() {
        for(CustomTradeEntry entry : tradeEntries) {
            updateEntry(entry);
        }
    }

    public void updateEntry(CustomTradeEntry entry) {

        int row = tradeEntries.indexOf(entry);
        if(row < 0) return;

        int rowIndex = row * COLUMNS_PER_ROW;

        if(entry.isModified()) {
            setIcon(rowIndex + 1, modifiedSlot);
            setIcon(rowIndex + 5, modifiedSlot);
        }
        else {
            setIcon(rowIndex + 1, disabledSlot);
            setIcon(rowIndex + 5, disabledSlot);
        }

    }

    public void addPageIcon(int currentPage, int totalPages) {
        setIcon(49, new PageIcon(currentPage, totalPages));
    }

    public boolean addCustomTrade(CustomTrade trade) {

        int row = tradeEntries.size();
        if(row > 4) return false;

        CustomTradeEntry entry = new CustomTradeEntry(row, this, trade);
        tradeEntries.add(entry);

        int index = row * 9;

        setButton(index + 0, "rename" + row, entry.getRenameButton());

        setIcon(index + 1, disabledSlot);

        addItemStack(index + 2, entry.getFirstIngredient());
        addItemStack(index + 3, entry.getSecondIngredient());
        addItemStack(index + 4, entry.getResult());

        setIcon(index + 5, disabledSlot);

        setButton(index + 6, "config" + row, entry.getConfigButton());
        setButton(index + 7, "blueprint" + row, entry.getBlueprintButton());
        setButton(index + 8, "delete" + row, entry.getDeleteButton());

        return true;
        
    }

    public boolean removeCustomTradeEntry(CustomTradeEntry entry) {

        int row = tradeEntries.indexOf(entry);
        if(row < 0) return false;

        for(int column = 0; column < COLUMNS_PER_ROW; column++) {
            setIcon(row * COLUMNS_PER_ROW + column, deletedSlot);
        }

        entry.delete();
        return true;
    }
    
    public int getPageIndex() {
        return pageIndex;
    }

    public int getTotalPages() {
        return totalPages;
    }

}
