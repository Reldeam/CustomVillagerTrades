package online.meinkraft.customvillagertrades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class CustomTradeLoader {

    static List<CustomTrade> loadTrades(CustomVillagerTrades plugin) {

        FileConfiguration data = plugin.getTradesConfig();
        Logger logger = plugin.getLogger();

        ArrayList<CustomTrade> trades = new ArrayList<>();
        Integer tradesLoaded = 0;

        List<?> tradeMaps = data.getList("trades");

        for(Object item : tradeMaps) {

            // check item is valid before proceeding
            if(!(item instanceof HashMap)) {
                logger.warning(
                    "Skipping invalid custom trade: " + 
                    item.toString()
                );
                continue;
            }

            HashMap<?,?> tradeMap = (HashMap<?,?>) item;

            // recipe variables
            ItemStack result = null;
            List<ItemStack> ingredients = null;
            ItemStack firstIngredient = null;
            ItemStack secondIngredient = null;
            Integer maxUses = null;
            Integer villagerExperience = null;
            Boolean giveExperienceToPlayer = null;

            // requirement variables
            Double chance = null;
            List<Villager.Profession> professions = null;
            List<Integer> levels = null;
            List<Villager.Type> villagerTypes = null;
            List<Biome> biomes = null;

            try {
                
                result = CustomTradeLoader.toItemStack(
                    (HashMap<?,?>) tradeMap.get("result")
                );
                ingredients = CustomTradeLoader.toItemStackList(
                    (ArrayList<?>) tradeMap.get("ingredients")
                );
                professions = CustomTradeLoader.toVillagerProfessionList(
                    (ArrayList<?>) tradeMap.get("professions")
                );
                maxUses = (Integer) tradeMap.get("maxUses");
                villagerExperience = (Integer) tradeMap.get("villagerExperience");
                giveExperienceToPlayer = (Boolean) tradeMap.get("giveExperienceToPlayer");

                try {
                    chance = (Double) tradeMap.get("chance");
                }
                catch(ClassCastException exception) {
                    chance = Double.valueOf((Integer) tradeMap.get("chance"));
                }
                
                levels = CustomTradeLoader.toVillagerLevelList(
                    (ArrayList<?>) tradeMap.get("levels")
                );
                villagerTypes = CustomTradeLoader.toVillagerTypeList(
                    (ArrayList<?>) tradeMap.get("villagerTypes")
                );
                biomes = CustomTradeLoader.toBiomeList(
                    (ArrayList<?>) tradeMap.get("biomes")
                );

            }
            catch(IllegalArgumentException exception) {
                logger.warning(
                    "Skipping invalid custom trade (" +
                    exception.getMessage() +
                    "): " + 
                    item.toString()
                );
                continue;
            }
            catch(ClassCastException exception) {
                logger.warning(
                    "Skipping invalid custom trade (" +
                    "malformed trade; check variable types and line indents" +
                    "): " + 
                    item.toString()
                );
                continue;
            }

            // set defaults for optional fields if not given
            if(villagerExperience == null) villagerExperience = 2;
            if(giveExperienceToPlayer == null) giveExperienceToPlayer = true;
            
            // check required fields exist

            // ingredients
            if(ingredients == null || ingredients.size() < 1) {
                logger.warning(
                    "Skipping invalid custom trade (" +
                    "ingredients not found" +
                    "): " + 
                    item.toString()
                );
                continue;
            }
            firstIngredient = ingredients.get(0);
            if(ingredients.size() > 1) secondIngredient = ingredients.get(1);

            // maxUses
            if(maxUses == null) {
                logger.warning(
                    "Skipping invalid custom trade (" +
                    "maxUses not found" +
                    "): " + 
                    item.toString()
                );
                continue;
            }

            // chance
            if(chance == null) {
                logger.warning(
                    "Skipping invalid custom trade (" +
                    "chance not found" +
                    "): " + 
                    item.toString()
                );
                continue;
            }

            trades.add(new CustomTrade(

                result,
                firstIngredient,
                secondIngredient,
                maxUses,
                villagerExperience,
                giveExperienceToPlayer,

                chance,
                professions,
                levels,
                villagerTypes,
                biomes

            ));

            tradesLoaded++;

        }

        logger.info(
            "Loaded " + 
            tradesLoaded + 
            " out of " + 
            tradeMaps.size() + 
            " custom trades"
        );

        return trades;

    }

    static List<Villager.Type> toVillagerTypeList(ArrayList<?> list) {
        if(list == null) return null;
        List<Villager.Type> types = new ArrayList<>();
        list.forEach(item -> {
            if(item instanceof String) {
                Villager.Type type = Villager.Type.valueOf((String) item);
                types.add(type);
            }
        });
        if(types.size() > 0) return types;
        return null;
    }

    static List<Biome> toBiomeList(ArrayList<?> list) {
        if(list == null) return null;
        List<Biome> biomes = new ArrayList<>();
        list.forEach(item -> {
            if(item instanceof String) {
                Biome type = Biome.valueOf((String) item);
                biomes.add(type);
            }
        });
        if(biomes.size() > 0) return biomes;
        return null;
    }

    static List<ItemStack> toItemStackList(ArrayList<?> list) {
        if(list == null) return null;
        List<ItemStack> ingredients = new ArrayList<>();
        list.forEach(item -> {
            if(item instanceof HashMap) {
                ingredients.add(CustomTradeLoader.toItemStack(
                    (HashMap<?, ?>) item
                ));
            }
        });
        if(ingredients.size() > 0) return ingredients;
        return null;
    }

    static ItemStack toItemStack(HashMap<?, ?> map) {
        
        if(map == null) return null; 

        String material = (String) map.get("material");
        Integer amount = (Integer) map.get("amount");
        if(amount == null) amount = 1;

        ItemStack itemStack = new ItemStack(Material.valueOf(material), amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<ItemEnchantment> enchantments = CustomTradeLoader.toItemEnchantmentList(
            (ArrayList<?>) map.get("enchantments")
        );


        if(enchantments != null) {
            for(ItemEnchantment enchantment : enchantments) { 
                itemMeta.addEnchant(
                    enchantment.getEnchantment(), 
                    enchantment.getLevel(), 
                    enchantment.ignoreLevelRestriction()
                );
            }
        }

        String displayName = (String) map.get("displayName");
        if(displayName != null) itemMeta.setDisplayName(displayName);

        List<String> lore = CustomTradeLoader.toLore(
            (ArrayList<?>) map.get("lore")
        );
        if(lore != null) itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);   
        return itemStack;

    }

    static List<String> toLore(ArrayList<?> list) {
        if(list == null) return null;
        List<String> lore = new ArrayList<>();
        list.forEach(item -> {
            if(item instanceof String) lore.add((String) item);
        });
        if(lore.size() > 0) return lore;
        return null;
    }

    static List<Villager.Profession> toVillagerProfessionList(ArrayList<?> list) {
        if(list == null) return null;
        List<Villager.Profession> professions = new ArrayList<>();
        list.forEach(item -> {
            if(item instanceof String) {
                Villager.Profession profession = Villager.Profession.valueOf((String) item);
                professions.add(profession);
            }
        });
        if(professions.size() > 0) return professions;
        return null;
    }

    static List<Integer> toVillagerLevelList(ArrayList<?> list) {
        if(list == null) return null;
        List<Integer> levels = new ArrayList<>();
        list.forEach(item -> {
            if(item instanceof Integer) {
                levels.add((Integer) item);
            }
        });
        if(levels.size() > 0) return levels;
        return null;
    }

    static List<ItemEnchantment> toItemEnchantmentList(ArrayList<?> list) {

        if(list == null) {
            return null;
        }

        List<ItemEnchantment> itemEnchantments = new ArrayList<>();

        for(Object item : list) {

            if(!(item instanceof HashMap)) continue;
            HashMap<?,?> enchantmentMap = (HashMap<?,?>) item;

            String enchantmentType =  (String) enchantmentMap.get("type");
            Enchantment enchantment = new EnchantmentWrapper(
                enchantmentType.toLowerCase()
            ).getEnchantment();

            Integer level = (Integer) enchantmentMap.get("level");
            if(level == null) level = 1;

            Boolean ignoreLevelRestriction = (Boolean) enchantmentMap.get("ignoreLevelRestriction");
            if(ignoreLevelRestriction == null) ignoreLevelRestriction = false;

            ItemEnchantment itemEnchantment = new ItemEnchantment(
                enchantment, 
                level, 
                ignoreLevelRestriction
            );

            itemEnchantments.add(itemEnchantment);

        }

        if(itemEnchantments.size() > 0) {
            return itemEnchantments;
        }

        return null;

    }

}


class ItemEnchantment {

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