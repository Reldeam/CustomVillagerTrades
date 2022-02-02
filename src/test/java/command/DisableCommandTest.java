package command;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import online.meinkraft.customvillagertrades.CustomVillagerTrades;

public class DisableCommandTest {

    private static ServerMock server;
    private static CustomVillagerTrades plugin;

    @BeforeAll
    public static void setUp()
    {
        server = MockBukkit.mock();
        plugin = (CustomVillagerTrades) MockBukkit.load(CustomVillagerTrades.class);
    }

    @AfterAll
    public static void tearDown()
    {
        MockBukkit.unmock();
    }

    @Test
    void testDisableCommand() {
        
        server.getPluginManager().enablePlugin(plugin);
        server.getConsoleSender();

        /*
        Command command = mock(Command.class);

        DisableCommand disableCommand = new DisableCommand(plugin);
        disableCommand.onCommand(
            (CommandSender) server.getConsoleSender(), 
            command, 
            "", 
            new String[]{}
        );
        */

    }
    
}
