package online.meinkraft.customvillagertrades.villager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class VillagerManager {

    private final JavaPlugin plugin;
    private Map<UUID, VillagerData> villagers = new HashMap<>();

    public VillagerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public VillagerData getData(Villager villager) {
        VillagerData data = villagers.get(villager.getUniqueId());
        if(data == null) data = addVillager(villager);
        return data;
    }

    public boolean has(Villager villager) {
        return villagers.containsKey(villager.getUniqueId());
    }

    public void remove(Villager villager) {
        villagers.remove(villager.getUniqueId());
    }

    public VillagerData addVillager(Villager villager) {
        VillagerData data = new VillagerData(villager);
        villagers.put(villager.getUniqueId(), data);
        return data;
    }

    public VillagerData addMerchant(Merchant merchant) {
        VillagerData data = new VillagerData(merchant);
        villagers.put(merchant.getTrader().getUniqueId(), data);
        return data;
    }

    public void load(String fileName) {

        BukkitObjectInputStream bukkitStream = null;
        villagers = new HashMap<>();

        try {

            File directory = new File(plugin.getDataFolder(), "data");
            if (!directory.exists()) directory.mkdir();

            File file = new File(plugin.getDataFolder(), "data/" + fileName);
            if(!file.exists()) {
                file.createNewFile();
                return;
            }

            FileInputStream stream = new FileInputStream(file);
            bukkitStream = new BukkitObjectInputStream(stream);

            try {
                Object serializedVillagerData = (Object) bukkitStream.readObject();
                if(serializedVillagerData instanceof List) {
                    List<?> list = (List<?>) serializedVillagerData;
                    for(Object item : list) {
                        if(item instanceof VillagerData) {
                            VillagerData villagerData = (VillagerData) item;
                            villagers.put(villagerData.getEntityId(), villagerData);
                        }
                    }
                }
            } catch (ClassNotFoundException exception) {
                plugin.getLogger().warning("Error deserializing villager data: " + exception.getMessage());
            }
            
        }
        catch(FileNotFoundException exception) {
            plugin.getLogger().warning("Error loading villager data: " + exception.getMessage());

        } catch (IOException exception) {
            plugin.getLogger().warning("Error loading villager data: " + exception.getMessage());
            exception.printStackTrace();
        }
        
        try {
            if(bukkitStream != null) bukkitStream.close();
        } catch (IOException exception) {
            plugin.getLogger().warning("Error closing input stream: " + exception.getMessage());
        }
        
    }

    public void save(String fileName) {

        BukkitObjectOutputStream bukkitStream = null;

        try {
            
            File file = new File(plugin.getDataFolder(), "data/" + fileName);
            if(!file.exists()) file.createNewFile();

            FileOutputStream stream = new FileOutputStream(file);
            bukkitStream = new BukkitObjectOutputStream(stream);

            bukkitStream.writeObject(villagers.values().stream().toList());

            bukkitStream.close();

        }
        catch(FileNotFoundException exception) {
            plugin.getLogger().info("Error saving villager data: " + exception.getMessage());
        }
        catch(IOException exception) {
            plugin.getLogger().info("Error saving villager data: " + exception.getMessage());       
        }

        try {
            if(bukkitStream != null) bukkitStream.close();
        } catch (IOException exception) {
            plugin.getLogger().warning("Error closing output stream: " + exception.getMessage());
        }

    }
    
}
