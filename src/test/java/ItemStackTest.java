import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;

public class ItemStackTest {

    @BeforeAll
    public static void setUp()
    {
        MockBukkit.mock();
    }

    @AfterAll
    public static void tearDown()
    {
        MockBukkit.unmock();
    }

    @Test
    void createNewItemStack() {
        
        ItemStack stack = new ItemStack(Material.APPLE);
        assertNotNull(stack);

    }

    @Test
    void setMetaData() {

        ItemStack stack = new ItemStack(Material.APPLE);
        assertNotNull(stack);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("Orange");
        stack.setItemMeta(meta);

    }
    
    
}