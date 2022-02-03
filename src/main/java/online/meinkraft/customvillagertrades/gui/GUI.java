package online.meinkraft.customvillagertrades.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class GUI {

    private JavaPlugin plugin;
    private final NamespacedKey buttonKey;

    private final Map<String, Page> pageMap = new HashMap<>();
    private final List<Page> pageList = new ArrayList<Page>();
    
    private Page currentPage = null;

    public GUI(JavaPlugin plugin) {

        this.plugin = plugin;
        buttonKey = new NamespacedKey(plugin, "label");
        
    }

    public void addPage(String key, Page page) {
        pageMap.put(key, page);
        pageList.add(page);
        plugin.getServer().getPluginManager().registerEvents(page, plugin);
    }

    public void addPage(int index, String key, Page page) {
        pageMap.put(key, page);
        pageList.add(index, page);
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

    public boolean open(Player player) {
        if(pageList.size() == 0) return false;
        currentPage = pageList.get(0);
        currentPage.open(player);
        return true;
    }

    public boolean openPage(String key, Player player) {
        Page page = pageMap.get(key);
        if(page == null) return false;
        currentPage = page;
        currentPage.open(player);
        return true;
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
    
}
