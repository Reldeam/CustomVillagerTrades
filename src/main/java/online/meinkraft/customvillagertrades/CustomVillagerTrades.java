package online.meinkraft.customvillagertrades;

import org.bukkit.plugin.java.JavaPlugin;
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

        getLogger().info("onEnable has been invoked!");

        // create config files if it doesn't exist
        saveDefaultConfig();
        createTradesConfig();

        // build custom trade list
        List<CustomTrade> customTrades = CustomTradeLoader.loadTrades(
            getTradesConfig(),
            getLogger()
        );
        villagerAcquireTradeListener.setCustomTrades(customTrades);

        // register listener
        getServer().getPluginManager().registerEvents(
            villagerAcquireTradeListener, 
            this
        );

    }
    
    @Override
    public void onDisable() {
        getLogger().info("onDisable has been invoked!");
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

    private FileConfiguration getTradesConfig() {
        return tradesConfig;
    }
    
}