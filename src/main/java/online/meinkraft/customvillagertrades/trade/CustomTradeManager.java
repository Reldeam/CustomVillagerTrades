package online.meinkraft.customvillagertrades.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.PluginConfig;
import online.meinkraft.customvillagertrades.util.WeightedCollection;
import online.meinkraft.customvillagertrades.villager.VillagerData;
import online.meinkraft.customvillagertrades.villager.VillagerManager;

public class CustomTradeManager {

    private Map<String, CustomTrade> customTrades;
    //private final JavaPlugin plugin;
    private final PluginConfig config;
    private final VillagerManager villagerManager;

    public CustomTradeManager(
        JavaPlugin plugin, 
        PluginConfig config,
        VillagerManager villagerManager
    ) {
        //this.plugin = plugin;
        this.config = config;
        this.villagerManager = villagerManager;
        customTrades = CustomTradeLoader.loadTrades(plugin, config);
    }

    public CustomTradeManager(CustomVillagerTrades plugin) {
        this(
            (JavaPlugin) plugin, 
            (PluginConfig) plugin,
            plugin.getVillagerManager()
        );
    }

    public List<CustomTrade> getValidTrades(Merchant merchant) {

        List<CustomTrade> validTrades = new ArrayList<>();
        Villager villager = (Villager) merchant;
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
                !config.isDuplicateTradesAllowed() &&
                merchantHasCustomTrade(merchant, trade)
            ) {
                continue;
            }
            
            // add to list of potential trades to return
            validTrades.add(trade);

        }

        return validTrades;

    }

    public boolean merchantHasCustomTrade(Merchant merchant, CustomTrade trade) {

        Villager villager = (Villager) merchant;
        VillagerData data = villagerManager.get(villager);
        return data.getCustomTradeKeys().contains(trade.getKey());

    }

    public CustomTrade chooseRandomTrade(List<CustomTrade> trades) {

        WeightedCollection<CustomTrade> weightedTrades = new WeightedCollection<>();
        trades.forEach(trade -> weightedTrades.add(100 * trade.getChance(), trade));
        return weightedTrades.next();

    }

    public boolean rerollMerchant(Merchant merchant) {

        Random rand = new Random();
        Villager villager = (Villager) merchant;

        VillagerData data = villagerManager.get(villager);
        data.clearCustomTradeKeys();
        List<VanillaTrade> vanillaTrades = data.getVanillaTrades();

        int villagerLevel = villager.getVillagerLevel();
        List<MerchantRecipe> newRecipes = new ArrayList<>();

        for(VanillaTrade vanillaTrade : vanillaTrades) {

            villager.setVillagerLevel(vanillaTrade.getVillagerLevel());
            List<CustomTrade> validCustomTrades = getValidTrades(merchant);
            CustomTrade customTrade = chooseRandomTrade(validCustomTrades);

            // chance of not getting the trade (if vanilla trades aren't disabled)
            if(
                config.isVanillaTradesAllowed() && 
                rand.nextDouble() > customTrade.getChance()
            ) {
                // keep vanilla trade
                newRecipes.add(vanillaTrade.getRecipe());
            }
            else {
                // set custom trade
                newRecipes.add(customTrade.getRecipe());
                data.addCustomTradeKey(customTrade.getKey());
            }

            villager.setRecipes(newRecipes);

        }
        
        villager.setVillagerLevel(villagerLevel);

        return true;
    
    }
 
    public void restoreMerchant(Merchant merchant) {

        Villager villager = (Villager) merchant;
        VillagerData data = villagerManager.get(villager);
        
        List<VanillaTrade> vanillaTrades = data.getVanillaTrades();
        List<MerchantRecipe> recipes = new ArrayList<>();
        for(VanillaTrade vanillaTrade : vanillaTrades) {
            recipes.add(vanillaTrade.getRecipe());
        }

        villager.setRecipes(recipes);
        data.clearCustomTradeKeys();

    }

}
