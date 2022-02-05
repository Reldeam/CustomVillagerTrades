package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;

import online.meinkraft.customvillagertrades.prompt.PlayerPrompt;

public class AddCustomTradeEntryButton extends TextInputButton {

    public AddCustomTradeEntryButton(PlayerPrompt prompt) {
        super(Material.NETHER_STAR, "Add New Custom Trade", null, prompt);
    }
    
}
