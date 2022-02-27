
package online.meinkraft.customvillagertrades.exception;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class VillagerNotMerchantException extends CustomVillagerTradesException {

    public VillagerNotMerchantException(CustomVillagerTrades plugin) {
        super(plugin, plugin.getMessage("villagerNotMerchantException"));
    }

}
