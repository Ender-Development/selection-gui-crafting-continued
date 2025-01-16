package io.enderdev.selectionguicrafting.config;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = SelectionGuiCrafting.MOD_ID)
public class SelectionConfig {

    @Config.Comment("Clientside config: Disables selection crafting GUIs from displaying the 'Close GUI' button, as you can use E and ESC to exit the GUI and the button could be clutter to some people.")
    public static boolean disableCloseGUIbutton = false;

    public static class ConfigHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equalsIgnoreCase(SelectionGuiCrafting.MOD_ID))
            {
                ConfigManager.sync(SelectionGuiCrafting.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
