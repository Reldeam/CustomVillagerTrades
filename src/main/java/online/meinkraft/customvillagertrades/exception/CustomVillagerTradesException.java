package online.meinkraft.customvillagertrades.exception;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public abstract class CustomVillagerTradesException extends Exception {

    protected final CustomVillagerTrades plugin;

    public CustomVillagerTradesException(CustomVillagerTrades plugin, String message) {
        super(message);
        this.plugin = plugin;
    }

    public CustomVillagerTrades getPlugin() {
        return plugin;
    }
    
}
