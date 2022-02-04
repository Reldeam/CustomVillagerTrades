package online.meinkraft.customvillagertrades.prompt;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class PlayerPrompt implements Listener {

    private final JavaPlugin plugin;
    private PromptListener callback;
    private Player player;
    private String prompt;

    public PlayerPrompt(JavaPlugin plugin, String prompt) {
        this.plugin = plugin;
        this.prompt = prompt;
    }

    public void promptPlayer(Player player, PromptListener callback) {
        this.player = player;
        this.callback = callback;

        TextComponent prefixComponent = new TextComponent("[" + plugin.getName() + "] ");
        prefixComponent.setColor(ChatColor.YELLOW);

        TextComponent promptComponent = new TextComponent(prompt + ", or type ");
        promptComponent.setColor(ChatColor.GREEN);

        TextComponent cancelComponent = new TextComponent("[CANCEL]");
        cancelComponent.setColor(ChatColor.RED);
        cancelComponent.setBold(true);
        cancelComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "CANCEL"));
        cancelComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to cancel")));
        

        BaseComponent[] component = new ComponentBuilder(prefixComponent)
        .append(promptComponent).append(cancelComponent)
        .append(":").color(ChatColor.GREEN).create();

        player.spigot().sendMessage(component);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    void onPlayerChatEvent(AsyncPlayerChatEvent event) {

        if(event.getPlayer().equals(player)) {
            if(!event.getMessage().toUpperCase().equals("CANCEL")) {
                callback.onPlayerInput(event.getMessage());
            }
            else {
                player.sendMessage(ChatColor.GRAY + "Action canceled");
            }
            event.setCancelled(true);
            HandlerList.unregisterAll(this);
        }

    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
    
}
