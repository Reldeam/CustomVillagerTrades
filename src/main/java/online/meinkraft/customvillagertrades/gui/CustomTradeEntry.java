package online.meinkraft.customvillagertrades.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import online.meinkraft.customvillagertrades.gui.button.CustomTradeBlueprintButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeConfigButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeDeleteButton;
import online.meinkraft.customvillagertrades.gui.button.CustomTradeRenameButton;
import online.meinkraft.customvillagertrades.gui.button.DisabledSlot;
import online.meinkraft.customvillagertrades.trade.CustomTrade;

public class CustomTradeEntry {

    private final CustomTrade trade;

    private final CustomTradeRenameButton renameButton;
    private final CustomTradeConfigButton configButton;
    private final CustomTradeBlueprintButton blueprintButton;
    private final CustomTradeDeleteButton deleteButton;
    private final DisabledSlot disabledSlot;

    public CustomTradeEntry(CustomTrade trade) {
        this.trade = trade;
        this.renameButton = new CustomTradeRenameButton(trade);
        this.configButton = new CustomTradeConfigButton(trade);
        this.blueprintButton = new CustomTradeBlueprintButton(trade);
        this.deleteButton = new CustomTradeDeleteButton(trade);
        this.disabledSlot = new DisabledSlot();
    }

    public List<ItemStack> getItems() {

        return Arrays.asList(new ItemStack[] {
            renameButton.getItem(),
            disabledSlot.getItem(),
            trade.getFirstIngredient(),
            trade.getSecondIngredient(),
            trade.getResult(),
            disabledSlot.getItem(),
            configButton.getItem(),
            blueprintButton.getItem(),
            deleteButton.getItem()
        });

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
