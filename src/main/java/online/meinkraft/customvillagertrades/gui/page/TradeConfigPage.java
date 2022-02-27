package online.meinkraft.customvillagertrades.gui.page;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Villager;

import net.md_5.bungee.api.ChatColor;
import online.meinkraft.customvillagertrades.gui.CustomTradeEntry;
import online.meinkraft.customvillagertrades.gui.Editor;
import online.meinkraft.customvillagertrades.gui.button.ConfigBackButton;
import online.meinkraft.customvillagertrades.gui.button.TextInputButton;
import online.meinkraft.customvillagertrades.gui.icon.DisabledSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.EmptySlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.ModifiedSlotIcon;
import online.meinkraft.customvillagertrades.gui.icon.PropertyIcon;
import online.meinkraft.customvillagertrades.gui.icon.UnmodifiedSlotIcon;
import online.meinkraft.customvillagertrades.prompt.PlayerPrompt;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class TradeConfigPage extends EditorPage {

    private final DisabledSlotIcon disabledSlot;
    private final UnmodifiedSlotIcon unmodifiedSlot;
    private final ModifiedSlotIcon modifiedSlot;
    private final EmptySlotIcon emptySlot;
    
    private final ConfigBackButton backButton;

    private final Map<String, PropertyIcon> icons;

    private CustomTradeEntry tradeEntry;

    public TradeConfigPage(Editor editor, String title) {

        super(editor, title);

        disabledSlot = new DisabledSlotIcon();
        unmodifiedSlot = new UnmodifiedSlotIcon();
        modifiedSlot = new ModifiedSlotIcon();
        emptySlot = new EmptySlotIcon();

        backButton = new ConfigBackButton(this);
        setButton(Slot.BACK_BUTTON.index(), "back", backButton);

        icons = new HashMap<>();
        
        icons.put("maxUses", new PropertyIcon(
            "maxUses", 
            editor.getMessage("maxUsesIconDescription").lines().toList(),
            editor.getMessage("maxUsesIconPossibleValues").lines().toList()
        ));
        
        icons.put("priceMultiplier", new PropertyIcon(
            "priceMultiplier", 
            editor.getMessage("priceMultiplierIconDescription").lines().toList(),
            editor.getMessage("priceMultiplierIconPossibleValues").lines().toList()
        ));
        
        icons.put("villagerExperience", new PropertyIcon(
            "villagerExperience", 
            editor.getMessage("villagerExperienceIconDescription").lines().toList(),
            editor.getMessage("villagerExperienceIconPossibleValues").lines().toList()
        ));
        
        icons.put("giveExperienceToPlayer", new PropertyIcon(
            "giveExperienceToPlayer",
            editor.getMessage("giveExperienceToPlayerIconDescription").lines().toList(),
            editor.getMessage("giveExperienceToPlayerIconPossibleValues").lines().toList()
        ));
        
        icons.put("chance", new PropertyIcon(
            "chance",
            editor.getMessage("chanceIconDescription").lines().toList(),
            editor.getMessage("chanceIconPossibleValues").lines().toList()
        ));
        
        icons.put("professions", new PropertyIcon(
            "professions",
            editor.getMessage("professionsIconDescription").lines().toList(),
            editor.getMessage("professionsIconPossibleValues").lines().toList()
        ));
        
        icons.put("levels", new PropertyIcon(
            "levels",
            editor.getMessage("levelsIconDescription").lines().toList(),
            editor.getMessage("levelsIconPossibleValues").lines().toList()
        ));
        
        icons.put("villagerTypes", new PropertyIcon(
            "villagerTypes",
            editor.getMessage("villagerTypesIconDescription").lines().toList(),
            editor.getMessage("villagerTypesIconPossibleValues").lines().toList()
        ));
        
        icons.put("biomes", new PropertyIcon(
            "biomes",
            editor.getMessage("biomesIconDescription").lines().toList(),
            editor.getMessage("biomesIconPossibleValues").lines().toList()
        ));

        icons.put("worlds", new PropertyIcon(
            "worlds",
            editor.getMessage("worldsIconDescription").lines().toList(),
            editor.getMessage("worldsIconPossibleValues").lines().toList()
        ));

        // create text prompts

        TextInputButton maxUsesPrompt = new TextInputButton(
            editor.getMessage("maxUsesButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("maxUsesButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton priceMultiplierPrompt = new TextInputButton(
            editor.getMessage("priceMultiplierButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("priceMultiplierButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton villagerExperiencePrompt = new TextInputButton(
            editor.getMessage("villagerExperienceButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("villagerExperienceButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton giveExperienceToPlayerPrompt = new TextInputButton(
            editor.getMessage("giveExperienceButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("giveExperienceButtonPrompt")
            )
        );
        giveExperienceToPlayerPrompt.onResponse(response -> {
            try {

                if(
                    !response.toUpperCase().equals("TRUE") ||
                    !response.toUpperCase().equals("FALSE")
                ) {
                    throw new IllegalArgumentException(
                        editor.getMessage("giveExperienceIllegalArgumentException")
                    );
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton chancePrompt = new TextInputButton(
            editor.getMessage("chanceButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("chanceButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        // create add prompts

        TextInputButton addProfessionPrompt = new TextInputButton(
            Material.AXOLOTL_BUCKET,
            editor.getMessage("addProfessionButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("addProfessionButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton removeProfessionPrompt = new TextInputButton(
            Material.WATER_BUCKET,
            editor.getMessage("removeProfessionButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("removeProfessionButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton addLevelPrompt = new TextInputButton(
            Material.AXOLOTL_BUCKET,
            editor.getMessage("addLevelButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("addLevelButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton removeLevelPrompt = new TextInputButton(
            Material.WATER_BUCKET,
            editor.getMessage("removeLevelButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("removeLevelButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton addVillagerTypePrompt = new TextInputButton(
            Material.AXOLOTL_BUCKET,
            editor.getMessage("addVillagerTypeButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("addVillagerTypeButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton removeVillagerTypePrompt = new TextInputButton(
            Material.WATER_BUCKET,
            editor.getMessage("removeVillagerTypeButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("removeVillagerTypeButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton addBiomePrompt = new TextInputButton(
            Material.AXOLOTL_BUCKET,
            editor.getMessage("addBiomeButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("addBiomeButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton removeBiomePrompt = new TextInputButton(
            Material.WATER_BUCKET,
            editor.getMessage("removeBiomeButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("removeBiomeButtonPrompt")
            )
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
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton addWorldPrompt = new TextInputButton(
            Material.AXOLOTL_BUCKET,
            editor.getMessage("addWorldButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("addWorldButtonPrompt")
            )
        );
        addWorldPrompt.onResponse(response -> {
            try {
                this.tradeEntry.getUpdates().addWorld(response);

                icons.get("worlds").setCurrentValue(this.tradeEntry.getUpdates().getWorlds().toString());
                setIcon(Slot.WORLDS_ICON.index(), icons.get("worlds"));

                if(this.tradeEntry.isWorldsModified()) 
                    setIcon(Slot.WORLDS_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.WORLDS_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
        });

        TextInputButton removeWorldPrompt = new TextInputButton(
            Material.WATER_BUCKET,
            editor.getMessage("removeWorldButtonLabel"), 
            new PlayerPrompt(
                editor.getPlugin(), 
                editor.getMessage("removeWorldButtonPrompt")
            )
        );
        removeWorldPrompt.onResponse(response -> {
            try {
                this.tradeEntry.getUpdates().removeWorld(response);

                icons.get("worlds").setCurrentValue(this.tradeEntry.getUpdates().getWorlds().toString());
                setIcon(Slot.WORLDS_ICON.index(), icons.get("worlds"));

                if(this.tradeEntry.isWorldsModified()) 
                    setIcon(Slot.WORLDS_MODIFY_INDICATOR.index(), modifiedSlot);
                else setIcon(Slot.WORLDS_MODIFY_INDICATOR.index(), unmodifiedSlot);
            }
            catch(IllegalArgumentException exception) {
                editor.getPlayer().sendMessage(String.format(
                    ChatColor.RED + editor.getMessage("changeValueFailed"),
                    exception.getMessage()
                ));
            }  

            editor.openPage(this, editor.getPlayer());
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

        setButton(Slot.WORLDS_ADD_PROMPT.index(), "addWorldPrompt", addWorldPrompt);
        setButton(Slot.WORLDS_REMOVE_PROMPT.index(), "removeWorldPrompt", removeWorldPrompt);

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
        String worlds = Arrays.toString(trade.getWorlds().toArray());

        icons.get("maxUses").setCurrentValue(maxUses);
        icons.get("priceMultiplier").setCurrentValue(priceMultiplier);
        icons.get("villagerExperience").setCurrentValue(villagerExperience);
        icons.get("giveExperienceToPlayer").setCurrentValue(giveExperienceToPlayer);
        icons.get("chance").setCurrentValue(chance);

        icons.get("professions").setCurrentValue(professions);
        icons.get("levels").setCurrentValue(levels);
        icons.get("villagerTypes").setCurrentValue(villagerTypes);
        icons.get("biomes").setCurrentValue(biomes);
        icons.get("worlds").setCurrentValue(worlds);

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
        worlds = Arrays.toString(trade.getWorlds().toArray());

        icons.get("maxUses").setPreviousValue(maxUses);
        icons.get("priceMultiplier").setPreviousValue(priceMultiplier);
        icons.get("villagerExperience").setPreviousValue(villagerExperience);
        icons.get("giveExperienceToPlayer").setPreviousValue(giveExperienceToPlayer);
        icons.get("chance").setPreviousValue(chance);

        icons.get("professions").setPreviousValue(professions);
        icons.get("levels").setPreviousValue(levels);
        icons.get("villagerTypes").setPreviousValue(villagerTypes);
        icons.get("biomes").setPreviousValue(biomes);
        icons.get("worlds").setPreviousValue(worlds);

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
        setIcon(Slot.WORLDS_ICON.index(), icons.get("worlds"));

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
        updateIndicator(tradeEntry.isWorldsModified(), Slot.WORLDS_MODIFY_INDICATOR);

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
        WORLDS_ICON(42),
        
        MAX_USES_MODIFY_INDICATOR(0),
        PRICE_MULTIPLIER_MODIFY_INDICATOR(9),
        VILLAGER_EXPERIENCE_MODIFY_INDICATOR(18),
        GIVE_EXPERIENCE_TO_PLAYER_MODIFY_INDICATOR(27),
        CHANCE_MODIFY_INDICATOR(36),
        PROFESSIONS_MODIFY_INDICATOR(5),
        LEVELS_MODIFY_INDICATOR(14),
        VILLAGER_TYPES_MODIFY_INDICATOR(23),
        BIOMES_MODIFY_INDICATOR(32),
        WORLDS_MODIFY_INDICATOR(41),
        
        MAX_USES_PROMPT(3),
        PRICE_MULTIPLIER_PROMPT(12),
        EXPERIENCE_PROMPT(21),
        GIVE_EXPERIENCE_TO_PLAYER_PROMPT(30),
        CHANCE_PROMPT(39),
        
        PROFESSIONS_ADD_PROMPT(8),
        LEVELS_ADD_PROMPT(17),
        VILLAGER_TYPES_ADD_PROMPT(26),
        BIOMES_ADD_PROMPT(35),
        WORLDS_ADD_PROMPT(44),
        
        PROFESSIONS_REMOVE_PROMPT(7),
        LEVELS_REMOVE_PROMPT(16),
        VILLAGER_TYPES_REMOVE_PROMPT(25),
        BIOMES_REMOVE_PROMPT(34),
        WORLDS_REMOVE_PROMPT(43),
        
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