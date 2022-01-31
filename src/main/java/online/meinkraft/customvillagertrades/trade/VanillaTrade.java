package online.meinkraft.customvillagertrades.trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class VanillaTrade implements ConfigurationSerializable {

    private final int villagerLevel;
    private final ItemStack result;
    private final List<ItemStack> ingredients;
    private final int maxUses;
    private final boolean experienceReward;
    private final int villagerExperience;
    private final float priceMultiplier;

    private final MerchantRecipe merchantRecipe;
    
    public VanillaTrade(
        int villagerLevel,
        ItemStack result,
        List<ItemStack> ingredients,
        int maxUses,
        boolean experienceReward,
        int villagerExperience,
        float priceMultiplier
    ) {
        this.villagerLevel = villagerLevel;
        this.result = result;
        this.ingredients = ingredients;
        this.maxUses = maxUses;
        this.experienceReward = experienceReward;
        this.villagerExperience = villagerExperience;
        this.priceMultiplier = priceMultiplier;

        merchantRecipe = new MerchantRecipe(
            result,
            0, // uses
            maxUses,
            experienceReward,
            villagerExperience,
            priceMultiplier
        );

        ingredients.forEach(ingredient -> {
            merchantRecipe.addIngredient(ingredient);
        });

    }

    public VanillaTrade(Map<String, Object> map) {

        villagerLevel = (int) map.get("villagerLevel");
        
        Object serializedResult = map.get("result");

        if(!(serializedResult instanceof Map)) {
            throw new IllegalArgumentException("result is not a map");
        }

        @SuppressWarnings("unchecked") 
        ItemStack result = ItemStack.deserialize(
            (Map<String, Object>) serializedResult
        );
        this.result = result;

        List<?> serializedIngredients = (List<?>) map.get("ingredients");
        ingredients = new ArrayList<>();

        for(Object serializedIngredient : serializedIngredients) {

            if(!(serializedIngredient instanceof Map)) {
                throw new IllegalArgumentException("ingredient is not a map");
            }

            @SuppressWarnings("unchecked") 
            ItemStack ingredient = ItemStack.deserialize(
                (Map<String, Object>) serializedIngredient
            );

            ingredients.add(ingredient);

        }

        maxUses = (int) map.get("maxUses");
        experienceReward = (boolean) map.get("experienceReward");
        villagerExperience = (int) map.get("villagerExperience");
        priceMultiplier = (float) map.get("priceMultiplier");

        merchantRecipe = new MerchantRecipe(
            result,
            0, // uses
            maxUses,
            experienceReward,
            villagerExperience,
            priceMultiplier
        );

        ingredients.forEach(ingredient -> {
            merchantRecipe.addIngredient(ingredient);
        });

    }

    public VanillaTrade(int villagerLevel, MerchantRecipe recipe) {
        this.villagerLevel = villagerLevel;
        result = recipe.getResult();
        ingredients = recipe.getIngredients();
        maxUses = recipe.getMaxUses();
        experienceReward = recipe.hasExperienceReward();
        villagerExperience = recipe.getVillagerExperience();
        priceMultiplier = recipe.getPriceMultiplier();
        merchantRecipe = recipe;
    }

    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> map = new HashMap<>();

        List<Map<String, Object>> ingredients = new ArrayList<>();
        this.ingredients.stream().forEach(ingredient -> {
            ingredients.add(ingredient.serialize());
        });

        map.put("villagerLevel", villagerLevel);
        map.put("result", result.serialize());
        map.put("ingredients", ingredients);
        map.put("maxUses", maxUses);
        map.put("experienceReward", experienceReward);
        map.put("villagerExperience", villagerExperience);
        map.put("priceMultiplier", priceMultiplier);

        return map;

    }

    public static VanillaTrade deserialize(Map<String, Object> map) {
        return new VanillaTrade(map);
    }

    public static VanillaTrade valueOf(Map<String, Object> map) {
        return new VanillaTrade(map);
    }

    public int getVillagerLevel() { return villagerLevel; }
    public ItemStack getResult() { return result; }
    public List<ItemStack> getIngredients() { return ingredients; }
    public int getMaxUses() { return maxUses; }
    public boolean getExperienceReward() { return experienceReward; }
    public int getVillagerExperience() { return villagerExperience; }
    public float getPriceMultiplier() { return priceMultiplier; }
    public MerchantRecipe getRecipe() { return merchantRecipe; }
    
}
