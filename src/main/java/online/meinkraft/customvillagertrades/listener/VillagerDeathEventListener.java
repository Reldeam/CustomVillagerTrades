package online.meinkraft.customvillagertrades.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.villager.VillagerManager;

public class VillagerDeathEventListener implements Listener {

    private final CustomVillagerTrades plugin;

    public VillagerDeathEventListener(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {

        if(event.getEntityType() == EntityType.VILLAGER) {
            VillagerManager villagerManager = plugin.getVillagerManager();
            villagerManager.remove((Villager) event.getEntity());
        }
        
    }
    
}