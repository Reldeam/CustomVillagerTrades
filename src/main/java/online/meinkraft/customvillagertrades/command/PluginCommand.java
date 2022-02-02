package online.meinkraft.customvillagertrades.command;

import org.bukkit.command.CommandExecutor;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public abstract class PluginCommand implements CommandExecutor {

    protected final CustomVillagerTrades plugin;

    public PluginCommand(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    public CustomVillagerTrades getPlugin() {
        return plugin;
    }
    
}
