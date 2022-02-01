package online.meinkraft.customvillagertrades.villager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import online.meinkraft.customvillagertrades.trade.VanillaTrade;

public class VillagerData implements ConfigurationSerializable {

    private final UUID entityId;
    private ArrayList<String> customTradeKeys;
    private ArrayList<VanillaTrade> vanillaTrades;
    private ArrayList<Boolean> customTradeMask;

    public VillagerData(
        UUID entityId,
        ArrayList<String> customTradeKeys,
        ArrayList<VanillaTrade> vanillaTrades,
        ArrayList<Boolean> customTradeMask
    ) {
        this.entityId = entityId;
        this.customTradeKeys = customTradeKeys;
        this.vanillaTrades = vanillaTrades;
        this.customTradeMask = customTradeMask;
    } 

    public VillagerData(Merchant merchant) {

        Entity entity = (Entity) merchant.getTrader();
        entityId = entity.getUniqueId();
        customTradeKeys = new ArrayList<>();
        vanillaTrades = new ArrayList<>();
        customTradeMask = new ArrayList<>();

        Villager villager;
        try {
            villager = (Villager) merchant;
        }
        catch(ClassCastException exception) {
            villager = null;
        }

        int villagerLevel = 0;
        for(MerchantRecipe recipe : merchant.getRecipes()) {
            if(villager != null) villagerLevel = villager.getVillagerLevel();
            vanillaTrades.add(new VanillaTrade(villagerLevel, recipe));
            customTradeMask.add(false);
        }

    }

    public VillagerData(Villager villager) {
        this((Merchant) villager);
    }

    public VillagerData(Map<String, Object> map) {
        
        entityId = UUID.fromString((String) map.get("entityId"));

        @SuppressWarnings("unchecked")
        ArrayList<String>customTradeKeys = (ArrayList<String>) map.get("customTradeKeys");
        this.customTradeKeys = customTradeKeys;

        if(customTradeKeys == null) {
            throw new NullArgumentException("customTradeKeys doesn't exist");
        }

        vanillaTrades = new ArrayList<>();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> serializedVanillaTrades = (List<Map<String, Object>>) map.get("vanillaTrades");

        if(serializedVanillaTrades == null) {
            throw new NullArgumentException("vanillaTrades doesn't exist");
        }
        
        serializedVanillaTrades.forEach(serializedVanillaTrade -> {
            vanillaTrades.add(new VanillaTrade(serializedVanillaTrade));
        });

        if(map.containsKey("customTradeMask")) {
            @SuppressWarnings("unchecked")
            ArrayList<Boolean>customTradeMask = (ArrayList<Boolean>) map.get("customTradeMask");
            this.customTradeMask = customTradeMask;
        }
        else { // pre 1.2-SNAPSHOT compatibility (customTradeMask may not exist)
            this.customTradeMask = new ArrayList<Boolean>(
                Collections.nCopies(vanillaTrades.size(), false)
            );
        }

    }
    
    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> map = new HashMap<>();
        map.put("entityId", entityId.toString());
        map.put("customTradeKeys", customTradeKeys);
        map.put("customTradeMask", customTradeMask);

        ArrayList<Map<String, Object>> serializedVanillaTrades = new ArrayList<>();
        vanillaTrades.forEach(vanillaTrade -> {
            serializedVanillaTrades.add(vanillaTrade.serialize());
        });
        map.put("vanillaTrades", serializedVanillaTrades);

        return map;
        
    }

    public static VillagerData deserialize(Map<String, Object> map) {
        return new VillagerData(map);
    }

    public static VillagerData valueOf(Map<String, Object> map) {
        return new VillagerData(map);
    }

    public void addVanillaTrade(int villagerLevel, MerchantRecipe recipe) {
        vanillaTrades.add(new VanillaTrade(villagerLevel, recipe));
        customTradeMask.add(false);
    }

    public void addCustomTradeKey(int index, String key) {
        customTradeMask.set(index, true);
        customTradeKeys.add(key);
    }

    public String getCustomTradeKey(int index) {
        try {
            return customTradeKeys.get(index);
        }
        catch(IndexOutOfBoundsException exception) {
            return null;
        }
    }

    public void clearCustomTradeKeys() {
        customTradeKeys = new ArrayList<>();
        customTradeMask.replaceAll(element -> false);
    }

    public UUID getEntityId() { return entityId; }
    public List<String> getCustomTradeKeys() { return customTradeKeys; }
    public List<VanillaTrade> getVanillaTrades() { return vanillaTrades; }
    
    public Boolean isCustomTrade(int index) {
        return customTradeMask.get(index);
    }

    public Boolean isVanillaTrade(int index) {
        return !isCustomTrade(index);
    } 
    
}
