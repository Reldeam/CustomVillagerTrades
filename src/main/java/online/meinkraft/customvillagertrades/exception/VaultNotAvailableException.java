package online.meinkraft.customvillagertrades.exception;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class VaultNotAvailableException extends CustomVillagerTradesException {

    public VaultNotAvailableException(CustomVillagerTrades plugin) {
        super(plugin, plugin.getMessage("vaultNotAvailableException"));
    }

}
