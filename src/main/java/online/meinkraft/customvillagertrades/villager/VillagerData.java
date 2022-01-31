package online.meinkraft.customvillagertrades.villager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

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

    public VillagerData(Villager villager) {
        Merchant merchant = (Merchant) villager;
        entityId = villager.getUniqueId();
        customTradeKeys = new ArrayList<>();
        vanillaTrades = new ArrayList<>();
        for(MerchantRecipe recipe : merchant.getRecipes()) {
            vanillaTrades.add(new VanillaTrade(villager.getVillagerLevel(), recipe));
        }
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
    }

    public void addCustomTradeKey(String key) {
        customTradeKeys.add(key);
    }

    public void clearCustomTradeKeys() {
        customTradeKeys = new ArrayList<>();
    }

    public UUID getEntityId() { return entityId; }
    public List<String> getCustomTradeKeys() { return customTradeKeys; }
    public List<VanillaTrade> getVanillaTrades() { return vanillaTrades; }
    
}
