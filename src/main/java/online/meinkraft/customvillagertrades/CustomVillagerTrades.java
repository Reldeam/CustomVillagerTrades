package online.meinkraft.customvillagertrades;

import org.bukkit.plugin.java.JavaPlugin;

import online.meinkraft.customvillagertrades.command.DisableCommand;
import online.meinkraft.customvillagertrades.command.EnableCommand;
import online.meinkraft.customvillagertrades.command.ReloadCommand;
import online.meinkraft.customvillagertrades.listener.VillagerAcquireTradeListener;
import online.meinkraft.customvillagertrades.util.CustomTrade;
import online.meinkraft.customvillagertrades.util.CustomTradeLoader;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class CustomVillagerTrades extends JavaPlugin {


    private File tradesConfigFile;
    private FileConfiguration tradesConfig;
    
    private boolean loaded = false;
    private boolean allowDuplicateTrades = false;

    private final VillagerAcquireTradeListener villagerAcquireTradeListener = new VillagerAcquireTradeListener(this);

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

        // build custom trade list
        List<CustomTrade> customTrades = CustomTradeLoader.loadTrades(this);
        villagerAcquireTradeListener.setCustomTrades(customTrades);

        // register listeners
        getServer().getPluginManager().registerEvents(
            villagerAcquireTradeListener, 
            this
        );

        // register commands
        this.getCommand("enable").setExecutor(new EnableCommand(this));
        this.getCommand("disable").setExecutor(new DisableCommand(this));
        this.getCommand("reload").setExecutor(new ReloadCommand(this));

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

        HandlerList.unregisterAll(villagerAcquireTradeListener);

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
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
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
    
}