package online.meinkraft.customvillagertrades.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class EnableCommand extends PluginCommand {
    
    public EnableCommand(CustomVillagerTrades plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.plugin.onEnable();
        sender.sendMessage(
            ChatColor.GREEN + 
            plugin.getMessage("pluginEnabled")
        );

        return true;
    }
    
}