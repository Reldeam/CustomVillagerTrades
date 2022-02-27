
package online.meinkraft.customvillagertrades.exception;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class EconomyNotEnabledException extends CustomVillagerTradesException {

    public EconomyNotEnabledException(CustomVillagerTrades plugin) {
        super(plugin, plugin.getMessage("economyNotEnabledException"));
    }

}
