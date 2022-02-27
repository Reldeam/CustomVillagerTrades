package online.meinkraft.customvillagertrades.exception;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class IngredientsNotFoundException extends CustomVillagerTradesException {

    public IngredientsNotFoundException(CustomVillagerTrades plugin) {
        super(plugin, plugin.getMessage("ingredientsNotFoundException"));
    }

}
