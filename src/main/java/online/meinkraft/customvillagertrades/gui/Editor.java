package online.meinkraft.customvillagertrades.gui;

import org.bukkit.plugin.java.JavaPlugin;

import online.meinkraft.customvillagertrades.gui.button.NextPageButton;
import online.meinkraft.customvillagertrades.gui.button.PrevPageButton;

public class Editor extends GUI {

    public Editor(JavaPlugin plugin) {
        super(plugin);

        Page testPage1 = new Page(this, "Test Page 1");
        testPage1.addButton(45, "prevPage", new PrevPageButton());
        testPage1.addButton(53, "nextPage", new NextPageButton());

        Page testPage2 = new Page(this, "Test Page 2");
        testPage2.addButton(45, "prevPage", new PrevPageButton());
        testPage2.addButton(53, "nextPage", new NextPageButton());

        addPage("test", testPage1);
        addPage("test", testPage2);

    }
    
}
