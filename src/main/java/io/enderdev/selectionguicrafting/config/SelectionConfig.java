package io.enderdev.selectionguicrafting.config;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.Tags;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Tags.MOD_ID, name = Tags.MOD_ID, category = Tags.MOD_ID)
public class SelectionConfig {

    @Config.Name("Client")
    @Config.LangKey("config.selectionguicrafting.client")
    public static final Client CLIENT = new Client();

    public static class Client
    {
        @Config.Name("Disable Close GUI Button")
        @Config.Comment("Clientside config: Disables selection crafting GUIs from displaying the 'Close GUI' button, as you can use E and ESC to exit the GUI and the button could be clutter to some people.")
        public boolean disableCloseGUIbutton = false;
    }

    @Mod.EventBusSubscriber(modid = Tags.MOD_ID)
    public static class ConfigHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equalsIgnoreCase(Tags.MOD_ID))
            {
                ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
