package online.meinkraft.customvillagertrades;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Villager;

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

    public List<Villager.Profession> getVanillaDisabledProfessions();
    public boolean isVanillaTradesDisabledForProfession(Villager.Profession profession);

}
