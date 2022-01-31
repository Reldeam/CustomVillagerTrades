package online.meinkraft.customvillagertrades.trade;

import java.util.List;

import org.bukkit.block.Biome;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomTrade {

    private final String key;
    private final ItemStack result;
    private final ItemStack firstIngredient;
    private final ItemStack secondIngredient;
    private final Integer maxUses;
    private final Integer villagerExperience;
    private final Boolean giveExperienceToPlayer;

    private final Double chance;
    private final List<Villager.Profession> professions;
    private final List<Integer> levels;
    private final List<Villager.Type> villagerTypes;
    private final List<Biome> biomes;
    
    private final MerchantRecipe recipe;
    
    public CustomTrade(

        JavaPlugin plugin,

        String key,

        ItemStack result,
        ItemStack firstIngredient,
        ItemStack secondIngredient,
        Integer maxUses,
        Integer villagerExperience,
        Boolean giveExperienceToPlayer,

        Double chance,
        List<Villager.Profession> professions,
        List<Integer> levels,
        List<Villager.Type> villagerTypes,
        List<Biome> biomes

    ) {

        this.key = key;

        this.result = result;
        this.firstIngredient = firstIngredient;
        this.secondIngredient = secondIngredient;
        this.maxUses = maxUses;
        this.villagerExperience = villagerExperience;
        this.giveExperienceToPlayer = giveExperienceToPlayer;

        this.chance = chance;
        this.professions = professions;
        this.levels = levels;
        this.villagerTypes = villagerTypes;
        this.biomes = biomes;
        
        // create recipe
        recipe = new MerchantRecipe(
            result,
            maxUses
        );

        recipe.addIngredient(firstIngredient);
        if(secondIngredient != null) recipe.addIngredient(secondIngredient);
        recipe.setExperienceReward(giveExperienceToPlayer);
        recipe.setVillagerExperience(villagerExperience);

    }

    public String getKey() {
        return key;
    }

    public ItemStack getFirstIngredient() {
        return firstIngredient;
    }

    public ItemStack getSecondIngredient() {
        return secondIngredient;
    }

    public ItemStack getResult() {
        return result;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public Double getChance() {
        return chance;
    }

    public List<Villager.Profession> getProfessions() {
        return professions;
    }

    public List<Integer> getLevels() {
        return levels;
    }

    public Boolean giveExperienceToPlayer() {
        return giveExperienceToPlayer;
    }

    public Integer getVillagerExperience() {
        return villagerExperience;
    }

    public MerchantRecipe getRecipe() {
        return recipe;
    }

    public List<Villager.Type> getVillagerTypes() {
        return villagerTypes;
    }

    public List<Biome> getBiomes() {
        return biomes;
    }

}