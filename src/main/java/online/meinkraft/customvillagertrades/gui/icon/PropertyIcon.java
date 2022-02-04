package online.meinkraft.customvillagertrades.gui.icon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;

public class PropertyIcon extends Icon {

    private List<String> description;
    private String previousValue;
    private String currentValue;
    private List<String> possibleValues;

    public PropertyIcon(
        Material material, 
        String label, 
        List<String> description,
        String previousValue,
        String currentValue, 
        List<String> possibleValues
    ) {

        super(material, ChatColor.GOLD + label);
        this.description = description;
        this.previousValue = previousValue;
        this.currentValue = currentValue;
        this.possibleValues = possibleValues;
        setLore();

    }

    public PropertyIcon(
        String label, 
        List<String> description,
        String previousValue,
        String currentValue, 
        List<String> possibleValues
    ) {

        this(
            Material.WRITABLE_BOOK, 
            label, 
            description, 
            previousValue, 
            currentValue, 
            possibleValues
        );

    }

    public PropertyIcon(String label, List<String> description, List<String> possibleValues) {
        this(label, description, null, null, possibleValues);
    }

    public PropertyIcon(String label, List<String> description) {
        this(label, description, null, null, null);
    }

    private void setLore() {

        List<String> lore = new ArrayList<>();
        for(String line : description) lore.add(line);

        lore.add("");

        lore.add(
            ChatColor.BLUE + "CURRENT VALUE: " + 
            ChatColor.GREEN + currentValue
        );

        lore.add(
            ChatColor.BLUE + "PREVIOUS VALUE: " + 
            ChatColor.YELLOW + previousValue
        );

        if(possibleValues != null && possibleValues.size() > 0) {

            lore.add(
                ChatColor.BLUE + "POSSIBLE VALUES: " + 
                ChatColor.LIGHT_PURPLE + possibleValues.get(0)
            );

            for(int index = 1; index < possibleValues.size(); index++) {
                lore.add(ChatColor.LIGHT_PURPLE + possibleValues.get(index));
            }

        }
        
        super.setLore(lore);

    }

    public void setDescription(List<String> description) {
        this.description = description;
        this.setLore();
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
        this.setLore();
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
        this.setLore();
    }

    public void setPossibleValues(List<String> possibleValues) {
        this.possibleValues = possibleValues;
        this.setLore();
    }
    
}

