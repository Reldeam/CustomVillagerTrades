package online.meinkraft.customvillagertrades;

import org.bukkit.plugin.java.JavaPlugin;

import online.meinkraft.customvillagertrades.command.DisableCommand;
import online.meinkraft.customvillagertrades.command.EnableCommand;
import online.meinkraft.customvillagertrades.command.ReloadCommand;
import online.meinkraft.customvillagertrades.command.RerollCommand;
import online.meinkraft.customvillagertrades.listener.PlayerInteractEntityListener;
import online.meinkraft.customvillagertrades.listener.VillagerAcquireTradeListener;
import online.meinkraft.customvillagertrades.util.CustomTrade;
import online.meinkraft.customvillagertrades.util.CustomTradeLoader;
import online.meinkraft.customvillagertrades.util.WeightedCollection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Villager;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CustomVillagerTrades extends JavaPlugin {


    private File tradesConfigFile;
    private FileConfiguration tradesConfig;
    
    private boolean loaded = false;

    // configuration variables
    private boolean allowDuplicateTrades = false;
    private boolean disableVanillaTrades = false;
    private Material rerollMaterial;

    private List<CustomTrade> customTrades;

    private final VillagerAcquireTradeListener villagerAcquireTradeListener = new VillagerAcquireTradeListener(this);
    private final PlayerInteractEntityListener playerInteractEntityListener = new PlayerInteractEntityListener(this);

    @Override
    public void onEnable() {
        
        // ensure plugin doesn't get enabled more than once
        if(loaded) {
            getLogger().warning("Plugin already enabled");
            return;
        }

        // create config files if it doesn't exist
        createConfig();
        createTradesConfig();

        // set config values
        this.allowDuplicateTrades = getConfig().getBoolean("allowDuplicateTrades");
        this.disableVanillaTrades = getConfig().getBoolean("disableVanillaTrades");
        this.rerollMaterial = Material.getMaterial(getConfig().getString("rerollItem"));

        // build custom trade list
        customTrades = CustomTradeLoader.loadTrades(this);

        // register listeners
        getServer().getPluginManager().registerEvents(
            villagerAcquireTradeListener, 
            this
        );
        getServer().getPluginManager().registerEvents(
            playerInteractEntityListener,
            this
        );

        // register commands
        this.getCommand("enable").setExecutor(new EnableCommand(this));
        this.getCommand("disable").setExecutor(new DisableCommand(this));
        this.getCommand("reload").setExecutor(new ReloadCommand(this));
        this.getCommand("reroll").setExecutor(new RerollCommand(this));

        // ensure plugin doesn't get enabled more than once
        this.loaded = true;

    }
    
    @Override
    public void onDisable() {

        // ensure plugin doesn't get disabled more than once
        if(!loaded) {
            getLogger().warning("Plugin already disabled");
            return;
        }

        // unregister listeners
        HandlerList.unregisterAll(villagerAcquireTradeListener);
        HandlerList.unregisterAll(playerInteractEntityListener);

        // allow plugin to be enabled again
        this.loaded = false;

    }

    private void createConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void createTradesConfig() {

        tradesConfigFile = new File(getDataFolder(), "trades.yml");
        if (!tradesConfigFile.exists()) {
            tradesConfigFile.getParentFile().mkdirs();
            saveResource("trades.yml", false);
         }

        tradesConfig = new YamlConfiguration();
        try {
            tradesConfig.load(tradesConfigFile);
        } catch (IOException | InvalidConfigurationException exception) {
            getLogger().warning(
                "Failed to read trades.yml: " +
                exception.getMessage()
            );
        }
        
    }

    public FileConfiguration getTradesConfig() {
        return tradesConfig;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public boolean allowDuplicateTrades() {
        return this.allowDuplicateTrades;
    }

    public boolean disableVanillaTrades() {
        return this.disableVanillaTrades;
    }

    public Material getRerollMaterial() {
        return rerollMaterial;
    }

    public List<CustomTrade> getValidTrades(Merchant merchant) {

        List<CustomTrade> validTrades = new ArrayList<>();
        Villager villager = (Villager) merchant;

        for(CustomTrade trade : customTrades) {

            List<Villager.Profession> professions = trade.getProfessions();
            List<Integer> levels = trade.getLevels();
            List<Villager.Type> villagerTypes = trade.getVillagerTypes();
            List<Biome> biomes = trade.getBiomes();

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
            if(!allowDuplicateTrades()) {
                boolean resultExists = false;
                for(MerchantRecipe recipe : merchant.getRecipes()) {
                    List<ItemStack> ingredients = recipe.getIngredients();
                    if(
                        recipe.getResult().getType() == trade.getResult().getType() &&
                        ingredients.get(0).getType() == trade.getFirstIngredient().getType()
                    ) {
                        resultExists = true;
                        break;
                    }
                }
                if(resultExists) continue;
            }
            
            // add to list of potential trades to return
            validTrades.add(trade);

        }

        return validTrades;

    }

    public CustomTrade chooseRandomTrade(List<CustomTrade> trades) {

        WeightedCollection<CustomTrade> weightedTrades = new WeightedCollection<>();
        trades.forEach(trade -> weightedTrades.add(100 * trade.getChance(), trade));
        return weightedTrades.next();

    }

    public boolean rerollMerchant(Merchant merchant) {

        Random rand = new Random();
        Villager villager = (Villager) merchant;
    
        List<MerchantRecipe> oldRecipes = merchant.getRecipes();
    
        // check merchant has recipes to reroll
        if(oldRecipes == null || oldRecipes.size() == 0) return false;
    
        // clear the merchant's recipes in case no duplicate trades is set
        merchant.setRecipes(new ArrayList<>());
        List<MerchantRecipe> newRecipes = new ArrayList<>();
        
        Integer recipeIndex = 0;
    
        for(MerchantRecipe oldRecipe : oldRecipes) {

            Integer level = (int) Math.floor(recipeIndex++ / 2) + 1;
            if(level > 5) level = 5;
            villager.setVillagerLevel(level);

            List<CustomTrade> trades = getValidTrades(merchant);

            NamespacedKey key = new NamespacedKey(this, "CustomTrade");
            ItemMeta resultMeta = oldRecipe.getResult().getItemMeta();
            PersistentDataContainer container = resultMeta.getPersistentDataContainer();

            // don't reroll vanilla recipes
            Byte customTrade = container.get(key, PersistentDataType.BYTE);
            boolean isVanillaTrade = (
                customTrade == null || 
                Byte.compare(customTrade, (byte) 0) == 0
            );
            if(isVanillaTrade && !disableVanillaTrades()) {
                newRecipes.add(oldRecipe);
            }
            else if(trades.size() > 0) {
                CustomTrade trade = chooseRandomTrade(trades);
                if(
                    !disableVanillaTrades() && 
                    rand.nextDouble() > trade.getChance()
                ) {
                    newRecipes.add(oldRecipe);
                }
                else newRecipes.add(trade.getRecipe());
            }
    
            merchant.setRecipes(newRecipes);
    
        }

        return true;
    
    }
    
}

