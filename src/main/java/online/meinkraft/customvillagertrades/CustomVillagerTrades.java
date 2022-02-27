package online.meinkraft.customvillagertrades;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import online.meinkraft.customvillagertrades.command.DisableCommand;
import online.meinkraft.customvillagertrades.command.EditorCommand;
import online.meinkraft.customvillagertrades.command.EnableCommand;
import online.meinkraft.customvillagertrades.command.ReloadCommand;
import online.meinkraft.customvillagertrades.command.RerollCommand;
import online.meinkraft.customvillagertrades.command.RestoreCommand;
import online.meinkraft.customvillagertrades.exception.EconomyNotAvailableException;
import online.meinkraft.customvillagertrades.exception.VaultNotAvailableException;
import online.meinkraft.customvillagertrades.listener.InventoryClickListener;
import online.meinkraft.customvillagertrades.listener.InventoryCloseListener;
import online.meinkraft.customvillagertrades.listener.PlayerInteractEntityListener;
import online.meinkraft.customvillagertrades.listener.TradeSelectListener;
import online.meinkraft.customvillagertrades.listener.VillagerAcquireTradeListener;
import online.meinkraft.customvillagertrades.listener.VillagerCareerChangeListener;
import online.meinkraft.customvillagertrades.listener.VillagerDeathEventListener;
import online.meinkraft.customvillagertrades.trade.CustomTradeManager;
import online.meinkraft.customvillagertrades.trade.VanillaTrade;
import online.meinkraft.customvillagertrades.util.UpdateChecker;
import online.meinkraft.customvillagertrades.villager.VillagerData;
import online.meinkraft.customvillagertrades.villager.VillagerManager;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.HandlerList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomVillagerTrades extends JavaPlugin implements PluginConfig {

    private File tradesConfigFile;
    private FileConfiguration tradesConfig;

    private File languageFile;
    private FileConfiguration languageConfig;

    //private Map<UUID, VillagerData> villagers = new HashMap<>();
    
    private boolean loaded = false;

    // economy
    private Economy economy = null;

    // configuration variables
    private boolean isVanillaTradesAllowed = false;
    private boolean isDuplicateTradesAllowed = false;
    private boolean isEconomyEnabled = false;
    private boolean isCurrencyPhysical = false;
    private boolean forgetInvalidCustomTrades = false;
    private Material toolMaterial;
    private Material currencyMaterial;
    private String currencyPrefix;
    private String currencySuffix;
    private List<Villager.Profession> vanillaDisabledProfessions;

    private VillagerManager villagerManager;
    private CustomTradeManager customTradeManager;

    private final VillagerAcquireTradeListener villagerAcquireTradeListener = new VillagerAcquireTradeListener(this);
    private final PlayerInteractEntityListener playerInteractEntityListener = new PlayerInteractEntityListener(this);
    private final VillagerDeathEventListener villagerDeathEventListener = new VillagerDeathEventListener(this);
    private final VillagerCareerChangeListener villagerCareerChangeListener = new VillagerCareerChangeListener(this);
    private final InventoryClickListener inventoryClickListener = new InventoryClickListener(this);
    private final TradeSelectListener tradeSelectListener = new TradeSelectListener(this);
    private final InventoryCloseListener inventoryCloseListener = new InventoryCloseListener(this);

    public CustomVillagerTrades() { super(); }

    protected CustomVillagerTrades(
        JavaPluginLoader loader, 
        PluginDescriptionFile description, 
        File dataFolder, 
        File file
    )
    {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onLoad() {

        // create config file if it doesn't exist
        createConfig();

        // load the language config
        loadLanguageConfig();

        // register ConfigurationSerializable classes
        ConfigurationSerialization.registerClass(VillagerData.class);
        ConfigurationSerialization.registerClass(VanillaTrade.class);

        // check to see if there is an update
        UpdateChecker updateChecker = new UpdateChecker(this, "99540");


        switch(updateChecker.getUpdateType()) {
            case RELEASE:
            case SNAPSHOT:
            case EXPERIMENTAL:
                if(updateChecker.isUpdateAvailable()) {
                    getLogger().info(String.format(
                        ChatColor.MAGIC + getMessage("pluginUpdateAvailable"),
                        updateChecker.getUpdateType().name(),
                        updateChecker.getCurrentVersion(),
                        updateChecker.getLatestVersion()
                    ));
                    getLogger().info(String.format(
                        ChatColor.MAGIC + getMessage("getPluginUpdate"),
                        updateChecker.getResourceURL()
                    ));
                }
                else {
                    getLogger().info(String.format(
                        ChatColor.MAGIC + getMessage("unreleasedPluginVersion"),
                        updateChecker.getCurrentVersion()
                    ));
                }
                break;
            case CURRENT:
                getLogger().info(String.format(
                    getMessage("uptodatePluginVersion"),
                    updateChecker.getCurrentVersion()
                ));
                break;
            default:
                getLogger().warning(getMessage("unknownPluginUpdateStatus"));
        }

    }

    @Override
    public void onEnable() {

        // ensure plugin doesn't get enabled more than once
        if(loaded) {
            getLogger().warning(getMessage("pluginAlreadyEnabled"));
            return;
        }

        // set config values
        isDuplicateTradesAllowed = getConfig().getBoolean("allowDuplicateTrades");
        isVanillaTradesAllowed = !getConfig().getBoolean("disableVanillaTrades");
        isEconomyEnabled = getConfig().getBoolean("enableEconomy");
        isCurrencyPhysical = getConfig().getBoolean("enablePhysicalCurrency");
        forgetInvalidCustomTrades = getConfig().getBoolean("forgetInvalidCustomTrades");
        toolMaterial = Material.getMaterial(getConfig().getString("tool"));
        currencyMaterial = Material.getMaterial(getConfig().getString("currencyItem"));
        currencyPrefix = getConfig().getString("currencyPrefix");
        currencySuffix = getConfig().getString("currencySuffix");

        // add vanillaDisabledProfessions
        List<String> vanillaDisabledProfessionStrings = getConfig().getStringList("disableVanillaTradesForProfessions");
        vanillaDisabledProfessions = new ArrayList<>();
        for(String professionString : vanillaDisabledProfessionStrings) {
            Villager.Profession profession = Villager.Profession.valueOf(professionString);
            if(profession != null) vanillaDisabledProfessions.add(profession);
            else {
                getLogger().warning(String.format(
                    getMessage("invalidDisabledProfession"),
                    professionString
                ));
            }
        }
        
        // setup economy
        if(isEconomyEnabled()) {
            try {
                isEconomyEnabled = false;
                if (setupEconomy()) {
                    isEconomyEnabled = true;
                }
                else {
                    getLogger().warning(getMessage("noEconomyProvider"));
                }
            } catch (VaultNotAvailableException | EconomyNotAvailableException exception) {
                getLogger().warning(exception.getMessage());   
            }
        }

        // create the custom trade
        createTradesConfig();

        // load villager manager
        villagerManager = new VillagerManager(this);
        // villagerManager.load("villagers.data");

        // load custom trades
        customTradeManager = new CustomTradeManager(this);
        customTradeManager.load();
        

        // register listeners
        getServer().getPluginManager().registerEvents(
            villagerAcquireTradeListener, 
            this
        );
        getServer().getPluginManager().registerEvents(
            playerInteractEntityListener,
            this
        );
        getServer().getPluginManager().registerEvents(
            villagerDeathEventListener,
            this
        );
        getServer().getPluginManager().registerEvents(
            villagerCareerChangeListener,
            this
        );
        getServer().getPluginManager().registerEvents(
            inventoryClickListener,
            this
        );
        getServer().getPluginManager().registerEvents(
            tradeSelectListener,
            this
        );
        getServer().getPluginManager().registerEvents(
            inventoryCloseListener,
            this
        );

        // register commands
        this.getCommand("reroll").setExecutor(new RerollCommand(this));
        this.getCommand("restore").setExecutor(new RestoreCommand(this));
        this.getCommand("enable").setExecutor(new EnableCommand(this));
        this.getCommand("disable").setExecutor(new DisableCommand(this));
        this.getCommand("reload").setExecutor(new ReloadCommand(this));
        this.getCommand("editor").setExecutor(new EditorCommand(this));

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
        HandlerList.unregisterAll(villagerDeathEventListener);
        HandlerList.unregisterAll(villagerCareerChangeListener);
        HandlerList.unregisterAll(inventoryClickListener);
        HandlerList.unregisterAll(tradeSelectListener);
        HandlerList.unregisterAll(inventoryCloseListener);
        
        // save data files
        // villagerManager.save("villagers.data");

        // allow plugin to be enabled again
        this.loaded = false;

    }

    @Override
    public FileConfiguration getTradesConfig() {
        return tradesConfig;
    }

    @Override
    public boolean isDuplicateTradesAllowed() {
        return isDuplicateTradesAllowed;
    }

    @Override
    public boolean isVanillaTradesAllowed() {
        return isVanillaTradesAllowed;
    }

    @Override
    public Material getToolMaterial() {
        return toolMaterial;
    }

    @Override
    public boolean isEconomyEnabled() {
        return isEconomyEnabled;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public CustomTradeManager getCustomTradeManager() {
        return this.customTradeManager;
    }
    
    public VillagerManager getVillagerManager() {
        return villagerManager;
    }

    public Economy getEconomy() {
        return economy;
    }

    public String getMessage(String key) {
        String message = languageConfig.getString(key);
        if(message == null) return "";
        return languageConfig.getString(key);
    }

    private void createConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            saveDefaultConfig();
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    protected File getLanguageFolder() {
        File folder = new File(getDataFolder(), "lang");
        if(!folder.isDirectory()) {
            folder.mkdir();
        }
        return folder;
    }

    private void loadLanguageConfig() {

        languageFile = new File(
            getLanguageFolder(), 
            getConfig().getString("language") + ".lang"
        );

        if (!languageFile.exists()) {
            saveResource("lang/en_US.lang", false);
            languageFile = new File(getLanguageFolder(), "en_US.lang");
        }

        languageConfig = new YamlConfiguration();

        try {
            languageConfig.load(languageFile);
        } catch (IOException | InvalidConfigurationException exception) {
            getLogger().warning(
                "Failed to read language file: " +
                exception.getMessage()
            );
        }

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
            getLogger().warning(String.format(
                getMessage("failedToReadTrades"),
                exception.getMessage()
            ));
        }
        
    }

    public void saveTradesConfig() {
        try {
            tradesConfig.save(tradesConfigFile);
        } catch (IOException exception) {
            getLogger().warning(String.format(
                getMessage("failedToSaveTrades"),
                exception.getMessage()
            ));
        }
    }

    private boolean setupEconomy() throws VaultNotAvailableException, EconomyNotAvailableException {

        // get vault plugin
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            throw new VaultNotAvailableException(this);
        }
        
        // get economy plugin
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) throw new EconomyNotAvailableException(this);

        economy = rsp.getProvider();
        return economy != null;

    }

    @Override
    public Material getCurrencyMaterial() {
        return this.currencyMaterial;
    }

    @Override
    public String getCurrencyPrefix() {
        return this.currencyPrefix;
    }


    @Override
    public String getCurrencySuffix() {
        return this.currencySuffix;
    }

    @Override
    public boolean isCurrencyPhysical() {
        return isCurrencyPhysical;
    }

    @Override
    public List<Profession> getVanillaDisabledProfessions() {
        return vanillaDisabledProfessions;
    }

    @Override
    public boolean forgetInvalidCustomTrades() {
        return forgetInvalidCustomTrades;
    }

    @Override
    public boolean isVanillaTradesDisabledForProfession(Profession profession) {
        return vanillaDisabledProfessions.contains(profession);
    }


    
    
}

