package online.meinkraft.customvillagertrades.listener;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import online.meinkraft.customvillagertrades.trade.CustomTrade;
import online.meinkraft.customvillagertrades.trade.CustomTradeManager;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.exception.VillagerNotMerchantException;

public class PlayerInteractEntityListener implements Listener {

    private final CustomVillagerTrades plugin;

    public PlayerInteractEntityListener(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        CustomTradeManager tradeManager = plugin.getCustomTradeManager();
        Entity entity = event.getRightClicked();

        // check interacted entity is a merchant villager
        if(
            entity.getType() != EntityType.VILLAGER ||
            !(entity instanceof Merchant)
        ) return;

        Villager villager = (Villager) entity;

        // refresh the trades based on their keys
        tradeManager.refreshTrades(villager, event.getPlayer());

        ItemStack toolUsed = event.getPlayer().getInventory().getItemInMainHand();

        PersistentDataContainer data = null;
        String blueprint = null;

        ItemMeta meta = toolUsed.getItemMeta();
        if(meta != null) data = meta.getPersistentDataContainer();
        if(data != null) blueprint = data.get(
            NamespacedKey.fromString("blueprint", plugin), 
            PersistentDataType.STRING
        );
        
        // give blueprint to villager (only if they have a profession)
        if(
            blueprint != null &&
            !villager.getProfession().equals(Villager.Profession.NONE) &&
            !villager.getProfession().equals(Villager.Profession.NITWIT)
        ) {

            CustomTrade trade = tradeManager.getCustomTrade(blueprint);

            if(trade != null) {
                // try to remove custom trade if sneaking
                if(event.getPlayer().isSneaking()) {
                    tradeManager.removeCustomTrade(villager, trade);
                }
                // try to add custom trade
                else {
                    tradeManager.forceCustomTrade(villager, trade); 
                }
                
            }
            
        }
        else if(toolUsed.getType() == plugin.getToolMaterial()) {
            try {
                usePluginTool(event, villager);
            } catch (VillagerNotMerchantException e) {
                return;
            }
        }
   
    }

    private void usePluginTool(PlayerInteractEntityEvent event, Villager villager) throws VillagerNotMerchantException {
        
        CustomTradeManager tradeManager = plugin.getCustomTradeManager();
        
        if(event.getPlayer().hasPermission("customvillagertrades.item.restore") && 
            event.getPlayer().isSneaking()
        ) {
            tradeManager.restoreVanillaTrades((Villager) villager);
            event.getPlayer().sendMessage(
                ChatColor.GREEN + plugin.getMessage("restoredVanillaTradesForVillager")
            );
        }
        else if(
            event.getPlayer().hasPermission("customvillagertrades.item.reroll") && 
            !event.getPlayer().isSneaking()
        ) {
            if(tradeManager.rerollCustomTrades(villager)) {
                if(!plugin.isVanillaTradesAllowed()) {
                    event.getPlayer().sendMessage(
                        ChatColor.GREEN + plugin.getMessage("rerolledAllTradesForVillager")
                    );
                }
                else {
                    event.getPlayer().sendMessage(
                        ChatColor.GREEN + plugin.getMessage("rerolledCustomTradesForVillager")
                    );
                }
            }
        }
    }

}