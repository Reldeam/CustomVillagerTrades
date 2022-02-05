package online.meinkraft.customvillagertrades.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import online.meinkraft.customvillagertrades.gui.page.Page;

public class GUI implements Listener {

    private JavaPlugin plugin;
    private final NamespacedKey buttonKey;
    private final NamespacedKey iconKey;

    private final Map<String, Page> pageMap = new HashMap<>();
    private final List<Page> pageList = new ArrayList<Page>();
    
    private Page currentPage = null;

    private boolean isOpen = false;
    private boolean closePerminently = true;
    private Player player = null;

    public GUI(JavaPlugin plugin) {

        this.plugin = plugin;
        buttonKey = new NamespacedKey(plugin, "gui-label");
        iconKey = new NamespacedKey(plugin, "gui-icon");
        
    }

    public void addPage(String key, Page page) {
        pageMap.put(key, page);
        pageList.add(page);
        plugin.getServer().getPluginManager().registerEvents(page, plugin);
    }

    public void addPage(int index, String key, Page page) {
        pageMap.put(key, page);
        pageList.add(index, page);
        plugin.getServer().getPluginManager().registerEvents(page, plugin);
    }

    public boolean removePage(String key) {
        Page page = pageMap.get(key);
        if(page != null && !page.equals(currentPage)) {
            pageMap.remove(key);
            pageList.remove(page);
            HandlerList.unregisterAll(page);
            return true;
        }
        return false;
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean open(Player player) {
        if(pageList.size() == 0) return false;
        currentPage = pageList.get(0);
        currentPage.open(player);
        isOpen = true;
        closePerminently = true;
        this.player = player;
        return true;
    }

    public boolean openPage(Page page, Player player) {
        if(!pageList.contains(page)) return false;
        currentPage = page;
        this.player = player;
        isOpen = true;
        closePerminently = true;
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {

            @Override
            public void run() {
                currentPage.open(player);

            }

        }); 
        return true;
    }

    public boolean openPage(String key, Player player) {
        Page page = pageMap.get(key);
        if(page != null) return openPage(page, player);
        return false;
    }

    public boolean close(boolean perminently) {
        if(!isOpen()) return false;
        closePerminently = perminently;
        player.closeInventory();
        isOpen = false;
        return true;
    }

    public boolean close() {
        return close(false);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        // clean up event handlers if GUI is being closed for the last time
        if(
            event.getInventory().equals(currentPage.getInventory()) &&
            closePerminently
        ) {
            for(Page page : pageList) HandlerList.unregisterAll(page);
        }

    }

    public boolean nextPage(Player player) {
        if(currentPage == null) return false;
        int index = pageList.indexOf(currentPage);
        if(index < 0 || index >= pageList.size() - 1) return false;
        currentPage = pageList.get(index + 1);
        currentPage.open(player);
        return true;
    }

    public boolean prevPage(Player player) {
        if(currentPage == null) return false;
        int index = pageList.indexOf(currentPage);
        if(index < 1) return false;
        currentPage = pageList.get(index - 1);
        currentPage.open(player);
        return true;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public NamespacedKey getButtonKey() {
        return buttonKey;
    }

    public NamespacedKey getIconKey() {
        return iconKey;
    }
    
}
