package online.meinkraft.customvillagertrades.command;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Merchant;

import online.meinkraft.customvillagertrades.trade.CustomTradeManager;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class RestoreCommand implements CommandExecutor {

    private final CustomVillagerTrades plugin;

    public RestoreCommand(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
            return false;
        }

        Player player = sender.getServer().getPlayer(sender.getName());
        World world = player.getWorld();

        List<Entity> merchants;

        String radiusArgument;

        if(args.length == 0) {
            sender.sendMessage(
                ChatColor.RED + 
                "No radius provided: It must be <all> or <number>"
            );
             return false;
        }

        radiusArgument = args[0].toLowerCase();
        
        if(radiusArgument.equals("all")) {
            merchants = world.getEntities().stream().filter(
                entity -> entity instanceof Merchant
            ).collect(Collectors.toList());
        }
        else {
            try {
                double radius = Double.parseDouble(radiusArgument);
                merchants = world.getNearbyEntities(
                    player.getLocation(),
                    radius,
                    radius,
                    radius,
                    entity -> (entity instanceof Merchant)
                ).stream().toList();
            }
            catch(NumberFormatException expection) {
                sender.sendMessage(
                    ChatColor.RED + 
                    "Invalid radius argument: It must be <all> or <number>"
                );
                return false;
            }
            
        }

        CustomTradeManager tradeManager = plugin.getCustomTradeManager();

        for(Entity entity : merchants) {
            Merchant merchant = (Merchant) entity;
            tradeManager.restoreMerchant(merchant);
        }

        sender.sendMessage(
            ChatColor.GREEN + 
            "Restored all Vanilla Minecraft trades for " + merchants.size() + 
            " merchants on " + world.getName()
        );
        
        return true;

    }
    
}
