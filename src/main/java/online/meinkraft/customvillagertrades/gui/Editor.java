package online.meinkraft.customvillagertrades.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Villager;

import net.md_5.bungee.api.ChatColor;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.exception.CustomTradeKeyAlreadyExistsException;
import online.meinkraft.customvillagertrades.exception.IngredientsNotFoundException;
import online.meinkraft.customvillagertrades.exception.ResultNotFoundException;
import online.meinkraft.customvillagertrades.gui.button.AddCustomTradeEntryButton;
import online.meinkraft.customvillagertrades.gui.button.EditorCancelButton;
import online.meinkraft.customvillagertrades.gui.button.EditorSaveButton;
import online.meinkraft.customvillagertrades.gui.button.MoneyButton;
import online.meinkraft.customvillagertrades.gui.page.TradeConfigPage;
import online.meinkraft.customvillagertrades.gui.page.TradeListPage;
import online.meinkraft.customvillagertrades.prompt.PlayerPrompt;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class Editor extends GUI {

    private static final int TRADES_PER_PAGE = 5;

    private List<TradeListPage> tradeListPages;
    private TradeConfigPage configPage;

    private final EditorSaveButton saveButton;
    private final EditorCancelButton cancelButton;
    private final MoneyButton moneyButton;
    private final AddCustomTradeEntryButton newButton;

    public Editor(CustomVillagerTrades plugin) {

        super(plugin);

        tradeListPages = new ArrayList<>();
        configPage = new TradeConfigPage(this, "CVT Editor - Configuration");

        saveButton = new EditorSaveButton(this);
        cancelButton = new EditorCancelButton(this);

        // create add trade button
        newButton = new AddCustomTradeEntryButton(
            new PlayerPrompt(this.getPlugin(), "Enter a unique name for this custom trade")
        );

        newButton.onResponse(response -> {
            TradeListPage page = addNewCustomTradeEntry(response);
            openPage(page, getPlayer());                                                                                                           
        });

        // create money button
        if(plugin.isEconomyEnabled()) {
            moneyButton = new MoneyButton(
                plugin,
                plugin.getCurrencyMaterial(), 
                plugin.getCurrencyPrefix(),
                plugin.getCurrencySuffix()
            );
        }
        else {
            moneyButton = null;
        }
        
        // create custom trade list pages
        List<CustomTrade> customTrades = plugin.getCustomTradeManager().getCustomTrades();
        int totalPages = (int) Math.ceil((double) customTrades.size() / TRADES_PER_PAGE);
        int index = 0;

        // make an empty trade list page if there are no custom trades
        if(totalPages < 1) totalPages = 1;

        for(int pageIndex = 0; pageIndex < totalPages; pageIndex++) {

            TradeListPage page = new TradeListPage(
                this, 
                "CVT Editor - Custom Trades",
                pageIndex,
                totalPages,
                newButton,
                saveButton,
                cancelButton,
                moneyButton
            );

            for(int rowIndex = 0; rowIndex < TRADES_PER_PAGE; rowIndex++) {
                if(index >= customTrades.size()) break;
                page.addCustomTrade(customTrades.get(index), configPage);
                index++;
            }

            addTradeListPage(page);

        }

        addPage("config", configPage);

    }

    public TradeListPage addNewCustomTradeEntry(String key) {

        int lastTradeListPageIndex = tradeListPages.size() - 1;
        TradeListPage lastTradeListPage = tradeListPages.get(lastTradeListPageIndex);
        int emptyRow = lastTradeListPage.getCustomTradeEntries().size();

        try {

            for(TradeListPage page : tradeListPages) {
                for(CustomTradeEntry entry : page.getCustomTradeEntries()) {
                    if(entry.getTrade().getKey().equals(key)) {
                        throw new CustomTradeKeyAlreadyExistsException();
                    }
                }
            }

            CustomTrade newTrade = new CustomTrade(
                key, 
                null, 
                null, 
                null, 
                0, 
                (double) 0, 
                0, 
                true, 
                (double) 0, 
                null, 
                null, 
                null, 
                null,
                null
            );

            if(emptyRow >= TRADES_PER_PAGE) {
                lastTradeListPage = appendNewTradeListPage();
                emptyRow = 0;
            }

            lastTradeListPage.addCustomTrade(newTrade, configPage, true);

        }
        catch(Exception exception) {
            getPlayer().sendMessage(
                ChatColor.RED +
                "Failed to create new custom trade: " +
                exception.getMessage()
            );
        }

        return lastTradeListPage;

    }

    public TradeListPage appendNewTradeListPage() {

        int newTradeListPageIndex = tradeListPages.size();
        int newTotalPages = newTradeListPageIndex + 1;

        TradeListPage newPage = new TradeListPage(
            this, 
            "CVT Editor - Custom Trades",
            newTradeListPageIndex,
            newTotalPages,
            newButton,
            saveButton,
            cancelButton,
            moneyButton
        );

        addPage(
            newTradeListPageIndex, 
            "tradeList" + newTradeListPageIndex, 
            newPage
        );

        tradeListPages.add(newPage);

        // update totalPages
        for(TradeListPage page : tradeListPages) {
            page.setTotalPages(tradeListPages.size());
        }

        return newPage;

    }

    public void addTradeListPage(TradeListPage page) {
        int pageNum = tradeListPages.size();
        addPage("tradeList" + pageNum, page);
        tradeListPages.add(page);
    }

    public void save() {

        getPlugin().getLogger().info(
            getPlayer().getName() + 
            " has made changes using the in-game editor"
        );

        for(TradeListPage page : tradeListPages) {
            for(CustomTradeEntry entry : page.getCustomTradeEntries()) {

                FileConfiguration config = getPlugin().getTradesConfig();
                CustomTrade oldTrade = entry.getTrade();
                CustomTrade newTrade = entry.getUpdates();

                // entry is deleted
                if(entry.isDeleted()) {
                    config.set(oldTrade.getKey(), null);
                    continue;
                }

                // create editor entry
                if(entry.isModified() || entry.isNew()) {

                    String tradeKey = newTrade.getKey();

                    try {

                        if(newTrade.getResult() == null) {
                            throw new ResultNotFoundException();
                        }

                        if(
                            newTrade.getFirstIngredient() == null &&
                            newTrade.getSecondIngredient() == null 
                        ) {
                            throw new IngredientsNotFoundException();
                        }
                    
                    }
                    catch(IngredientsNotFoundException | ResultNotFoundException exception) {
                        getPlayer().sendMessage(
                            ChatColor.YELLOW + "Failed to save custom trade " +
                            ChatColor.AQUA + tradeKey +
                            ChatColor.YELLOW + ": " +
                            ChatColor.RED + exception.getMessage()
                        );
                        continue;
                    }

                    config.set(tradeKey + ".maxUses", newTrade.getMaxUses());
                    config.set(tradeKey + ".priceMultiplier", newTrade.getPriceMultiplier());
                    config.set(tradeKey + ".experience", newTrade.getVillagerExperience());
                    config.set(tradeKey + ".giveExperienceToPlayer", newTrade.giveExperienceToPlayer());
                    config.set(tradeKey + ".chance", newTrade.getChance());

                    List<String> professions = newTrade.getProfessions().stream()
                    .map(Villager.Profession::name).collect(Collectors.toList());

                    List<String> villagerTypes = newTrade.getVillagerTypes().stream()
                    .map(Villager.Type::name).collect(Collectors.toList());

                    List<String> biomes = newTrade.getBiomes().stream()
                    .map(Biome::name).collect(Collectors.toList());

                    config.set(tradeKey + ".professions", professions);
                    config.set(tradeKey + ".levels", newTrade.getLevels());
                    config.set(tradeKey + ".villagerTypes", villagerTypes);
                    config.set(tradeKey + ".biomes", biomes);
                    config.set(tradeKey + ".worlds", newTrade.getWorlds());

                    if(entry.isItemsModified()) {
                        String editorKey = tradeKey + ".editor";
                        if(!config.contains(editorKey)) config.createSection(editorKey);
                        config.set(editorKey + ".firstIngredient", newTrade.getFirstIngredient());
                        config.set(editorKey + ".secondIngredient", newTrade.getSecondIngredient());
                        config.set(editorKey + ".result", newTrade.getResult());
                    }

                }

            }
        }

        getPlugin().saveTradesConfig();
        getPlugin().getCustomTradeManager().load();

        getPlayer().sendMessage(
            ChatColor.GREEN + 
            "Custom trades have been updated"
        );

    }

    @Override
    public CustomVillagerTrades getPlugin() {
        return (CustomVillagerTrades) super.getPlugin();
    }
    
}
