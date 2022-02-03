package online.meinkraft.customvillagertrades.task;

import online.meinkraft.customvillagertrades.gui.page.TradeListPage;

public class UpdateTradeListPageTask implements Runnable {

    private final TradeListPage page;

    public UpdateTradeListPageTask(TradeListPage page) {
        this.page = page;
    }

    @Override
    public void run() {
        page.updateEntries();
    }
    
}
