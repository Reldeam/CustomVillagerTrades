package online.meinkraft.customvillagertrades.gui;

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

import online.meinkraft.customvillagertrades.gui.button.Button;

public class Page implements Listener {

    private final GUI editor;
    private final Inventory inventory;
    private final int numRows;

    private Map<String, Button> buttons = new HashMap<>();
    
    public Page(GUI editor, String title, int rows) {

        this.editor = editor;

        if(rows < 1) rows = 1;
        else if(rows > 6) rows = 6;
        this.numRows = rows;

        inventory = Bukkit.createInventory(
            null, 
            getNumSlots(), 
            title
        );

    }

    public Page(GUI editor, String title) {
        this(editor, title, 6);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if(event.getInventory().equals(inventory)) {

            ItemStack item = event.getCurrentItem();
            if(item == null) return;

            ItemMeta itemMeta = (ItemMeta) item.getItemMeta();
            if(itemMeta == null) return;

            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            String buttonId = data.get(editor.getButtonKey(), PersistentDataType.STRING);
            if(buttonId == null) return;

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

    public void addButton(int index, String buttonId, Button button) {

        button.setKey(editor.getButtonKey(), buttonId);
        inventory.setItem(index, button.getItem());

        buttons.put(buttonId, button);

    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public GUI getEditor() {
        return editor;
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
