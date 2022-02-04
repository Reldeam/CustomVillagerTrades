package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;
import online.meinkraft.customvillagertrades.exception.EconomyNotEnabledException;
import online.meinkraft.customvillagertrades.gui.page.Page;
import online.meinkraft.customvillagertrades.prompt.PlayerPrompt;
import online.meinkraft.customvillagertrades.prompt.PromptListener;
import online.meinkraft.customvillagertrades.util.MoneyItem;

public class MoneyButton extends Button implements PromptListener {

    private final CustomVillagerTrades plugin;
    private Page page;
    private Player player;

    public MoneyButton(
        CustomVillagerTrades plugin,
        Material material, 
        String currencyPrefix, 
        String currencySuffix
    ) {
        super(material, "Create Money");
        this.plugin = plugin;
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {

        this.page = page;
        this.player = (Player) event.getWhoClicked();

        PlayerPrompt prompt = new PlayerPrompt(
            plugin, 
            ChatColor.GREEN + "Enter an amount of money"
        );

        page.getGUI().close();
        prompt.promptPlayer(player, this);

        return Result.DENY;

    }

    @Override
    public void onPlayerInput(String input) {

        try {

            double amount = Double.parseDouble(input);
            ItemStack money = MoneyItem.create(plugin, amount);
            player.getInventory().addItem(money);

        }
        catch(NumberFormatException exception) {
            player.sendMessage(ChatColor.RED + "Invalid Input - The amount must be a number");
        } catch (EconomyNotEnabledException e) {
            player.sendMessage(ChatColor.RED + "Economy is not enabled");
        }

        page.getGUI().openPage(page, player);

    }
    
}
