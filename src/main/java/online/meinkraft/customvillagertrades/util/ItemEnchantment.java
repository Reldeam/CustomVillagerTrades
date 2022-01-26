package online.meinkraft.customvillagertrades.util;

import org.bukkit.enchantments.Enchantment;

public class ItemEnchantment {

    private final Enchantment enchantment;
    private final Integer level;
    private final Boolean ignoreLevelRestriction;

    public ItemEnchantment(
        Enchantment enchantment,
        Integer level,
        Boolean ignoreLevelRestriction
    ) {
        this.enchantment = enchantment;
        this.level = level;
        this.ignoreLevelRestriction = ignoreLevelRestriction;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public Integer getLevel() {
        return level;
    }

    public Boolean ignoreLevelRestriction() {
        return ignoreLevelRestriction;
    }

}