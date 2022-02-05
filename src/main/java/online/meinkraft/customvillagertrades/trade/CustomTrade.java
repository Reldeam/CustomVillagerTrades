package online.meinkraft.customvillagertrades.trade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Biome;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class CustomTrade implements Cloneable {

    private String key;
    private ItemStack result;
    private ItemStack firstIngredient;
    private ItemStack secondIngredient;
    private Integer maxUses;
    private Double priceMultiplier;
    private Integer villagerExperience;
    private Boolean giveExperienceToPlayer;

    private Double chance;
    private List<Villager.Profession> professions;
    private List<Integer> levels;
    private List<Villager.Type> villagerTypes;
    private List<Biome> biomes;
    
    private MerchantRecipe recipe;
    
    public CustomTrade(

        String key,

        ItemStack result,
        ItemStack firstIngredient,
        ItemStack secondIngredient,
        Integer maxUses,
        Double priceMultiplier,
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

        if(maxUses == null) maxUses = 1;
        this.maxUses = maxUses;

        if(priceMultiplier == null) priceMultiplier = (double) 0;
        this.priceMultiplier = priceMultiplier;

        if(villagerExperience == null) villagerExperience = 0;
        this.villagerExperience = villagerExperience;

        if(giveExperienceToPlayer == null) giveExperienceToPlayer = false;
        this.giveExperienceToPlayer = giveExperienceToPlayer;

        if(chance == null) chance = (double) 0;
        this.chance = chance;

        if(professions == null) professions = new ArrayList<>();
        this.professions = professions;

        if(levels == null) levels = new ArrayList<>();
        this.levels = levels;

        if(villagerTypes == null) villagerTypes = new ArrayList<>();
        this.villagerTypes = villagerTypes;

        if(biomes == null) biomes = new ArrayList<>();
        this.biomes = biomes;
        
        // create recipe
        recipe = new MerchantRecipe(
            result,
            maxUses
        );

        if(firstIngredient != null) recipe.addIngredient(firstIngredient);
        if(secondIngredient != null) recipe.addIngredient(secondIngredient);
        
        recipe.setPriceMultiplier(priceMultiplier.floatValue());

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

    public Double getPriceMultiplier() {
        return priceMultiplier;
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

    public CustomTrade clone() {

        ItemStack result = null;
        ItemStack firstIngredient = null;
        ItemStack secondIngredient = null;

        if(this.result != null) {
            result = this.result.clone();
        }

        if(this.firstIngredient != null) {
            firstIngredient = this.firstIngredient.clone();
        }

        if(this.secondIngredient != null) {
            secondIngredient = this.secondIngredient.clone();
        }

        List<Villager.Profession> professions = new ArrayList<>();
        if(this.professions != null) professions.addAll(this.professions);

        List<Integer> levels = new ArrayList<>();
        if(this.levels != null) levels.addAll(this.levels);

        List<Villager.Type> villagerTypes = new ArrayList<>();
        if(this.villagerTypes != null) villagerTypes.addAll(this.villagerTypes);

        List<Biome> biomes = new ArrayList<>();
        if(this.biomes != null) biomes.addAll(this.biomes);

        return new CustomTrade(

            getKey(), 
            result, 
            firstIngredient, 
            secondIngredient, 
            maxUses, 
            priceMultiplier, 
            villagerExperience, 
            giveExperienceToPlayer, 
            chance, 
            professions, 
            levels, 
            villagerTypes, 
            biomes

        );

    }

    public void setFirstIngredient(ItemStack itemStack) {
        firstIngredient = itemStack;
    }

    public void setSecondIngredient(ItemStack itemStack) {
        secondIngredient = itemStack;
    }

    public void setResult(ItemStack itemStack) {
        result = itemStack;
    }

    public void setMaxUses(Integer maxUses) {
        if(maxUses < 0) throw new IllegalArgumentException("value must be postitive");
        this.maxUses = maxUses;
    }

    public void setPriceMultiplier(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public void setVillagerExperience(Integer villagerExperience) {
        if(villagerExperience < 0) throw new IllegalArgumentException("value must be postitive");
        this.villagerExperience = villagerExperience;
    }

    public void giveExperienceToPlayer(boolean giveExperienceToPlayer) {
        this.giveExperienceToPlayer = giveExperienceToPlayer;
    }

    public void setChance(Double chance) {
        if(chance < 0 | chance > 1) throw new IllegalArgumentException("chance must be between 0 - 1 (inclusive)");
        this.chance = chance;
    }

    public void addProfession(Villager.Profession profession) {
        if(professions.contains(profession)) {
            throw new IllegalArgumentException("profession already added");
        }
        professions.add(profession);
    }

    public void removeProfession(Villager.Profession profession) {
        professions.remove(profession);
    }

    public void addLevel(Integer level) {
        if(level < 1 || level > 5) {
            throw new IllegalArgumentException("level must be between 1 - 5 (inclusive)");
        }
        if(levels.contains(level)) {
            throw new IllegalArgumentException("profession already added");
        }
        levels.add(level);
    }

    public void removeLevel(Integer level) {
        levels.remove(level);
    }

    public void addVillagerType(Villager.Type type) {
        if(villagerTypes.contains(type)) {
            throw new IllegalArgumentException("villager type already added");
        }
        villagerTypes.add(type);
    }

    public void removeVillagerType(Villager.Type type) {
        villagerTypes.remove(type);
    }

    public void addBiome(Biome biome) {
        if(biomes.contains(biome)) {
            throw new IllegalArgumentException("biome already added");
        }
        biomes.add(biome);
    }

    public void removeBiome(Biome biome) {
        biomes.remove(biome);
    }

}