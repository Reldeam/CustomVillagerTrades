package online.meinkraft.customvillagertrades.villager;

import java.util.ArrayList;
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

import online.meinkraft.customvillagertrades.trade.CustomTrade;
import online.meinkraft.customvillagertrades.trade.VanillaTrade;

public class VillagerData implements ConfigurationSerializable {

    private final UUID entityId;
    private ArrayList<String> customTradeKeys;
    private ArrayList<VanillaTrade> vanillaTrades;

    public VillagerData(
        UUID entityId,
        ArrayList<String> customTradeKeys,
        ArrayList<VanillaTrade> vanillaTrades
    ) {
        this.entityId = entityId;
        this.customTradeKeys = customTradeKeys;
        this.vanillaTrades = vanillaTrades;
    } 

    public VillagerData(Merchant merchant) {

        Entity entity = (Entity) merchant;
        entityId = entity.getUniqueId();
        customTradeKeys = new ArrayList<>();
        vanillaTrades = new ArrayList<>();

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
            customTradeKeys.add(null);
        }

    }

    public VillagerData(Villager villager) {
        this((Merchant) villager);
    }

    public VillagerData(Map<String, Object> map) {
        
        entityId = UUID.fromString((String) map.get("entityId"));

        //unserialize custom trade keys

        @SuppressWarnings("unchecked")
        ArrayList<String>customTradeKeys = (ArrayList<String>) map.get("customTradeKeys");
        this.customTradeKeys = customTradeKeys;

        if(customTradeKeys == null) {
            throw new NullArgumentException("customTradeKeys doesn't exist");
        }

        // unserialize vanilla trade keys

        vanillaTrades = new ArrayList<>();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> serializedVanillaTrades = (List<Map<String, Object>>) map.get("vanillaTrades");

        if(serializedVanillaTrades == null) {
            throw new NullArgumentException("vanillaTrades doesn't exist");
        }
        
        serializedVanillaTrades.forEach(serializedVanillaTrade -> {
            vanillaTrades.add(new VanillaTrade(serializedVanillaTrade));
        });

        // make sure keys are set to the same length (solves issue with data
        // change from the customTradeKeyMask deprication).
        while(customTradeKeys.size() < vanillaTrades.size()) {
            customTradeKeys.add(null);
        }

    }
    
    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> map = new HashMap<>();
        map.put("entityId", entityId.toString());
        map.put("customTradeKeys", customTradeKeys);

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
        customTradeKeys.add(null);
    }

    public void addCustomTradeKey(int index, String key) {
        customTradeKeys.set(index, key);
    }

    public boolean removeCustomTrade(CustomTrade customTrade) {

        int keyIndex = customTradeKeys.indexOf(customTrade.getKey());
        if(keyIndex < 0) return false;

        VanillaTrade vanillaTrade = getVanillaTrade(keyIndex);

        // the covered vanilla trade is the same - remove both
        if(customTrade.getRecipe().equals(vanillaTrade.getRecipe())) {
            customTradeKeys.remove(keyIndex);
            vanillaTrades.remove(keyIndex);
        }
        // just remove the custom trade, revealing the vanilla trade
        else {
            customTradeKeys.set(keyIndex, null);
        }

        return true;

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
        customTradeKeys.replaceAll(element -> null);
    }

    public UUID getEntityId() { return entityId; }
    public List<String> getCustomTradeKeys() { return customTradeKeys; }
    public List<VanillaTrade> getVanillaTrades() { return vanillaTrades; }

    public VanillaTrade getVanillaTrade(int index) {
        return vanillaTrades.get(index);
    }
    
    public Boolean isCustomTrade(int index) {
        try {
            return customTradeKeys.get(index) != null;
        }
        catch(IndexOutOfBoundsException exception) {
            return false;
        } 
    }

    public Boolean isVanillaTrade(int index) {
        return !isCustomTrade(index);
    } 
    
}
