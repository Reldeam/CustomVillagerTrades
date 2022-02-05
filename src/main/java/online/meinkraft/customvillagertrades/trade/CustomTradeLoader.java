package online.meinkraft.customvillagertrades.trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Biome;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.exception.EconomyNotEnabledException;
import online.meinkraft.customvillagertrades.exception.IngredientsNotFoundException;
import online.meinkraft.customvillagertrades.exception.ResultNotFoundException;
import online.meinkraft.customvillagertrades.util.AttributeModifierWrapper;
import online.meinkraft.customvillagertrades.util.ItemEnchantment;
import online.meinkraft.customvillagertrades.util.MoneyItem;

public final class CustomTradeLoader {

    static public Map<String, CustomTrade> loadTrades(CustomVillagerTrades plugin) {

        FileConfiguration data = plugin.getTradesConfig();
        Logger logger = plugin.getLogger();

        Map<String, CustomTrade> trades = new HashMap<>();
        Integer tradesLoaded = 0;

        Set<String> tradeNames = data.getKeys(false);
        
        for(String tradeName : tradeNames) {

            Object dataItem = data.get(tradeName);
            // check item is valid before proceeding
            if(!(dataItem instanceof MemorySection)) {
                logger.warning(
                    "Skipping invalid custom trade: " + 
                    dataItem.toString()
                );
                continue;
            }

            //MemorySection tradeSection = (MemorySection) item;
            MemorySection tradeSection = (MemorySection) dataItem;

            // recipe variables
            ItemStack result = null;
            List<ItemStack> ingredients = null;
            ItemStack firstIngredient = null;
            ItemStack secondIngredient = null;

            Integer maxUses = tradeSection.getInt("maxUses");
            Double priceMultiplier = tradeSection.getDouble("priceMultiplier");
            Integer villagerExperience = tradeSection.getInt("experience");
            Boolean giveExperienceToPlayer = tradeSection.getBoolean("giveExperienceToPlayer");
                
            // requirement variables
            Double chance = tradeSection.getDouble("chance");  
            List<Villager.Profession> professions = CustomTradeLoader.toVillagerProfessionList(
                (List<String>) tradeSection.getStringList("professions")
            );
            List<Integer> levels = tradeSection.getIntegerList("levels");
            List<Villager.Type> villagerTypes = CustomTradeLoader.toVillagerTypeList(
                (List<String>) tradeSection.getStringList("villagerTypes")
            );
            List<Biome> biomes = CustomTradeLoader.toBiomeList(
                (List<String>) tradeSection.getStringList("biomes")
            );

            try {
                // objects

                MemorySection editor = (MemorySection) tradeSection.get("editor");

                // get items from editor field
                if(editor != null) {
                    result = editor.getItemStack("result");
                    firstIngredient = editor.getItemStack("firstIngredient");
                    secondIngredient = editor.getItemStack("secondIngredient");
                }
                else {
                    MemorySection resultSection = (MemorySection) tradeSection.get("result");

                    if(resultSection == null) {
                        throw new ResultNotFoundException();
                    }

                    result = CustomTradeLoader.toItemStack(
                        plugin,
                        (Map<?, ?>) resultSection.getValues(false)
                    );  
    
                    ingredients = CustomTradeLoader.toItemStackList(
                        plugin,
                        (List<?>) tradeSection.getMapList("ingredients")
                    );

                    // ingredients
                    if(ingredients == null || ingredients.size() < 1) {
                        throw new IngredientsNotFoundException();
                    }

                    firstIngredient = ingredients.get(0);
                    if(ingredients.size() > 1) {
                        secondIngredient = ingredients.get(1);
                    }

                }
                
            }
            catch(IllegalArgumentException exception) {
                logger.warning(
                    ChatColor.YELLOW +
                    "Skipping invalid custom trade " +
                    ChatColor.AQUA +
                    tradeSection.getCurrentPath() +
                    ChatColor.YELLOW +
                    " (" + exception.getMessage() + ")"
                );
                continue;
            }
            catch(ClassCastException exception) {
                logger.warning(
                    ChatColor.YELLOW +
                    "Skipping invalid custom trade " +
                    ChatColor.AQUA +
                    tradeSection.getCurrentPath() +
                    ChatColor.YELLOW +
                    " (malformed trade; check variable types and line indents)"
                );
                continue;
            } catch (EconomyNotEnabledException exception) {
                logger.warning(
                    ChatColor.YELLOW +
                    "Skipping invalid custom trade " +
                    ChatColor.AQUA +
                    tradeSection.getCurrentPath() +
                    ChatColor.YELLOW +
                    " (trade has a money component but economy is not enabled)"
                    
                );
                continue;
            } catch (IngredientsNotFoundException exception) {
                logger.warning(
                    ChatColor.YELLOW +
                    "Skipping invalid custom trade " +
                    ChatColor.AQUA +
                    tradeSection.getCurrentPath() +
                    ChatColor.YELLOW +
                    " (ingredients not found)"
                );
                continue;
            } catch (ResultNotFoundException e) {
                logger.warning(
                    ChatColor.YELLOW +
                    "Skipping invalid custom trade " +
                    ChatColor.AQUA +
                    tradeSection.getCurrentPath() +
                    ChatColor.YELLOW +
                    " (result not found)"
                );
                continue;
            }

            CustomTrade trade = new CustomTrade(
                
                tradeName,
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

            if(trades.containsKey(tradeName)) {
                CustomTrade duplicateTrade = trades.get(tradeName);
                logger.warning(
                    ChatColor.YELLOW +
                    "Trade already exists\n" +
                    "Replacing: " + 
                    ChatColor.AQUA + duplicateTrade.toString() + "\n" +
                    ChatColor.YELLOW + "With: " + 
                    ChatColor.AQUA + trade.toString()
                );
            }

            trades.put(tradeName, trade);
            tradesLoaded++;

        }

        ChatColor loadedColor = ChatColor.RED;
        if(tradesLoaded == tradeNames.size()) loadedColor = ChatColor.GREEN;
        else if(tradesLoaded >= tradeNames.size() / 2) loadedColor = ChatColor.YELLOW;

        logger.info(
            "Loaded " + 
            loadedColor + tradesLoaded + 
            ChatColor.RESET +" out of " + 
            ChatColor.GREEN + tradeNames.size() + 
            ChatColor.RESET + " custom trades"
        );

        return trades;

    }

    static public List<Villager.Type> toVillagerTypeList(List<String> list) {
        List<Villager.Type> types = new ArrayList<>();
        for(String item : list) {
            Villager.Type type = Villager.Type.valueOf((String) item);
            types.add(type);
        }
        return types;
    }

    static public List<Biome> toBiomeList(List<String> list) {
        List<Biome> biomes = new ArrayList<>();
        for(String item : list) {
            Biome type = Biome.valueOf((String) item);
            biomes.add(type);
        }
        return biomes;
    }

    static public List<Villager.Profession> toVillagerProfessionList(List<String> list) {
        List<Villager.Profession> professions = new ArrayList<>();
        for(String item : list) {
            Villager.Profession profession = Villager.Profession.valueOf((String) item);
            professions.add(profession);
        }
        return professions;
    }

    static public List<ItemStack> toItemStackList(CustomVillagerTrades plugin, List<?> list) throws EconomyNotEnabledException {
        List<ItemStack> ingredients = new ArrayList<>();
        for(Object item : list) {
            ingredients.add(CustomTradeLoader.toItemStack(plugin, (Map<?, ?>) item));
        }
        return ingredients;
    }

    @SuppressWarnings("deprecation")
    static public ItemStack toItemStack(CustomVillagerTrades plugin, Map<?, ?> map) throws EconomyNotEnabledException {

        // if it's a money item
        if(map.containsKey("money")) {

            if(!plugin.isEconomyEnabled()) {
                throw new EconomyNotEnabledException();
            }

            Double amount;
            try {
                amount = (Double) map.get("money");
            }
            catch(ClassCastException exception) {
                amount = Double.valueOf((Integer) map.get("money"));
            }

            return MoneyItem.create(plugin, amount);

        }

        String material = (String) map.get("material");
        Integer amount = (Integer) map.get("amount");
        if(amount == null) amount = 1;

        String nbt = (String) map.get("nbt");

        ItemStack itemStack = new ItemStack(Material.valueOf(material), amount);

        // this is unsupported - use at your own risk
        if(nbt != null) {
            org.bukkit.UnsafeValues unsafe = Bukkit.getUnsafe();
            unsafe.modifyItemStack(itemStack, nbt);
        }
       
        ItemMeta itemMeta = itemStack.getItemMeta();

         // set enchantments
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

        // set display name
        if(map.containsKey("displayName")) {
            String displayName = (String) map.get("displayName");
            itemMeta.setDisplayName(displayName);
        }

        // set lore
        if(map.containsKey("lore")) {
            List<?> loreList = (List<?>) map.get("lore");
            itemMeta.setLore(CustomTradeLoader.toLore(loreList));
        }

        // set attribute modifiers
        List<AttributeModifierWrapper> wrappers = CustomTradeLoader.toAttributeModifierWrapperList(    
            (ArrayList<?>) map.get("attributeModifiers")
        );
        if(wrappers != null) {
            for(AttributeModifierWrapper wrapper : wrappers) {
                itemMeta.addAttributeModifier(
                    wrapper.geAttribute(), 
                    wrapper.getModifier()
                );
            }
        }

        itemStack.setItemMeta(itemMeta);  
        return itemStack;

    }

    static private List<String> toLore(List<?> list) {
        if(list == null) return null;
        List<String> lore = new ArrayList<>();
        list.forEach(item -> {
            if(item instanceof String) lore.add((String) item);
        });
        if(lore.size() > 0) return lore;
        return null;
    }

    static private List<AttributeModifierWrapper> toAttributeModifierWrapperList(ArrayList<?> list) {
        
        if(list == null) return null;

        List<AttributeModifierWrapper> wrappers = new ArrayList<>();

        for(Object item : list) {

            if(!(item instanceof HashMap)) continue;
            HashMap<?,?> map = (HashMap<?,?>) item;

            String nameString = (String) map.get("name");
            Attribute attribute = Attribute.valueOf(nameString.toUpperCase());

            Double amount;
            try {
                amount = (Double) map.get("amount");
            }
            catch(ClassCastException exception) {
                amount = Double.valueOf((Integer) map.get("amount"));
            }

            String operationString = (String) map.get("operation");
            AttributeModifier.Operation operation;

            switch(operationString.toUpperCase()) {
                case "ADD":
                    operation = AttributeModifier.Operation.ADD_NUMBER;
                    break;
                case "MULTIPLY":
                    operation = AttributeModifier.Operation.ADD_SCALAR;
                    break;
                case "MULTIPLY_ALL_MODIFIERS":
                    operation = AttributeModifier.Operation.MULTIPLY_SCALAR_1;
                    break; 
                default:
                    throw new IllegalArgumentException(
                        "attributeModifier.operation must be one of " + 
                        "ADD | MULTIPLY | MULTIPLY_ALL_MODIFIERS"
                    );
            }
             
            String slotString = (String) map.get("slot");
            EquipmentSlot slot = null;
            if(slotString != null) slot = EquipmentSlot.valueOf(slotString.toUpperCase());

            AttributeModifier modifier = new AttributeModifier(
                UUID.randomUUID(),
                nameString.toUpperCase(), 
                amount, 
                operation,
                slot
            );

            AttributeModifierWrapper wrapper = new AttributeModifierWrapper(
                attribute, 
                modifier
            );

            wrappers.add(wrapper);
        }

        if(wrappers.size() > 0) return wrappers;
        return null;

    }

    static private List<ItemEnchantment> toItemEnchantmentList(ArrayList<?> list) {

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