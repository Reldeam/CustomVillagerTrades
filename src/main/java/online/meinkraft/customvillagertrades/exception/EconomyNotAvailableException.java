package online.meinkraft.customvillagertrades.exception;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class EconomyNotAvailableException extends CustomVillagerTradesException {

    public EconomyNotAvailableException(CustomVillagerTrades plugin) {
        super(plugin, plugin.getMessage("economyNotAvailableException"));
    }

}
