package online.meinkraft.customvillagertrades.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class EnableCommand implements CommandExecutor {

    private final CustomVillagerTrades plugin;

    public EnableCommand(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.plugin.onEnable();
        sender.sendMessage(ChatColor.GREEN + "Plugin enabled");

        return true;
    }
    
}