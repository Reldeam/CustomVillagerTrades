package online.meinkraft.customvillagertrades.gui;

import java.util.List;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.gui.button.DisabledSlot;
import online.meinkraft.customvillagertrades.gui.button.NextPageButton;
import online.meinkraft.customvillagertrades.gui.button.PrevPageButton;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class Editor extends GUI {

    private final DisabledSlot disabledSlot;

    public Editor(CustomVillagerTrades plugin) {

        super(plugin);

        disabledSlot = new DisabledSlot();

        Page testPage1 = new Page(this, "Test Page 1");
        testPage1.addButton(45, "prevPage", new PrevPageButton());
        testPage1.addButton(53, "nextPage", new NextPageButton());

        Page testPage2 = new Page(this, "Test Page 2");
        testPage2.addButton(45, "prevPage", new PrevPageButton());
        testPage2.addButton(53, "nextPage", new NextPageButton());

        List<CustomTrade> customTrades = plugin.getCustomTradeManager().getCustomTrades();
        CustomTradeEntry entry = new CustomTradeEntry(customTrades.get(0));
        setCustomTradeEntry(entry, testPage2, 0);

        addPage("test", testPage1);
        addPage("test", testPage2);

    }

    public void setCustomTradeEntry(CustomTradeEntry entry, Page page, int row) {
        int index = row * 9;
        page.addButton(index + 0, "rename" + row, entry.getRenameButton());
        page.addButton(index + 1, "leftDisabled" + row, disabledSlot);
        page.addButton(index + 5, "rightDisabled" + row, disabledSlot);
        page.addButton(index + 6, "config" + row, entry.getConfigButton());
        page.addButton(index + 7, "blueprint" + row, entry.getBlueprintButton());
        page.addButton(index + 8, "delete" + row, entry.getDeleteButton());
    }
    
}
