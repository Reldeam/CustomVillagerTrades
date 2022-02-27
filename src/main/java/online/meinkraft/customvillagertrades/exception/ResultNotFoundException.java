package online.meinkraft.customvillagertrades.exception;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class ResultNotFoundException extends CustomVillagerTradesException {

    public ResultNotFoundException(CustomVillagerTrades plugin) {
        super(plugin, plugin.getMessage("resultNotFoundException"));
    }

}
