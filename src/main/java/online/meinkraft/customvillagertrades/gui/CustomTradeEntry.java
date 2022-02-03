package online.meinkraft.customvillagertrades.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import online.meinkraft.customvillagertrades.gui.button.CustomTradeBlueprintButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeConfigButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeDeleteButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeRenameButton;
import online.meinkraft.customvillagertrades.gui.page.TradeListPage;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class CustomTradeEntry {

    private final int COLUMNS_PER_ROW = 9;

    private final int row;
    private final CustomTrade trade;
    private final CustomTrade updates;
    private final TradeListPage page;

    private final CustomTradeRenameButton renameButton;
    private final CustomTradeConfigButton configButton;
    private final CustomTradeBlueprintButton blueprintButton;
    private final CustomTradeDeleteButton deleteButton;

    private boolean isDeleted = false;

    public CustomTradeEntry(int row, TradeListPage page, CustomTrade trade) {

        this.row = row;
        this.page = page;

        this.trade = trade;
        updates = trade.clone();

        this.renameButton = new CustomTradeRenameButton(this);
        this.configButton = new CustomTradeConfigButton(this);
        this.blueprintButton = new CustomTradeBlueprintButton(this);
        this.deleteButton = new CustomTradeDeleteButton(this);

    }

    public boolean isModified() {

        Inventory inventory = page.getInventory();
        int index = row * COLUMNS_PER_ROW;

        ItemStack firstIngredient = inventory.getItem(index + 2);
        ItemStack secondIngredient = inventory.getItem(index + 3);
        ItemStack result = inventory.getItem(index + 4);

        if(
            (firstIngredient == null && getFirstIngredient() != null) ||
            (firstIngredient != null && getFirstIngredient() == null) ||
            (firstIngredient != null && !firstIngredient.equals(getFirstIngredient()))
        ) return true;

        if(
            (secondIngredient == null && getSecondIngredient() != null) ||
            (secondIngredient != null && getSecondIngredient() == null) ||
            (secondIngredient != null && !secondIngredient.equals(getSecondIngredient()))
        ) return true;

        if(
            (result == null && getResult() != null) ||
            (result != null && getResult() == null) ||
            (result != null && !result.equals(getResult()))
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
        return trade.getFirstIngredient();
    }

    public ItemStack getSecondIngredient() {
        return trade.getSecondIngredient();
    }

    public ItemStack getResult() {
        return trade.getResult();
    }

    public CustomTradeRenameButton getRenameButton() {
        return renameButton;
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
