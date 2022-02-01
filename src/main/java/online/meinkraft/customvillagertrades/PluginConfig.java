package online.meinkraft.customvillagertrades;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public interface PluginConfig {

    public FileConfiguration getConfig();
    public FileConfiguration getTradesConfig();

    public boolean isDuplicateTradesAllowed();
    public boolean isVanillaTradesAllowed();
    public boolean isEconomyEnabled();
    public boolean isCurrencyPhysical();
    public Material getToolMaterial();

    public Material getCurrencyMaterial();
    public String getCurrencyPrefix();
    public String getCurrencySuffix();

}
