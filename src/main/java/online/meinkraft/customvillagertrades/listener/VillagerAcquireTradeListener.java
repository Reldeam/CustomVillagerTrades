package online.meinkraft.customvillagertrades.listener;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import online.meinkraft.customvillagertrades.CustomTrade;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;

public class VillagerAcquireTradeListener implements Listener {

    private List<CustomTrade> customTrades;

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) throws FileNotFoundException{

        Random rand = new Random();
        Villager villager = (Villager) event.getEntity();
        Merchant merchant = (Merchant) event.getEntity();
        

        for(CustomTrade trade : customTrades) {

            List<Villager.Profession> professions = trade.getProfessions();
            List<Integer> levels = trade.getLevels();
            List<Villager.Type> villagerTypes = trade.getVillagerTypes();
            List<Biome> biomes = trade.getBiomes();

            // chance of getting the trade
            if(rand.nextDouble() > trade.getChance()) {
                continue;
            }

            // trader must have the right profession(s)
            if(professions != null && !professions.contains(villager.getProfession())) {
                continue;
            }

            // trader must have the right level(s)
            if(levels != null && !levels.contains(villager.getVillagerLevel())) {
                continue;
            }

            // trader must be of the right type(s)
            if(villagerTypes != null && !villagerTypes.contains(villager.getVillagerType())) {
                continue;
            }

            // trader must be in the right biome(s)
            if(biomes != null) {
                Location location = villager.getLocation();
                Biome biome = location.getWorld().getBiome(location);
                if(!biomes.contains(biome)) continue;
            }
            
            // tader can't sell the same type of thing more than once
            boolean resultExists = false;
            for(MerchantRecipe recipe : merchant.getRecipes()) {
                if(recipe.getResult().getType() == trade.getResult().getType()) {
                    resultExists = true;
                    break;
                }
            }
            if(resultExists) continue;

            // replace recipe with custom recipe
            event.setRecipe(trade.getRecipe());

        }

    }

    public void setCustomTrades(List<CustomTrade> customTrades) {
        this.customTrades = customTrades;
    }
    
}
