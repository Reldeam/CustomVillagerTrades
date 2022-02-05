package online.meinkraft.customvillagertrades.gui.page;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.GUI;
import online.meinkraft.customvillagertrades.gui.button.EditorCancelButton;
import online.meinkraft.customvillagertrades.gui.button.NextPageButton;
import online.meinkraft.customvillagertrades.gui.button.PrevPageButton;
import online.meinkraft.customvillagertrades.gui.button.EditorSaveButton;
import online.meinkraft.customvillagertrades.gui.button.MoneyButton;
import online.meinkraft.customvillagertrades.gui.icon.DeletedSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.EmptySlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.ModifiedSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.PageIcon;
import online.meinkraft.customvillagertrades.gui.icon.UnmodifiedSlotIcon;
import online.meinkraft.customvillagertrades.task.UpdateTradeListPageTask;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class TradeListPage extends Page {

    private final int COLUMNS_PER_ROW = 9;

    private final int pageIndex;
    private final int totalPages;

    private final UnmodifiedSlotIcon unmodifiedSlot;
    private final DeletedSlotIcon deletedSlot;
    private final ModifiedSlotIcon modifiedSlot;
    private final EmptySlotIcon emptySlot;

    private List<CustomTradeEntry> tradeEntries = new ArrayList<>();

    public TradeListPage(
        GUI gui, 
        String title,
        int pageIndex,
        int totalPages,
        EditorSaveButton saveButton,
        EditorCancelButton cancelButton,
        MoneyButton moneyButton
    ) {
        super(gui, title);

        this.pageIndex = pageIndex;
        this.totalPages = totalPages;

        unmodifiedSlot = new UnmodifiedSlotIcon();
        deletedSlot = new DeletedSlotIcon();
        modifiedSlot = new ModifiedSlotIcon();
        emptySlot = new EmptySlotIcon();

        // save and discard buttons
        setButton(51, "cancel", cancelButton);
        setButton(52, "save", saveButton);

        // create money button
        if(moneyButton != null) setButton(47, "money", moneyButton);
        else setIcon(47, emptySlot);

        // add previous and next page buttons
        if(pageIndex > 0) setButton(45, "prevPage", new PrevPageButton());
        else setIcon(45, emptySlot);
        
        if(pageIndex < totalPages - 1) setButton(53, "nextPage", new NextPageButton());
        else setIcon(53, emptySlot);

        setIcon(49, new PageIcon(pageIndex + 1, totalPages));

        // add empty slots
        setIcon(46, emptySlot);
        setIcon(48, emptySlot);
        setIcon(50, emptySlot);

    }

    @Override
    @EventHandler
    public void onClick(InventoryClickEvent event) {

        super.onClick(event);

        if(event.getSlotType() == SlotType.OUTSIDE) return;

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

        entry.updateItems();

        if(entry.isModified()) {
            setIcon(rowIndex + 1, modifiedSlot);
            setIcon(rowIndex + 5, modifiedSlot);
        }
        else {
            setIcon(rowIndex + 1, unmodifiedSlot);
            setIcon(rowIndex + 5, unmodifiedSlot);
        }

    }

    public void addPageIcon(int currentPage, int totalPages) {
        setIcon(49, new PageIcon(currentPage, totalPages));
    }

    public boolean addCustomTrade(CustomTrade trade, TradeConfigPage configPage) {

        int row = tradeEntries.size();
        if(row > 4) return false;

        CustomTradeEntry entry = new CustomTradeEntry(row, this, configPage, trade);
        tradeEntries.add(entry);

        int index = row * 9;

        setIcon(index + 0, entry.getKeyIcon());
        
        setIcon(index + 1, unmodifiedSlot);

        addItemStack(index + 2, entry.getFirstIngredient());
        addItemStack(index + 3, entry.getSecondIngredient());
        addItemStack(index + 4, entry.getResult());

        setIcon(index + 5, unmodifiedSlot);

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
    
    public List<CustomTradeEntry> getCustomTradeEntries() {
        return tradeEntries;
    }

}
