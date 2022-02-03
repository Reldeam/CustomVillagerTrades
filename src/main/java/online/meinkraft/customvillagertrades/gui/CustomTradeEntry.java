package online.meinkraft.customvillagertrades.gui;

import org.bukkit.inventory.ItemStack;

import online.meinkraft.customvillagertrades.gui.button.CustomTradeBlueprintButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeConfigButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeDeleteButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeRenameButton;
import online.meinkraft.customvillagertrades.gui.page.TradeListPage;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class CustomTradeEntry {

    private final CustomTrade trade;
    private final TradeListPage page;

    private final CustomTradeRenameButton renameButton;
    private final CustomTradeConfigButton configButton;
    private final CustomTradeBlueprintButton blueprintButton;
    private final CustomTradeDeleteButton deleteButton;

    public CustomTradeEntry(TradeListPage page, CustomTrade trade) {
        this.page = page;
        this.trade = trade;
        this.renameButton = new CustomTradeRenameButton(this);
        this.configButton = new CustomTradeConfigButton(this);
        this.blueprintButton = new CustomTradeBlueprintButton(this);
        this.deleteButton = new CustomTradeDeleteButton(this);
    }

    public TradeListPage getPage() {
        return page;
    }

    public CustomTrade getTrade() {
        return trade;
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
