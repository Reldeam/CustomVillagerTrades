package online.meinkraft.customvillagertrades.listener;

import java.util.List;
import java.util.Random;

import online.meinkraft.customvillagertrades.trade.CustomTradeManager;
import online.meinkraft.customvillagertrades.villager.VillagerData;
import online.meinkraft.customvillagertrades.villager.VillagerManager;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

import org.bukkit.entity.Villager;
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
        Villager villager = (Villager) event.getEntity();
        Merchant merchant = (Merchant) event.getEntity();

        VillagerManager villagerManager = plugin.getVillagerManager();
        VillagerData data = villagerManager.get(villager);
        data.addVanillaTrade(villager.getVillagerLevel(), event.getRecipe());

        CustomTradeManager tradeManager = plugin.getCustomTradeManager();

        List<CustomTrade> trades = tradeManager.getValidTrades(merchant);
        if(trades.size() == 0) {
            // don't allow villager to acquire vanilla trade if they are disabled
            if(!plugin.isVanillaTradesAllowed()) event.setCancelled(true);
            return;
        }

        CustomTrade trade = tradeManager.chooseRandomTrade(trades);

        // chance of not getting the trade (if vanilla trades aren't disabled)
        if(
            plugin.isVanillaTradesAllowed() && 
            rand.nextDouble() > trade.getChance()
        ) {
            // keep vanilla trade
            event.setRecipe(event.getRecipe());
        }
        else {
            // set custom trade
            event.setRecipe(trade.getRecipe());
            data.addCustomTradeKey(trade.getKey());
        }

    }
    
}
