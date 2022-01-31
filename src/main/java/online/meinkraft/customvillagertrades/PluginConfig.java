package online.meinkraft.customvillagertrades;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public interface PluginConfig {

    public FileConfiguration getConfig();
    public FileConfiguration getTradesConfig();

    public boolean isDuplicateTradesAllowed();
    public boolean isVanillaTradesAllowed();
    public Material getToolMaterial();

}
