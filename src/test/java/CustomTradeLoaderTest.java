import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;
//import online.meinkraft.customvillagertrades.PluginConfig;
import online.meinkraft.customvillagertrades.trade.CustomTradeLoader;

public class CustomTradeLoaderTest {

    //private static ServerMock server;
    private static CustomVillagerTrades plugin;
    //private static PluginConfig config;

    @BeforeAll
    public static void setUp()
    {
        //server = MockBukkit.mock();
        MockBukkit.mock();
        plugin = (CustomVillagerTrades) MockBukkit.load(CustomVillagerTrades.class);
    }

    @AfterAll
    public static void tearDown()
    {
        MockBukkit.unmock();
    }

    @Test
    void instantiateLoader() {

        CustomTradeLoader.loadTrades(plugin);
        
    }
    
}
