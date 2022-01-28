package online.meinkraft.customvillagertrades.listener;

import java.util.List;
import java.util.Random;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.util.CustomTrade;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.Merchant;

public class VillagerAcquireTradeListener implements Listener {

    private final CustomVillagerTrades plugin;

    public VillagerAcquireTradeListener(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {

        Random rand = new Random();
        Merchant merchant = (Merchant) event.getEntity();

        List<CustomTrade> trades = plugin.getValidTrades(merchant);
        if(trades.size() == 0) {
            if(plugin.disableVanillaTrades()) event.setCancelled(true);
            return;
        }

        CustomTrade trade = plugin.chooseRandomTrade(trades);

        // chance of not getting the trade (if vanilla trades aren't disabled)
        if(
            !plugin.disableVanillaTrades() && 
            rand.nextDouble() > trade.getChance()
        ) {
            // keep vanilla trade
            event.setRecipe(event.getRecipe());
        }
        else {
            // set custom trade
            event.setRecipe(trade.getRecipe());
        }

    }
    
}
