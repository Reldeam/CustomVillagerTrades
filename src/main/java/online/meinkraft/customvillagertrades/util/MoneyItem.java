package online.meinkraft.customvillagertrades.util;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.exception.EconomyNotEnabledException;

public class MoneyItem {
    
    public static ItemStack create(CustomVillagerTrades plugin, double amount) throws EconomyNotEnabledException {

        if(!plugin.isEconomyEnabled()) throw new EconomyNotEnabledException();

        ItemStack itemStack = new ItemStack(plugin.getCurrencyMaterial());

        // Make item look shiny
        itemStack.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);

        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();

        data.set(new NamespacedKey(plugin, "money"), PersistentDataType.DOUBLE, amount);
        
        itemMeta.setDisplayName(
            plugin.getCurrencyPrefix() +
            String.format("%,.2f", amount) +
            plugin.getCurrencySuffix()
        );

        // Hide the enchantment details
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }
}
