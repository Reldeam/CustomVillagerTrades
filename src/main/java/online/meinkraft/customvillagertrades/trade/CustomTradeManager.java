package online.meinkraft.customvillagertrades.trade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
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

    public List<CustomTrade> getCustomTrades() {
        return customTrades.values().stream().toList();
    }

    public CustomTrade getCustomTrade(String key) {
        return customTrades.get(key);
    }

    public void forceCustomTrade(Villager villager, CustomTrade customTrade) {

        VillagerData data = villagerManager.loadVillagerData(villager);
        if(data == null) data = villagerManager.addVillager(villager);

        data.addVanillaTrade(
            villager.getVillagerLevel(), 
            customTrade.getRecipe()
        );

        data.addCustomTradeKey(
            data.getVanillaTrades().size() - 1, 
            customTrade.getKey()
        );

        villagerManager.saveVillagerData(data);

        Merchant merchant = (Merchant) villager;
        List<MerchantRecipe> recipes = new ArrayList<>();
        recipes.addAll(merchant.getRecipes());
        recipes.add(customTrade.getRecipe());
        merchant.setRecipes(recipes);

    }

    public void removeCustomTrade(Villager villager, CustomTrade trade) {

        VillagerData data = villagerManager.loadVillagerData(villager);
        if(data == null) return;

        data.removeCustomTrade(trade);
        villagerManager.saveVillagerData(data);

        Merchant merchant = (Merchant) villager;
        List<MerchantRecipe> recipes = new ArrayList<>();

        for(int index = 0; index < data.getVanillaTrades().size(); index ++) {
            VanillaTrade vanillaTrade = data.getVanillaTrade(index);
            boolean isCustomTrade = data.isCustomTrade(index);
            String customTradeKey = data.getCustomTradeKey(index);
            CustomTrade customTrade = getCustomTrade(customTradeKey);
            if(isCustomTrade) recipes.add(customTrade.getRecipe());
            else recipes.add(vanillaTrade.getRecipe());
        }

        merchant.setRecipes(recipes);

    }

    public void refreshTrades(Villager villager, Player player) {

        Merchant merchant = (Merchant) villager;
        Set<String> tagSet = villager.getScoreboardTags(); // [Cruxien]: Potential addition to allow exceptions to the Disabled Vanilla Professions.

        VillagerManager villagerManager = plugin.getVillagerManager();
        VillagerData villagerData = villagerManager.loadVillagerData(villager);

        List<MerchantRecipe> oldRecipes = merchant.getRecipes();
        List<MerchantRecipe> newRecipes = new ArrayList<>();

        // preload valid trades (ignoring duplicates) to not have to do this
        // multiple times as it is expensive
        List<CustomTrade> validCustomTrades;
        try {
            if(plugin.forgetInvalidCustomTrades()) {
                validCustomTrades = getValidTrades(villager, villagerData, false, true);
            }
            else {
                validCustomTrades = new ArrayList<>();
            }
        } catch (VillagerNotMerchantException e) {
            validCustomTrades = new ArrayList<>();
        }
        
        int customTradeIndex = 0;
        for(int index = 0; index < oldRecipes.size(); index++) {
            if(villagerData.isCustomTrade(index)) {

                // get the corresponding customTradeKey for this index
                String customTradeKey = villagerData.getCustomTradeKey(customTradeIndex);
                customTradeIndex++;

                CustomTrade customTrade = customTrades.get(customTradeKey);

                // replace old trade with new trade if it is a custom trade
                if(customTrade != null) {

                    // the villager might forget the trade if it is not valid
                    // anymore
                    if(plugin.forgetInvalidCustomTrades()) {

                        if(!validCustomTrades.contains(customTrade)) {

                            if(player != null) player.sendMessage(String.format(
                                plugin.getMessage("villagerForgotTrade"),
                                customTrade.getKey()
                            ));

                            newRecipes.add(villagerData.getVanillaTrade(index).getRecipe());
                            villagerData.removeCustomTrade(customTrade);
                            continue;
                                
                        }

                    }

                    MerchantRecipe oldRecipe = oldRecipes.get(index);
                    MerchantRecipe newRecipe = customTrade.getRecipe();

                    // set the uses and special price of the previous recipe so 
                    // that players cant continually refresh uses and price by 
                    // closing and opening the trade window
                    newRecipe.setUses(oldRecipe.getUses());
                    newRecipe.setSpecialPrice(oldRecipe.getSpecialPrice());

                    // add updated recipe
                    newRecipes.add(newRecipe);
                    
                }
                else  newRecipes.add(oldRecipes.get(index));

            }
            else if(
                plugin.isVanillaTradesAllowed() &&
                (!plugin.isVanillaTradesDisabledForProfession(villager.getProfession()) || (plugin.isVanillaTradesDisabledForProfession(villager.getProfession()) && tagSet.contains("cvt_trade_override"))) // [Cruxien]: Potential addition to allow exceptions to the Disabled Vanilla Professions. 
            ) {
                newRecipes.add(oldRecipes.get(index));
            }
        }

        if (!tagSet.contains("cvt_trade_override")) { // [Cruxien]: Hopefully allows the individual villager to bypass this entirely and use the trades already in its data
        	merchant.setRecipes(newRecipes);
        	villagerManager.saveVillagerData(villagerData);
        }
        

    }

    public void refreshTrades(Villager villager) {
        refreshTrades(villager, null);
    }

    

    public List<CustomTrade> getValidTrades(
        Villager villager, 
        VillagerData villagerData,
        boolean strictLevel, 
        boolean ingoreDuplicates
    ) throws VillagerNotMerchantException {

        List<CustomTrade> validTrades = new ArrayList<>();
        
        if(!(villager instanceof Merchant)) {
            throw new VillagerNotMerchantException(plugin);
        }

        List<CustomTrade> allTrades = customTrades.values().stream().toList();

        for(CustomTrade trade : allTrades) {

            List<Villager.Profession> professions = trade.getProfessions();
            List<Integer> levels = trade.getLevels();
            List<Villager.Type> villagerTypes = trade.getVillagerTypes();
            List<Biome> biomes = trade.getBiomes();
            List<String> worlds = trade.getWorlds();

            // trader must have the right profession(s)
            if(
                professions.size() > 0 && 
                !professions.contains(villager.getProfession())
            ) {
                continue;
            }

            // trader must have the right level(s)
            // if strictLevels (default is true) then they must have the exact 
            // level requirement
            if(
                levels.size() > 0 && 
                strictLevel &&
                !levels.contains(villager.getVillagerLevel())
            ) {
                continue;
            }

            // if not strictLevel (default is true) then villager must have at 
            // least the level of the minimum level in levels
            if(
                levels.size() > 0 && 
                !strictLevel && 
                villager.getVillagerLevel() < Collections.min(levels)
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

            // trader must be in the right worlds(s)
            if(worlds.size() > 0) {
                World world = villager.getLocation().getWorld();
                if(!worlds.contains(world.getName())) continue;
            }
            
            // tader can't sell the same type of thing more than once
            if(
                !ingoreDuplicates &&
                !plugin.isDuplicateTradesAllowed() &&
                villagerHasCustomTrade(villagerData, trade)
            ) {
                continue;
            }
            
            // add to list of potential trades to return
            validTrades.add(trade);

        }

        return validTrades;

    }

    public List<CustomTrade> getValidTrades(
        Villager villager, 
        VillagerData villagerData,
        boolean ingoreDuplicates
    ) throws VillagerNotMerchantException {
        return getValidTrades(villager, villagerData, true, ingoreDuplicates);
    }

    public List<CustomTrade> getValidTrades(Villager villager, VillagerData villagerData) throws VillagerNotMerchantException {
        return getValidTrades(villager, villagerData, false);
    }

    public boolean villagerHasCustomTrade(VillagerData villagerData, CustomTrade trade) {
        return villagerData.getCustomTradeKeys().contains(trade.getKey());
    }

    public CustomTrade chooseRandomTrade(List<CustomTrade> trades) {

        if(trades.size() == 0) return null;
        WeightedCollection<CustomTrade> weightedTrades = new WeightedCollection<>();
        trades.forEach(trade -> {
            if(trade.getChance() == 0) return;
            weightedTrades.add(100 * trade.getChance(), trade);
        });
        if(weightedTrades.size() == 0) return null;
        return weightedTrades.next();

    }

    public boolean rerollCustomTrades(Villager villager) throws VillagerNotMerchantException {

        if(!(villager instanceof Merchant)) {
            throw new VillagerNotMerchantException(plugin);
        }

        Random rand = new Random();
        
        VillagerData villagerData = villagerManager.loadVillagerData(villager);
        villagerData.clearCustomTradeKeys();
        List<VanillaTrade> vanillaTrades = villagerData.getVanillaTrades();

        int villagerLevel = villager.getVillagerLevel();
        List<MerchantRecipe> newRecipes = new ArrayList<>();

        VanillaTrade vanillaTrade;
        for(int index = 0; index < vanillaTrades.size(); index++) {
            vanillaTrade = vanillaTrades.get(index);

            villager.setVillagerLevel(vanillaTrade.getVillagerLevel());
            List<CustomTrade> validCustomTrades = getValidTrades(villager, villagerData);

            // no trades available - continue
            if(validCustomTrades.size() == 0) {
                // keep vanilla trade
                newRecipes.add(vanillaTrade.getRecipe());
            }
            else {
                CustomTrade customTrade = chooseRandomTrade(validCustomTrades);

                // can happen if all custom trades have 0 chance
                if(customTrade == null) {
                    // keep vanilla trade
                    newRecipes.add(vanillaTrade.getRecipe());
                }
                // chance of not getting the trade (if vanilla trades aren't disabled)
                else if(
                    plugin.isVanillaTradesAllowed() && 
                    rand.nextDouble() > customTrade.getChance()
                ) {
                    // keep vanilla trade
                    newRecipes.add(vanillaTrade.getRecipe());
                }
                else {
                    // set custom trade
                    newRecipes.add(customTrade.getRecipe());
                    villagerData.addCustomTradeKey(index, customTrade.getKey());
                }
            }

            villager.setRecipes(newRecipes);

        }
        
        villager.setVillagerLevel(villagerLevel);
        villagerManager.saveVillagerData(villagerData);
        return true;
    
    }
 
    public void restoreVanillaTrades(Villager villager) throws VillagerNotMerchantException {

        if(!(villager instanceof Merchant)) {
            throw new VillagerNotMerchantException(plugin);
        }
        
        VillagerData data = villagerManager.loadVillagerData(villager);
        
        List<VanillaTrade> vanillaTrades = data.getVanillaTrades();
        List<MerchantRecipe> recipes = new ArrayList<>();
        for(VanillaTrade vanillaTrade : vanillaTrades) {
            recipes.add(vanillaTrade.getRecipe());
        }

        villager.setRecipes(recipes);
        data.clearCustomTradeKeys();
        villagerManager.saveVillagerData(data);

    }

}
