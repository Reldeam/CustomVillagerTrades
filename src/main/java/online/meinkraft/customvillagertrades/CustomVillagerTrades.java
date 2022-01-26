package online.meinkraft.customvillagertrades;

import org.bukkit.plugin.java.JavaPlugin;

import online.meinkraft.customvillagertrades.command.DisableCommand;
import online.meinkraft.customvillagertrades.command.ReloadCommand;
import online.meinkraft.customvillagertrades.listener.VillagerAcquireTradeListener;

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

    private final VillagerAcquireTradeListener villagerAcquireTradeListener = new VillagerAcquireTradeListener();

    @Override
    public void onEnable() {

        // create config files if it doesn't exist
        saveDefaultConfig();
        createTradesConfig();

        // build custom trade list
        List<CustomTrade> customTrades = CustomTradeLoader.loadTrades(this);
        villagerAcquireTradeListener.setCustomTrades(customTrades);

        // register listeners
        getServer().getPluginManager().registerEvents(
            villagerAcquireTradeListener, 
            this
        );

        // register commands
        this.getCommand("reload").setExecutor(new ReloadCommand(this));
        this.getCommand("disable").setExecutor(new DisableCommand(this));

    }
    
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(villagerAcquireTradeListener);
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
    
}