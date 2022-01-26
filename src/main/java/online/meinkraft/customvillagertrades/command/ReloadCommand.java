package online.meinkraft.customvillagertrades.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class ReloadCommand implements CommandExecutor {

    private final CustomVillagerTrades plugin;

    public ReloadCommand(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.plugin.onDisable();
        this.plugin.onEnable();
        return true;
    }
    
}
