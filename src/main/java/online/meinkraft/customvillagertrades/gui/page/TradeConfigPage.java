package online.meinkraft.customvillagertrades.gui.page;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.GUI;
import online.meinkraft.customvillagertrades.gui.button.BackButton;
import online.meinkraft.customvillagertrades.gui.icon.DisabledSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.ModifiedSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.PropertyIcon;
import online.meinkraft.customvillagertrades.gui.icon.UnmodifiedSlotIcon;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class TradeConfigPage extends Page {

    private final DisabledSlotIcon disabledSlot;
    private final UnmodifiedSlotIcon unmodifiedSlot;
    private final ModifiedSlotIcon modifiedSlot;
    
    private final BackButton backButton;

    private final Map<String, PropertyIcon> icons;

    private CustomTradeEntry tradeEntry;

    public TradeConfigPage(GUI gui, String title) {

        super(gui, title);

        disabledSlot = new DisabledSlotIcon();
        unmodifiedSlot = new UnmodifiedSlotIcon();
        modifiedSlot = new ModifiedSlotIcon();

        backButton = new BackButton();
        setButton(45, "back", backButton);

        icons = new HashMap<>();
        
        icons.put("maxUses", new PropertyIcon(
            "maxUses", 
            Arrays.asList(new String[]{
                "The number of times this trade can be made before",
                "the villager needs to work to refresh the trade."
            }),
            Arrays.asList(new String[]{
                "A positive whole number (e.g. 4)"
            })
        ));
        
        icons.put("priceMultiplier", new PropertyIcon(
            "priceMultiplier", 
            Arrays.asList(new String[]{
                "Effects how much the cost of the trade changes",
                "depending on factors such as how much the villager",
                "likes or hates you, as well as how much you have",
                "traded with them."
            }),
            Arrays.asList(new String[]{ 
                "A positive number (e.g. 0.2)"
            })
        ));
        
        icons.put("experience", new PropertyIcon(
            "experience", 
            Arrays.asList(new String[]{
                "The amount of experience the villager and player",
                "will recieve. (If giveExperienceToPlayer is set",
                "To FALSE then only the villager will recieve this",
                "experience."
            }),
            Arrays.asList(new String[]{
                "A positive whole number (e.g. 4)"
            })
        ));
        
        icons.put("giveExperienceToPlayer", new PropertyIcon(
            "giveExperienceToPlayer",
            Arrays.asList(new String[]{
                "Whether the player recieves experience when making",
                "this trade or not."
            }), 
            Arrays.asList(new String[]{
                "TRUE or FALSE"
            })
        ));
        
        icons.put("chance", new PropertyIcon(
            "chance",
            Arrays.asList(new String[]{
                "The chance that the trade will be acquired when",
                "all other requirements are met. This chance will",
                "be weighted with all of the other possible custom",
                "trades the villager could get."
            }), 
            Arrays.asList(new String[]{
                "TRUE or FALSE"
            })
        ));
        
        icons.put("professions", new PropertyIcon(
            "professions",
            Arrays.asList(new String[]{
                "The required proffessions a villager needs to",
                "acquire this trade. No professions means that",
                "all professions could acquire this trade."
            }), 
            Arrays.asList(new String[]{
                "ARMORER, BUTCHER, CARTOGRAPHER,",
                "CLERIC, FARMER, FISHERMAN, FLETCHER, LEATHERWORKER,",
                "LIBRARIAN, MASON, SHEPHERD, TOOLSMITH, WEAPONSMITH"
            })
        ));
        
        icons.put("levels", new PropertyIcon(
            "levels",
            Arrays.asList(new String[]{
                "The levels at which a trader could acquire",
                "this trade."
            }), 
            Arrays.asList(new String[]{
                "1, 2, 3, 4, 5"
            })
        ));
        
        icons.put("villagerTypes", new PropertyIcon(
            "villagerTypes",
            Arrays.asList(new String[]{
                "The villager types that a villager needs to be",
                "to acquire this trade. No types means that all",
                "types could acquire this trade."
            }), 
            Arrays.asList(new String[]{
                "DESERT, JUNGLE, PLAINS,",
                "SAVANNA, SNOW, SWAMP, TAIGA"
            })
        ));
        
        icons.put("biomes", new PropertyIcon(
            "biomes",
            Arrays.asList(new String[]{
                "The biome(S) the villager must be in to be able",
                "to acquire this trade. No biome means that the",
                "villager could acquire the trade in any biome."
            }),  
            Arrays.asList(new String[]{
                "You can find a full list here:",
                "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/block/Biome.html"
            })
        ));


        // modification indication slots 
        setIcon(0, unmodifiedSlot);
        setIcon(9, unmodifiedSlot);
        setIcon(18, unmodifiedSlot);
        setIcon(27, unmodifiedSlot);
        setIcon(36, unmodifiedSlot);

        setIcon(5, unmodifiedSlot);
        setIcon(14, unmodifiedSlot);
        setIcon(23, unmodifiedSlot);
        setIcon(32, unmodifiedSlot);

        // disabled slots
        setIcon(4, disabledSlot);
        setIcon(13, disabledSlot);
        setIcon(22, disabledSlot);
        setIcon(31, disabledSlot);
        setIcon(40, disabledSlot);
        setIcon(49, disabledSlot);
        
    }

    public void setCustomTradeEntry(CustomTradeEntry tradeEntry) {

        this.tradeEntry = tradeEntry;
        CustomTrade trade = tradeEntry.getUpdates();

        // get values

        String maxUses = Integer.toString(trade.getMaxUses());
        String priceMultiplier = Double.toString(trade.getPriceMultiplier());
        String experience = Integer.toString(trade.getVillagerExperience());
        String giveExperienceToPlayer = Boolean.toString(trade.giveExperienceToPlayer()).toUpperCase();
        String chance = Double.toString(trade.getChance());

        String professions = Arrays.toString(trade.getProfessions().toArray());
        String levels = Arrays.toString(trade.getLevels().toArray());
        String villagerTypes = Arrays.toString(trade.getVillagerTypes().toArray());
        String biomes = Arrays.toString(trade.getBiomes().toArray());

        System.out.println(maxUses);

        // set current values

        icons.get("maxUses").setCurrentValue(maxUses);
        icons.get("priceMultiplier").setCurrentValue(priceMultiplier);
        icons.get("experience").setCurrentValue(experience);
        icons.get("giveExperienceToPlayer").setCurrentValue(giveExperienceToPlayer);
        icons.get("chance").setCurrentValue(chance);

        icons.get("professions").setCurrentValue(professions);
        icons.get("levels").setCurrentValue(levels);
        icons.get("villagerTypes").setCurrentValue(villagerTypes);
        icons.get("biomes").setCurrentValue(biomes);

        // set previous values

        icons.get("maxUses").setPreviousValue(maxUses);
        icons.get("priceMultiplier").setPreviousValue(priceMultiplier);
        icons.get("experience").setPreviousValue(experience);
        icons.get("giveExperienceToPlayer").setPreviousValue(giveExperienceToPlayer);
        icons.get("chance").setPreviousValue(chance);

        icons.get("professions").setPreviousValue(professions);
        icons.get("levels").setPreviousValue(levels);
        icons.get("villagerTypes").setPreviousValue(villagerTypes);
        icons.get("biomes").setPreviousValue(biomes);

        setIcon(1, icons.get("maxUses"));
        setIcon(10, icons.get("priceMultiplier"));
        setIcon(19, icons.get("experience"));
        setIcon(28, icons.get("giveExperienceToPlayer"));
        setIcon(37, icons.get("chance"));

        setIcon(6, icons.get("professions"));
        setIcon(15, icons.get("levels"));
        setIcon(24, icons.get("villagerTypes"));
        setIcon(33, icons.get("biomes"));

        backButton.setPage(tradeEntry.getTradeListPage());

    }

    public CustomTradeEntry getCustomTradeEntry() {
        return tradeEntry;
    }
    
}
