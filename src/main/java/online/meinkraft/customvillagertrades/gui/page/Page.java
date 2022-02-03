package online.meinkraft.customvillagertrades.gui.page;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import online.meinkraft.customvillagertrades.gui.GUI;
import online.meinkraft.customvillagertrades.gui.button.Button;
import online.meinkraft.customvillagertrades.gui.icon.Icon;

public class Page implements Listener {

    private final GUI gui;
    private final Inventory inventory;
    private final int numRows;

    private Map<String, Button> buttons = new HashMap<>();
    
    public Page(GUI gui, String title, int rows) {

        this.gui = gui;

        if(rows < 1) rows = 1;
        else if(rows > 6) rows = 6;
        this.numRows = rows;

        inventory = Bukkit.createInventory(
            null, 
            getNumSlots(), 
            title
        );

    }

    public Page(GUI gui, String title) {
        this(gui, title, 6);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if(event.getInventory().equals(inventory)) {

            ItemStack item = event.getCurrentItem();
            if(item == null) return;

            ItemMeta itemMeta = (ItemMeta) item.getItemMeta();
            if(itemMeta == null) return;

            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            boolean isIcon = data.get(gui.getIconKey(), PersistentDataType.BYTE) != null;
            String buttonId = data.get(gui.getButtonKey(), PersistentDataType.STRING);
            
            if(isIcon) {
                event.setCancelled(true);
            }
            else if(buttonId != null) {
                Button button = buttons.get(buttonId);
                if(button == null) {
                    event.setCancelled(true);
                    return;
                }
                else {
                    Result result = button.onClick(this, event);
                    event.setResult(result);
                }
            }
               
        }
        
    }

    public void addButton(int index, String buttonId, Button button) {

        button.setKey(gui.getButtonKey(), buttonId);
        inventory.setItem(index, button.getItem());

        buttons.put(buttonId, button);

    }

    public void addIcon(int index, Icon icon) {

        ItemStack item = icon.getItem();
        ItemMeta itemMeta = (ItemMeta) item.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(gui.getIconKey(), PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(itemMeta);

        inventory.setItem(index, icon.getItem());

    }

    public void addItemStack(int index, ItemStack itemStack) {
        inventory.setItem(index, itemStack);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public GUI getGUI() {
        return gui;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumSlots() {
        return 9 * numRows;
    }

}
