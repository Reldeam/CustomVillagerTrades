package online.meinkraft.customvillagertrades.trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.exception.VillagerNotMerchantException;
import online.meinkraft.customvillagertrades.util.WeightedCollection;
import online.meinkraft.customvillagertrades.villager.VillagerData;
import online.meinkraft.customvillagertrades.villager.VillagerManager;

public class CustomTradeManager {

    private Map<String, CustomTrade> customTrades = new HashMap<>();
    private final CustomVillagerTrades plugin;
    private final VillagerManager villagerManager;

    public CustomTradeManager(
        CustomVillagerTrades plugin
    ) {
        this.plugin = plugin;
        this.villagerManager = plugin.getVillagerManager();
    }

    public void load() {
        customTrades = CustomTradeLoader.loadTrades(plugin);
    }

    public void refreshTrades(Merchant merchant) {

        VillagerManager villagerManager = plugin.getVillagerManager();
        VillagerData data = villagerManager.getData((Villager) merchant);

        List<MerchantRecipe> oldRecipes = merchant.getRecipes();
        List<MerchantRecipe> newRecipes = new ArrayList<>();
        
        int customTradeIndex = 0;
        for(int index = 0; index < oldRecipes.size(); index++) {
            if(data.isCustomTrade(index)) {

                // get the corresponding customTradeKey for this index
                String customTradeKey = data.getCustomTradeKey(customTradeIndex);
                customTradeIndex++;

                CustomTrade customTrade = customTrades.get(customTradeKey);

                // replace old trade with new trade if it is a custom trade
                if(customTrade != null) {

                    MerchantRecipe oldRecipe = oldRecipes.get(index);
                    MerchantRecipe newRecipe = customTrade.getRecipe();
                    // set the uses of the previous recipe so that players
                    // cant continually refresh uses by closing and opening
                    // the trade window
                    newRecipe.setUses(oldRecipe.getUses());

                    // add updated recipe
                    newRecipes.add(newRecipe);
                    
                }
                else  newRecipes.add(oldRecipes.get(index));

            }
            else {
                newRecipes.add(oldRecipes.get(index));
            }
        }

        merchant.setRecipes(newRecipes);

    }

    public List<CustomTrade> getValidTrades(Villager villager) throws VillagerNotMerchantException {

        List<CustomTrade> validTrades = new ArrayList<>();
        
        if(!(villager instanceof Merchant)) {
            throw new VillagerNotMerchantException();
        }

        List<CustomTrade> allTrades = customTrades.values().stream().toList();

        for(CustomTrade trade : allTrades) {

            List<Villager.Profession> professions = trade.getProfessions();
            List<Integer> levels = trade.getLevels();
            List<Villager.Type> villagerTypes = trade.getVillagerTypes();
            List<Biome> biomes = trade.getBiomes();

            // trader must have the right profession(s)
            if(
                professions.size() > 0 && 
                !professions.contains(villager.getProfession())
            ) {
                continue;
            }

            // trader must have the right level(s)
            if(
                levels.size() > 0 && 
                !levels.contains(villager.getVillagerLevel())
            ) {
                continue;
            }

            // trader must be of the right type(s)
            if(
                villagerTypes.size() > 0 && 
                !villagerTypes.contains(villager.getVillagerType())
            ) {
                continue;
            }

            // trader must be in the right biome(s)
            if(biomes.size() > 0) {
                Location location = villager.getLocation();
                Biome biome = location.getWorld().getBiome(location);
                if(!biomes.contains(biome)) continue;
            }
            
            // tader can't sell the same type of thing more than once
            if(
                !plugin.isDuplicateTradesAllowed() &&
                villagerHasCustomTrade(villager, trade)
            ) {
                continue;
            }
            
            // add to list of potential trades to return
            validTrades.add(trade);

        }

        return validTrades;

    }

    public boolean villagerHasCustomTrade(Villager villager, CustomTrade trade) {

        VillagerData data = villagerManager.getData(villager);
        return data.getCustomTradeKeys().contains(trade.getKey());

    }

    public CustomTrade chooseRandomTrade(List<CustomTrade> trades) {

        WeightedCollection<CustomTrade> weightedTrades = new WeightedCollection<>();
        trades.forEach(trade -> weightedTrades.add(100 * trade.getChance(), trade));
        return weightedTrades.next();

    }

    public boolean rerollCustomTrades(Villager villager) throws VillagerNotMerchantException {

        if(!(villager instanceof Merchant)) {
            throw new VillagerNotMerchantException();
        }

        Random rand = new Random();
        
        VillagerData data = villagerManager.getData(villager);
        data.clearCustomTradeKeys();
        List<VanillaTrade> vanillaTrades = data.getVanillaTrades();

        int villagerLevel = villager.getVillagerLevel();
        List<MerchantRecipe> newRecipes = new ArrayList<>();

        VanillaTrade vanillaTrade;
        for(int index = 0; index < vanillaTrades.size(); index++) {
            vanillaTrade = vanillaTrades.get(index);

            villager.setVillagerLevel(vanillaTrade.getVillagerLevel());
            List<CustomTrade> validCustomTrades = getValidTrades(villager);
            CustomTrade customTrade = chooseRandomTrade(validCustomTrades);

            // chance of not getting the trade (if vanilla trades aren't disabled)
            if(
                plugin.isVanillaTradesAllowed() && 
                rand.nextDouble() > customTrade.getChance()
            ) {
                // keep vanilla trade
                newRecipes.add(vanillaTrade.getRecipe());
            }
            else {
                // set custom trade
                newRecipes.add(customTrade.getRecipe());
                data.addCustomTradeKey(index, customTrade.getKey());
            }

            villager.setRecipes(newRecipes);

        }
        
        villager.setVillagerLevel(villagerLevel);

        return true;
    
    }
 
    public void restoreVanillaTrades(Villager villager) throws VillagerNotMerchantException {

        if(!(villager instanceof Merchant)) {
            throw new VillagerNotMerchantException();
        }
        
        VillagerData data = villagerManager.getData(villager);
        
        List<VanillaTrade> vanillaTrades = data.getVanillaTrades();
        List<MerchantRecipe> recipes = new ArrayList<>();
        for(VanillaTrade vanillaTrade : vanillaTrades) {
            recipes.add(vanillaTrade.getRecipe());
        }

        villager.setRecipes(recipes);
        data.clearCustomTradeKeys();

    }

}
