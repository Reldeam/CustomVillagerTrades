package online.meinkraft.customvillagertrades.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import online.meinkraft.customvillagertrades.gui.button.CustomTradeBlueprintButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeConfigButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeDeleteButton;
import online.meinkraft.customvillagertrades.gui.icon.BookIcon;
import online.meinkraft.customvillagertrades.gui.page.TradeConfigPage;
import online.meinkraft.customvillagertrades.gui.page.TradeListPage;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class CustomTradeEntry {

    private final int COLUMNS_PER_ROW = 9;

    private final int row;
    private final CustomTrade trade;
    private final CustomTrade updates;
    private final TradeListPage tradeListPage;
    private final TradeConfigPage tradeConfigPage;

    private final BookIcon keyIcon;

    private final CustomTradeConfigButton configButton;
    private final CustomTradeBlueprintButton blueprintButton;
    private final CustomTradeDeleteButton deleteButton;

    private final boolean isNew;

    private boolean isDeleted = false;

    public CustomTradeEntry(
        int row, 
        TradeListPage tradeListPage, 
        TradeConfigPage tradeConfigPage,
        CustomTrade trade,
        boolean isNew
    ) {

        this.row = row;
        this.tradeListPage = tradeListPage;
        this.tradeConfigPage = tradeConfigPage;

        this.isNew = isNew;

        this.trade = trade;
        updates = trade.clone();

        this.keyIcon = new BookIcon(trade.getKey());

        this.configButton = new CustomTradeConfigButton(tradeConfigPage, this);
        this.blueprintButton = new CustomTradeBlueprintButton(this);
        this.deleteButton = new CustomTradeDeleteButton(this);

    }

    public void updateItems() {
        Inventory inventory = tradeListPage.getInventory();
        int index = row * COLUMNS_PER_ROW;

        ItemStack firstIngredient = inventory.getItem(index + 2);
        ItemStack secondIngredient = inventory.getItem(index + 3);
        ItemStack result = inventory.getItem(index + 4);

        
        // first ingredient
        if(isItemStackModified(firstIngredient, getFirstIngredient())) {
            updates.setFirstIngredient(firstIngredient);
        }

        // second ingredient
        if(isItemStackModified(secondIngredient, getSecondIngredient())) {
            updates.setSecondIngredient(secondIngredient);
        }

        // result
        if(isItemStackModified(result, getResult())) {
            updates.setResult(result);
        }

    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isModified() {

        if(
            isItemsModified() ||
            isMaxUsesModified() ||
            isPriceMultiplierModified() ||
            isVillagerExperienceModified() ||
            isGiveExperienceToPlayerModified() ||
            isChanceModified() ||
            isProfessionsModified() ||
            isLevelsModified() ||
            isVillagerTypesModified() ||
            isBiomesModified() ||
            isWorldsModified()
        ) {
            return true;
        }

        return false;

    }

    public boolean isItemsModified() {
        return (
            isItemStackModified(trade.getFirstIngredient(), updates.getFirstIngredient()) ||
            isItemStackModified(trade.getSecondIngredient(), updates.getSecondIngredient()) ||
            isItemStackModified(trade.getResult(), updates.getResult())
        );
    }

    public boolean isItemStackModified(ItemStack oldItemStack, ItemStack newItemStack) {

        if(
            (oldItemStack == null && newItemStack != null) ||
            (oldItemStack != null && newItemStack == null) ||
            (oldItemStack != null && !oldItemStack.equals(newItemStack))
        ) return true;

        return false;
    }

    public boolean isMaxUsesModified() {
        return !trade.getMaxUses().equals(updates.getMaxUses());
    }

    public boolean isPriceMultiplierModified() {
        return !trade.getPriceMultiplier().equals(updates.getPriceMultiplier());
    }

    public boolean isVillagerExperienceModified() {
        return !trade.getVillagerExperience().equals(updates.getVillagerExperience());
    }

    public boolean isGiveExperienceToPlayerModified() {
        return !trade.giveExperienceToPlayer().equals(updates.giveExperienceToPlayer());
    }

    public boolean isChanceModified() {
        return !trade.getChance().equals(updates.getChance());
    }

    public boolean isProfessionsModified() {
        return !trade.getProfessions().equals(updates.getProfessions());
    }

    public boolean isLevelsModified() {
        return !trade.getLevels().equals(updates.getLevels());
    }

    public boolean isVillagerTypesModified() {
        return !trade.getVillagerTypes().equals(updates.getVillagerTypes());
    }

    public boolean isBiomesModified() {
        return !trade.getBiomes().equals(updates.getBiomes());
    }

    public boolean isWorldsModified() {
        return !trade.getWorlds().equals(updates.getWorlds());
    }

    public void delete() {
        isDeleted = true;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public int getRow() {
        return row;
    }

    public TradeListPage getTradeListPage() {
        return tradeListPage;
    }

    public TradeConfigPage gTradeConfigPage() {
        return tradeConfigPage;
    }

    public CustomTrade getTrade() {
        return trade;
    }

    public CustomTrade getUpdates() {
        return updates;
    }

    public ItemStack getFirstIngredient() {
        return updates.getFirstIngredient();
    }

    public ItemStack getSecondIngredient() {
        return updates.getSecondIngredient();
    }

    public ItemStack getResult() {
        return updates.getResult();
    }

    public BookIcon getKeyIcon() {
        return keyIcon;
    }

    public CustomTradeConfigButton getConfigButton() {
        return configButton;
    }

    public CustomTradeBlueprintButton getBlueprintButton() {
        return blueprintButton;
    }

    public CustomTradeDeleteButton getDeleteButton() {
        return deleteButton;
    }

}
