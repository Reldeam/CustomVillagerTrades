package online.meinkraft.customvillagertrades.task;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;

public class CheckCustomTradeEntryModified implements Runnable {

    private final CustomTradeEntry entry;

    public CheckCustomTradeEntryModified(CustomTradeEntry entry) {
        this.entry = entry;
    }

    @Override
    public void run() {
        entry.getPage().updateEntry(entry);
    }
    
}
