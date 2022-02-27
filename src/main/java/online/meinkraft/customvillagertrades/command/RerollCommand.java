package online.meinkraft.customvillagertrades.command;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;

import online.meinkraft.customvillagertrades.trade.CustomTradeManager;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.exception.VillagerNotMerchantException;

public class RerollCommand extends PluginCommand {
    
    public RerollCommand(CustomVillagerTrades plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                ChatColor.RED + 
                plugin.getMessage("playerOnlyCommand")
            );
            return false;
        }

        Player player = sender.getServer().getPlayer(sender.getName());
        World world = player.getWorld();

        List<Entity> villagers;

        String radiusArgument;

        if(args.length == 0) {
            sender.sendMessage(
                ChatColor.RED + 
                plugin.getMessage("noRadiusProvided")
            );
             return false;
        }

        radiusArgument = args[0].toLowerCase();
        
        if(radiusArgument.equals("all")) {
            villagers = world.getEntities().stream().filter(
                entity -> entity instanceof Merchant
            ).filter(
                entity -> entity instanceof Villager
            ).collect(Collectors.toList());
        }
        else {
            try {
                double radius = Double.parseDouble(radiusArgument);
                villagers = world.getNearbyEntities(
                    player.getLocation(),
                    radius,
                    radius,
                    radius,
                    entity -> (entity instanceof Merchant && entity instanceof Villager)
                ).stream().toList();
            }
            catch(NumberFormatException expection) {
                sender.sendMessage(
                    ChatColor.RED + 
                    plugin.getMessage("invalidRadiusProvided")
                );
                return false;
            }
            
        }

        CustomTradeManager tradeManager = plugin.getCustomTradeManager();

        Integer numRerolled = 0;
        for(Entity entity : villagers) {
            try {
                Villager villager = (Villager) entity;
                if(tradeManager.rerollCustomTrades(villager)) numRerolled++;
            } catch (VillagerNotMerchantException e) {
                plugin.getLogger().warning(String.format(
                    plugin.getMessage("invalidRerollTarget"),
                    sender.getName(),
                    entity.toString()
                ));
            }
        }

        if(!plugin.isVanillaTradesAllowed()) {
            sender.sendMessage(String.format(
                ChatColor.GREEN + plugin.getMessage("rerolledAllTrades"),
                numRerolled,
                world.getName()
            ));
        }
        else {
            sender.sendMessage(String.format(
                ChatColor.GREEN + plugin.getMessage("rerolledCustomTrades"),
                numRerolled,
                world.getName()
            ));
        }
        
        return true;

    }
    
}
