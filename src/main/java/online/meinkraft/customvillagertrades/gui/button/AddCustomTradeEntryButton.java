package online.meinkraft.customvillagertrades.gui.button;

import org.bukkit.Material;

import online.meinkraft.customvillagertrades.gui.Editor;
import online.meinkraft.customvillagertrades.prompt.PlayerPrompt;

public class AddCustomTradeEntryButton extends TextInputButton {

    public AddCustomTradeEntryButton(Editor editor, PlayerPrompt prompt) {
        super(
            Material.NETHER_STAR, 
            editor.getMessage("addCustomTradeButtonLabel"), 
            null, 
            prompt
        );
    }
    
}
