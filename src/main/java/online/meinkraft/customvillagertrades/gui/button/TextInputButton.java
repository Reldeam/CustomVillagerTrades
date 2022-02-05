package online.meinkraft.customvillagertrades.gui.button;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;

import online.meinkraft.customvillagertrades.gui.page.Page;
import online.meinkraft.customvillagertrades.prompt.PlayerPrompt;
import online.meinkraft.customvillagertrades.prompt.PromptListener;

public class TextInputButton extends Button {

    private final PlayerPrompt prompt;
    private PromptListener listener;

    public TextInputButton(
        Material material, 
        String label, 
        List<String> lore, 
        PlayerPrompt prompt
    ) {
        super(material, label, lore);
        this.prompt = prompt;
        this.listener = null;
    }

    public TextInputButton(
        Material material, 
        String label, 
        PlayerPrompt prompt
    ) {
        this(material, label, null, prompt);
    }

    public TextInputButton(
        String label, 
        PlayerPrompt prompt
    ) {
        this(Material.MOJANG_BANNER_PATTERN, label, null, prompt);
    }

    public void onResponse(PromptListener listener) {
        this.listener = listener;
    }

    @Override
    public Result onClick(Page page, InventoryClickEvent event) {

        if(listener != null) {
            page.getGUI().close();
            prompt.promptPlayer((Player) event.getWhoClicked(), listener);
        }

        return Result.DENY;

    }
    
}
