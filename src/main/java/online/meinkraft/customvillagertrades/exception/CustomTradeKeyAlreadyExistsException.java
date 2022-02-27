package online.meinkraft.customvillagertrades.exception;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class CustomTradeKeyAlreadyExistsException extends CustomVillagerTradesException {

    public CustomTradeKeyAlreadyExistsException(CustomVillagerTrades plugin) {
        super(plugin, plugin.getMessage("customTradeKeyAlreadyExistsException"));
    }

}

