package online.meinkraft.customvillagertrades.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import online.meinkraft.customvillagertrades.gui.button.CustomTradeBlueprintButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeConfigButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeDeleteButton;
import online.meinkraft.customvillagertrades.gui.icon.CustomTradeEntryKeyIcon;
import online.meinkraft.customvillagertrades.gui.page.TradeListPage;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class CustomTradeEntry {

    private final int COLUMNS_PER_ROW = 9;

    private final int row;
    private final CustomTrade trade;
    private final CustomTrade updates;
    private final TradeListPage page;

    private final CustomTradeEntryKeyIcon keyIcon;

    private final CustomTradeConfigButton configButton;
    private final CustomTradeBlueprintButton blueprintButton;
    private final CustomTradeDeleteButton deleteButton;

    private boolean isDeleted = false;

    public CustomTradeEntry(int row, TradeListPage page, CustomTrade trade) {

        this.row = row;
        this.page = page;

        this.trade = trade;
        updates = trade.clone();

        this.keyIcon = new CustomTradeEntryKeyIcon(trade.getKey());

        this.configButton = new CustomTradeConfigButton(this);
        this.blueprintButton = new CustomTradeBlueprintButton(this);
        this.deleteButton = new CustomTradeDeleteButton(this);

    }

    public void updateItems() {
        Inventory inventory = page.getInventory();
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

    public boolean isModified() {

        if(
            isItemStackModified(trade.getFirstIngredient(), updates.getFirstIngredient()) ||
            isItemStackModified(trade.getSecondIngredient(), updates.getSecondIngredient()) ||
            isItemStackModified(trade.getResult(), updates.getResult())
        ) {
            return true;
        }

        return false;

    }

    public boolean isItemStackModified(ItemStack oldItemStack, ItemStack newItemStack) {

        if(
            (oldItemStack == null && newItemStack != null) ||
            (oldItemStack != null && newItemStack == null) ||
            (oldItemStack != null && !oldItemStack.equals(newItemStack))
        ) return true;

        return false;
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

    public TradeListPage getPage() {
        return page;
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

    public CustomTradeEntryKeyIcon getKeyIcon() {
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
