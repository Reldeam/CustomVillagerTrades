package online.meinkraft.customvillagertrades.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.villager.VillagerManager;

public class VillagerCareerChangeListener implements Listener {

    private final CustomVillagerTrades plugin;

    public VillagerCareerChangeListener(CustomVillagerTrades plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVillagerCareerChangeEvent(VillagerCareerChangeEvent event) {

        if(event.getEntityType() == EntityType.VILLAGER) {
            VillagerManager villagerManager = plugin.getVillagerManager();
            villagerManager.remove((Villager) event.getEntity());
        }
        
    }
    
}