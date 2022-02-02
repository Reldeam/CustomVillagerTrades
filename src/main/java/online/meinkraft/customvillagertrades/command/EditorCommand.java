package online.meinkraft.customvillagertrades.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.gui.Editor;

public class EditorCommand extends PluginCommand {
    
    public EditorCommand(CustomVillagerTrades plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
            return false;
        }

        Player player = (Player) sender;

        Editor editor = new Editor();
        editor.open(player);

        return true;

    }
    
}