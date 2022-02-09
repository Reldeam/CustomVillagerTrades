package online.meinkraft.customvillagertrades.villager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class VillagerManager {

    private final JavaPlugin plugin;
    //private Map<UUID, VillagerData> villagers = new HashMap<>();

    public VillagerManager(JavaPlugin plugin) {
        
        this.plugin = plugin;

        File directory = new File(plugin.getDataFolder(), "data");
        if (!directory.exists()) directory.mkdir();

        else {
            File file = new File(plugin.getDataFolder(), "data/" + "villagers.data");
            if(file.exists()) convertOldDataFormat(file);
        }

    }

    public boolean has(Villager villager) {
        String fileName = villager.getUniqueId().toString() + ".villager";
        File file = new File(plugin.getDataFolder(), "data/" + fileName);
        return file.exists();
    }

    public void remove(Villager villager) {
        String fileName = villager.getUniqueId().toString() + ".villager";
        File file = new File(plugin.getDataFolder(), "data/" + fileName);
        if(file.exists()) file.delete();
    }

    public VillagerData addVillager(Villager villager) {
        VillagerData villagerData = new VillagerData(villager);
        saveVillagerData(villagerData);
        return villagerData;
    }

    public VillagerData loadVillagerData(Villager villager) {

        BukkitObjectInputStream bukkitStream = null;
        VillagerData villagerData = null;
        String fileName = villager.getUniqueId().toString() + ".villager";

        try {

            File file = new File(plugin.getDataFolder(), "data/" + fileName);
            if(!file.exists()) return addVillager(villager);

            FileInputStream stream = new FileInputStream(file);
            bukkitStream = new BukkitObjectInputStream(stream);

            Object data = (Object) bukkitStream.readObject();
            if(data instanceof VillagerData) {
                villagerData = (VillagerData) data;
            }
            
        }
        catch(FileNotFoundException exception) {
            plugin.getLogger().warning("Error loading villager data: " + exception.getMessage());
        } 
        catch (IOException exception) {
            plugin.getLogger().warning("Error loading villager data: " + exception.getMessage());
            exception.printStackTrace();
        }
        catch (ClassNotFoundException exception) {
            plugin.getLogger().warning("Error deserializing villager data: " + exception.getMessage());
        }
        
        try {
            if(bukkitStream != null) bukkitStream.close();
        } catch (IOException exception) {
            plugin.getLogger().warning("Error closing input stream: " + exception.getMessage());
        }

        return villagerData;

    }

    public void saveVillagerData(VillagerData villagerData) {

        BukkitObjectOutputStream bukkitStream = null;
        String fileName = villagerData.getEntityId().toString() + ".villager";

        try {
            
            File file = new File(plugin.getDataFolder(), "data/" + fileName);
            if(!file.exists()) file.createNewFile();

            FileOutputStream stream = new FileOutputStream(file);
            bukkitStream = new BukkitObjectOutputStream(stream);

            bukkitStream.writeObject(villagerData);

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


    private void convertOldDataFormat(File file) {

        BukkitObjectInputStream bukkitStream = null;

        try {

            FileInputStream stream = new FileInputStream(file);
            bukkitStream = new BukkitObjectInputStream(stream);

            Object serializedVillagerData = (Object) bukkitStream.readObject();
            if(serializedVillagerData instanceof List) {
                List<?> list = (List<?>) serializedVillagerData;
                for(Object item : list) {
                    if(item instanceof VillagerData) {
                        VillagerData villagerData = (VillagerData) item;
                        saveVillagerData(villagerData);
                    }
                }
            }
            
        }
        catch(FileNotFoundException exception) {
            plugin.getLogger().warning("Error converting villager data: " + exception.getMessage());

        } 
        catch (IOException exception) {
            plugin.getLogger().warning("Error converting villager data: " + exception.getMessage());
            exception.printStackTrace();
        }
        catch (ClassNotFoundException exception) {
            plugin.getLogger().warning("Error deserializing villager data: " + exception.getMessage());
        }
        
        try {
            if(bukkitStream != null) bukkitStream.close();
        } catch (IOException exception) {
            plugin.getLogger().warning("Error closing input stream: " + exception.getMessage());
        }

        file.delete();
        
    }
    
}
