package online.meinkraft.customvillagertrades.gui.page;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Villager;

import net.md_5.bungee.api.ChatColor;
import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.GUI;
import online.meinkraft.customvillagertrades.gui.button.ConfigBackButton;
import online.meinkraft.customvillagertrades.gui.button.TextInputButton;
import online.meinkraft.customvillagertrades.gui.icon.DisabledSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.EmptySlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.ModifiedSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.PropertyIcon;
import online.meinkraft.customvillagertrades.gui.icon.UnmodifiedSlotIcon;
import online.meinkraft.customvillagertrades.prompt.PlayerPrompt;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class TradeConfigPage extends Page {

    private final DisabledSlotIcon disabledSlot;
    private final UnmodifiedSlotIcon unmodifiedSlot;
    private final ModifiedSlotIcon modifiedSlot;
    private final EmptySlotIcon emptySlot;
    
    private final ConfigBackButton backButton;

    private final Map<String, PropertyIcon> icons;

    private CustomTradeEntry tradeEntry;

    public TradeConfigPage(GUI gui, String title) {

        super(gui, title);

        disabledSlot = new DisabledSlotIcon();
        unmodifiedSlot = new UnmodifiedSlotIcon();
        modifiedSlot = new ModifiedSlotIcon();
        emptySlot = new EmptySlotIcon();

        backButton = new ConfigBackButton();
        setButton(Slot.BACK_BUTTON.index(), "back", backButton);

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
        
        icons.put("villagerExperience", new PropertyIcon(
            "villagerExperience", 
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

        // create text prompts

        TextInputButton maxUsesPrompt = new TextInputButton(
            "Edit maxUses", 
            new PlayerPrompt(gui.getPlugin(), "Enter a new value for maxUses")
        );
        maxUsesPrompt.onResponse(response -> {
            try {
                Integer maxUses = Integer.parseInt(response);
                this.tradeEntry.getUpdates().setMaxUses(maxUses);

                icons.get("maxUses").setCurrentValue(response);
                setIcon(Slot.MAX_USES_ICON.index(), icons.get("maxUses"));

                if(this.tradeEntry.isMaxUsesModified()) 
                    setIcon(Slot.MAX_USES_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.MAX_USES_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton priceMultiplierPrompt = new TextInputButton(
            "Edit priceMultiplier", 
            new PlayerPrompt(gui.getPlugin(), "Enter a new value for priceMultiplier")
        );
        priceMultiplierPrompt.onResponse(response -> {
            try {
                Double priceMultiplier = Double.parseDouble(response);
                this.tradeEntry.getUpdates().setPriceMultiplier(priceMultiplier);

                icons.get("priceMultiplier").setCurrentValue(response);
                setIcon(Slot.PRICE_MULTIPLIER_ICON.index(), icons.get("priceMultiplier"));

                if(this.tradeEntry.isPriceMultiplierModified()) 
                    setIcon(Slot.PRICE_MULTIPLIER_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.PRICE_MULTIPLIER_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton villagerExperiencePrompt = new TextInputButton(
            "Edit villagerExperience", 
            new PlayerPrompt(gui.getPlugin(), "Enter a new value for villagerExperience")
        );
        villagerExperiencePrompt.onResponse(response -> {
            try {
                Integer villagerExperience = Integer.parseInt(response);
                this.tradeEntry.getUpdates().setVillagerExperience(villagerExperience);

                icons.get("villagerExperience").setCurrentValue(response);
                setIcon(Slot.VILLAGER_EXPERIENCE_ICON.index(), icons.get("villagerExperience"));

                if(this.tradeEntry.isVillagerExperienceModified()) 
                    setIcon(Slot.VILLAGER_EXPERIENCE_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.VILLAGER_EXPERIENCE_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton giveExperienceToPlayerPrompt = new TextInputButton(
            "Edit giveExperienceToPlayer", 
            new PlayerPrompt(gui.getPlugin(), "Enter a new value for giveExperienceToPlayer")
        );
        giveExperienceToPlayerPrompt.onResponse(response -> {
            try {

                if(
                    !response.toUpperCase().equals("TRUE") ||
                    !response.toUpperCase().equals("FALSE")
                ) {
                    throw new IllegalArgumentException("value must be TRUE or FALSE");
                }

                Boolean giveExperienceToPlayer = Boolean.parseBoolean(response);
                this.tradeEntry.getUpdates().giveExperienceToPlayer(giveExperienceToPlayer);

                icons.get("giveExperienceToPlayer").setCurrentValue(giveExperienceToPlayer.toString().toUpperCase());
                setIcon(Slot.GIVE_EXPERIENCE_TO_PLAYER_ICON.index(), icons.get("giveExperienceToPlayer"));

                if(this.tradeEntry.isGiveExperienceToPlayerModified()) 
                    setIcon(Slot.GIVE_EXPERIENCE_TO_PLAYER_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.GIVE_EXPERIENCE_TO_PLAYER_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton chancePrompt = new TextInputButton(
            "Edit chance", 
            new PlayerPrompt(gui.getPlugin(), "Enter a new value for chance")
        );
        chancePrompt.onResponse(response -> {
            try {
                Double chance = Double.parseDouble(response);
                this.tradeEntry.getUpdates().setChance(chance);

                icons.get("chance").setCurrentValue(response);
                setIcon(Slot.CHANCE_ICON.index(), icons.get("chance"));

                if(this.tradeEntry.isChanceModified()) 
                    setIcon(Slot.CHANCE_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.CHANCE_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        // create add prompts

        TextInputButton addProfessionPrompt = new TextInputButton(
            Material.AXOLOTL_BUCKET,
            "Add profession", 
            new PlayerPrompt(gui.getPlugin(), "Enter a profession to add")
        );
        addProfessionPrompt.onResponse(response -> {
            try {
                Villager.Profession profession = Villager.Profession.valueOf(response.toUpperCase());
                this.tradeEntry.getUpdates().addProfession(profession);

                icons.get("professions").setCurrentValue(this.tradeEntry.getUpdates().getProfessions().toString());
                setIcon(Slot.PROFESSIONS_ICON.index(), icons.get("professions"));

                if(this.tradeEntry.isProfessionsModified()) 
                    setIcon(Slot.PROFESSIONS_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.PROFESSIONS_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton removeProfessionPrompt = new TextInputButton(
            Material.WATER_BUCKET,
            "Remove profession", 
            new PlayerPrompt(gui.getPlugin(), "Enter a profession to remove")
        );
        removeProfessionPrompt.onResponse(response -> {
            try {
                Villager.Profession profession = Villager.Profession.valueOf(response.toUpperCase());
                this.tradeEntry.getUpdates().removeProfession(profession);

                icons.get("professions").setCurrentValue(this.tradeEntry.getUpdates().getProfessions().toString());
                setIcon(Slot.PROFESSIONS_ICON.index(), icons.get("professions"));

                if(this.tradeEntry.isProfessionsModified()) 
                    setIcon(Slot.PROFESSIONS_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.PROFESSIONS_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton addLevelPrompt = new TextInputButton(
            Material.AXOLOTL_BUCKET,
            "Add level", 
            new PlayerPrompt(gui.getPlugin(), "Enter a level to add")
        );
        addLevelPrompt.onResponse(response -> {
            try {
                Integer level = Integer.parseInt(response);
                this.tradeEntry.getUpdates().addLevel(level);

                icons.get("levels").setCurrentValue(this.tradeEntry.getUpdates().getLevels().toString());
                setIcon(Slot.LEVELS_ICON.index(), icons.get("levels"));

                if(this.tradeEntry.isLevelsModified()) 
                    setIcon(Slot.LEVELS_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.LEVELS_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton removeLevelPrompt = new TextInputButton(
            Material.WATER_BUCKET,
            "Remove level", 
            new PlayerPrompt(gui.getPlugin(), "Enter a level to remove")
        );
        removeLevelPrompt.onResponse(response -> {
            try {
                Integer level = Integer.parseInt(response);
                this.tradeEntry.getUpdates().removeLevel(level);

                icons.get("levels").setCurrentValue(this.tradeEntry.getUpdates().getLevels().toString());
                setIcon(Slot.LEVELS_ICON.index(), icons.get("levels"));

                if(this.tradeEntry.isLevelsModified()) 
                    setIcon(Slot.LEVELS_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.LEVELS_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton addVillagerTypePrompt = new TextInputButton(
            Material.AXOLOTL_BUCKET,
            "Add villager type", 
            new PlayerPrompt(gui.getPlugin(), "Enter a villager type to add")
        );
        addVillagerTypePrompt.onResponse(response -> {
            try {
                Villager.Type type = Villager.Type.valueOf(response.toUpperCase());
                this.tradeEntry.getUpdates().addVillagerType(type);

                icons.get("villagerTypes").setCurrentValue(this.tradeEntry.getUpdates().getVillagerTypes().toString());
                setIcon(Slot.VILLAGER_TYPES_ICON.index(), icons.get("villagerTypes"));

                if(this.tradeEntry.isVillagerTypesModified()) 
                    setIcon(Slot.VILLAGER_TYPES_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.VILLAGER_TYPES_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton removeVillagerTypePrompt = new TextInputButton(
            Material.WATER_BUCKET,
            "Remove villager type", 
            new PlayerPrompt(gui.getPlugin(), "Enter a villager type to remove")
        );
        removeVillagerTypePrompt.onResponse(response -> {
            try {
                Villager.Type type = Villager.Type.valueOf(response.toUpperCase());
                this.tradeEntry.getUpdates().removeVillagerType(type);

                icons.get("villagerTypes").setCurrentValue(this.tradeEntry.getUpdates().getVillagerTypes().toString());
                setIcon(Slot.VILLAGER_TYPES_ICON.index(), icons.get("villagerTypes"));

                if(this.tradeEntry.isVillagerTypesModified()) 
                    setIcon(Slot.VILLAGER_TYPES_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.VILLAGER_TYPES_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton addBiomePrompt = new TextInputButton(
            Material.AXOLOTL_BUCKET,
            "Add biome", 
            new PlayerPrompt(gui.getPlugin(), "Enter a biome to add")
        );
        addBiomePrompt.onResponse(response -> {
            try {
                Biome biome = Biome.valueOf(response.toUpperCase());
                this.tradeEntry.getUpdates().addBiome(biome);

                icons.get("biomes").setCurrentValue(this.tradeEntry.getUpdates().getBiomes().toString());
                setIcon(Slot.BIOMES_ICON.index(), icons.get("biomes"));

                if(this.tradeEntry.isBiomesModified()) 
                    setIcon(Slot.BIOMES_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.BIOMES_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        TextInputButton removeBiomePrompt = new TextInputButton(
            Material.WATER_BUCKET,
            "Remove biome", 
            new PlayerPrompt(gui.getPlugin(), "Enter a biome to remove")
        );
        removeBiomePrompt.onResponse(response -> {
            try {
                Biome biome = Biome.valueOf(response.toUpperCase());
                this.tradeEntry.getUpdates().removeBiome(biome);

                icons.get("biomes").setCurrentValue(this.tradeEntry.getUpdates().getBiomes().toString());
                setIcon(Slot.BIOMES_ICON.index(), icons.get("biomes"));

                if(this.tradeEntry.isBiomesModified()) 
                    setIcon(Slot.BIOMES_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.BIOMES_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                gui.getPlayer().sendMessage(
                    ChatColor.RED + "Failed to edit value: " +
                    exception.getMessage()
                );
            }  

            gui.openPage(this, gui.getPlayer());
        });

        // place prompts
        setButton(Slot.MAX_USES_PROMPT.index(), "maxUsesPrompt", maxUsesPrompt);
        setButton(Slot.PRICE_MULTIPLIER_PROMPT.index(), "priceMultiplierPrompt", priceMultiplierPrompt);
        setButton(Slot.EXPERIENCE_PROMPT.index(), "villagerExperiencePrompt", villagerExperiencePrompt);
        setButton(Slot.GIVE_EXPERIENCE_TO_PLAYER_PROMPT.index(), "giveExperienceToPlayerPrompt", giveExperienceToPlayerPrompt);
        
        setButton(Slot.CHANCE_PROMPT.index(), "chancePrompt", chancePrompt);

        setButton(Slot.PROFESSIONS_ADD_PROMPT.index(), "addProfessionPrompt", addProfessionPrompt);
        setButton(Slot.PROFESSIONS_REMOVE_PROMPT.index(), "removeProfessionPrompt", removeProfessionPrompt);

        setButton(Slot.LEVELS_ADD_PROMPT.index(), "addLevelPrompt", addLevelPrompt);
        setButton(Slot.LEVELS_REMOVE_PROMPT.index(), "removeLevelPrompt", removeLevelPrompt);

        setButton(Slot.VILLAGER_TYPES_ADD_PROMPT.index(), "addVillagerTypePrompt", addVillagerTypePrompt);
        setButton(Slot.VILLAGER_TYPES_REMOVE_PROMPT.index(), "removeVillagerTypePrompt", removeVillagerTypePrompt);

        setButton(Slot.BIOMES_ADD_PROMPT.index(), "addBiomePrompt", addBiomePrompt);
        setButton(Slot.BIOMES_REMOVE_PROMPT.index(), "removeBiomePrompt", removeBiomePrompt);

        // disabled slots
        setIcon(4, disabledSlot);
        setIcon(13, disabledSlot);
        setIcon(22, disabledSlot);
        setIcon(31, disabledSlot);
        setIcon(40, disabledSlot);
        setIcon(49, disabledSlot);

        //empty slots
        setIcon(2, emptySlot);
        setIcon(11, emptySlot);
        setIcon(20, emptySlot);
        setIcon(29, emptySlot);
        setIcon(38, emptySlot);
        
        setIcon(41, emptySlot);
        setIcon(42, emptySlot);
        setIcon(43, emptySlot);
        setIcon(44, emptySlot);

        setIcon(46, emptySlot);
        setIcon(47, emptySlot);
        setIcon(48, emptySlot);

        setIcon(50, emptySlot);
        setIcon(51, emptySlot);
        setIcon(52, emptySlot);
        setIcon(53, emptySlot);

    }

    public void setCustomTradeEntry(CustomTradeEntry tradeEntry) {

        this.tradeEntry = tradeEntry;
        CustomTrade trade = tradeEntry.getUpdates();

        // set current values

        String maxUses = Integer.toString(trade.getMaxUses());
        String priceMultiplier = Double.toString(trade.getPriceMultiplier());
        String villagerExperience = Integer.toString(trade.getVillagerExperience());
        String giveExperienceToPlayer = Boolean.toString(trade.giveExperienceToPlayer()).toUpperCase();
        String chance = Double.toString(trade.getChance());

        String professions = Arrays.toString(trade.getProfessions().toArray());
        String levels = Arrays.toString(trade.getLevels().toArray());
        String villagerTypes = Arrays.toString(trade.getVillagerTypes().toArray());
        String biomes = Arrays.toString(trade.getBiomes().toArray());

        icons.get("maxUses").setCurrentValue(maxUses);
        icons.get("priceMultiplier").setCurrentValue(priceMultiplier);
        icons.get("villagerExperience").setCurrentValue(villagerExperience);
        icons.get("giveExperienceToPlayer").setCurrentValue(giveExperienceToPlayer);
        icons.get("chance").setCurrentValue(chance);

        icons.get("professions").setCurrentValue(professions);
        icons.get("levels").setCurrentValue(levels);
        icons.get("villagerTypes").setCurrentValue(villagerTypes);
        icons.get("biomes").setCurrentValue(biomes);

        // set previous values

        trade = tradeEntry.getTrade();

        maxUses = Integer.toString(trade.getMaxUses());
        priceMultiplier = Double.toString(trade.getPriceMultiplier());
        villagerExperience = Integer.toString(trade.getVillagerExperience());
        giveExperienceToPlayer = Boolean.toString(trade.giveExperienceToPlayer()).toUpperCase();
        chance = Double.toString(trade.getChance());

        professions = Arrays.toString(trade.getProfessions().toArray());
        levels = Arrays.toString(trade.getLevels().toArray());
        villagerTypes = Arrays.toString(trade.getVillagerTypes().toArray());
        biomes = Arrays.toString(trade.getBiomes().toArray());

        icons.get("maxUses").setPreviousValue(maxUses);
        icons.get("priceMultiplier").setPreviousValue(priceMultiplier);
        icons.get("villagerExperience").setPreviousValue(villagerExperience);
        icons.get("giveExperienceToPlayer").setPreviousValue(giveExperienceToPlayer);
        icons.get("chance").setPreviousValue(chance);

        icons.get("professions").setPreviousValue(professions);
        icons.get("levels").setPreviousValue(levels);
        icons.get("villagerTypes").setPreviousValue(villagerTypes);
        icons.get("biomes").setPreviousValue(biomes);

        // update icons
        setIcon(Slot.MAX_USES_ICON.index(), icons.get("maxUses"));
        setIcon(Slot.PRICE_MULTIPLIER_ICON.index(), icons.get("priceMultiplier"));
        setIcon(Slot.VILLAGER_EXPERIENCE_ICON.index(), icons.get("villagerExperience"));
        setIcon(Slot.GIVE_EXPERIENCE_TO_PLAYER_ICON.index(), icons.get("giveExperienceToPlayer"));
        setIcon(Slot.CHANCE_ICON.index(), icons.get("chance"));

        setIcon(Slot.PROFESSIONS_ICON.index(), icons.get("professions"));
        setIcon(Slot.LEVELS_ICON.index(), icons.get("levels"));
        setIcon(Slot.VILLAGER_TYPES_ICON.index(), icons.get("villagerTypes"));
        setIcon(Slot.BIOMES_ICON.index(), icons.get("biomes"));

        // update modification indication slots 
        updateIndicator(tradeEntry.isMaxUsesModified(), Slot.MAX_USES_MODIFY_INDICATOR);
        updateIndicator(tradeEntry.isPriceMultiplierModified(), Slot.PRICE_MULTIPLIER_MODIFY_INDICATOR);
        updateIndicator(tradeEntry.isVillagerExperienceModified(), Slot.VILLAGER_EXPERIENCE_MODIFY_INDICATOR);
        updateIndicator(tradeEntry.isGiveExperienceToPlayerModified(), Slot.GIVE_EXPERIENCE_TO_PLAYER_MODIFY_INDICATOR);
        updateIndicator(tradeEntry.isChanceModified(), Slot.CHANCE_MODIFY_INDICATOR);
        updateIndicator(tradeEntry.isProfessionsModified(), Slot.PROFESSIONS_MODIFY_INDICATOR);
        updateIndicator(tradeEntry.isLevelsModified(), Slot.LEVELS_MODIFY_INDICATOR);
        updateIndicator(tradeEntry.isVillagerTypesModified(), Slot.VILLAGER_TYPES_MODIFY_INDICATOR);
        updateIndicator(tradeEntry.isBiomesModified(), Slot.BIOMES_MODIFY_INDICATOR);

        // set navigation properties
        backButton.setPage(tradeEntry.getTradeListPage());

    }

    private void updateIndicator(boolean isModified, Slot slot) {
        if(isModified) setIcon(slot.index(), modifiedSlot);
        else setIcon(slot.index(), unmodifiedSlot);
    }

    public CustomTradeEntry getCustomTradeEntry() {
        return tradeEntry;
    }

    private enum Slot {

        MAX_USES_ICON(1),
        PRICE_MULTIPLIER_ICON(10),
        VILLAGER_EXPERIENCE_ICON(19),
        GIVE_EXPERIENCE_TO_PLAYER_ICON(28),
        CHANCE_ICON(37),
        PROFESSIONS_ICON(6),
        LEVELS_ICON(15),
        VILLAGER_TYPES_ICON(24),
        BIOMES_ICON(33),
        
        MAX_USES_MODIFY_INDICATOR(0),
        PRICE_MULTIPLIER_MODIFY_INDICATOR(9),
        VILLAGER_EXPERIENCE_MODIFY_INDICATOR(18),
        GIVE_EXPERIENCE_TO_PLAYER_MODIFY_INDICATOR(27),
        CHANCE_MODIFY_INDICATOR(36),
        PROFESSIONS_MODIFY_INDICATOR(5),
        LEVELS_MODIFY_INDICATOR(14),
        VILLAGER_TYPES_MODIFY_INDICATOR(23),
        BIOMES_MODIFY_INDICATOR(32),
        
        MAX_USES_PROMPT(3),
        PRICE_MULTIPLIER_PROMPT(12),
        EXPERIENCE_PROMPT(21),
        GIVE_EXPERIENCE_TO_PLAYER_PROMPT(30),
        CHANCE_PROMPT(39),
        
        PROFESSIONS_ADD_PROMPT(8),
        LEVELS_ADD_PROMPT(17),
        VILLAGER_TYPES_ADD_PROMPT(26),
        BIOMES_ADD_PROMPT(35),
        
        PROFESSIONS_REMOVE_PROMPT(7),
        LEVELS_REMOVE_PROMPT(16),
        VILLAGER_TYPES_REMOVE_PROMPT(25),
        BIOMES_REMOVE_PROMPT(34),
        
        BACK_BUTTON(45);

        private int value; 
        
        private Slot(int value) { 
            this.value = value; 
        }

        public int index() {
            return value;
        }

    }
    
}