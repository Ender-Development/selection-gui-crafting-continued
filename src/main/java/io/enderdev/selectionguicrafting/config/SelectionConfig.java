package io.enderdev.selectionguicrafting.config;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.Tags;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Tags.MOD_ID, name = Tags.MOD_ID, category = "")
public class SelectionConfig {

    @Config.Name("Client")
    @Config.LangKey("config.selectionguicrafting.client")
    public static final Client CLIENT = new Client();

    public static class Client {
        @Config.Name("Disable Close GUI Button")
        @Config.Comment("Clientside config: Disables selection crafting GUIs from displaying the 'Close GUI' button, as you can use E and ESC to exit the GUI and the button could be clutter to some people.")
        public boolean disableCloseGUIbutton = false;
    }

    @Config.Name("General")
    @Config.LangKey("config.selectionguicrafting.general")
    public static final General GENERAL = new General();

    public static class General {
        @Config.RequiresMcRestart
        @Config.Name("Blacklist Block Classes")
        @Config.Comment("Block classes that should be ignored when interacting with them.")
        public String[] blacklistClasses = new String[]{
                "net.minecraft.block.BlockDoor",
                "net.minecraft.block.BlockTrapDoor",
                "net.minecraft.block.BlockFenceGate",
                "net.minecraft.block.BlockWorkbench",
                "net.minecraft.block.BlockLever",
                "net.minecraft.block.BlockButton",
                "net.minecraft.block.BlockAnvil"
        };
    }

    @Mod.EventBusSubscriber(modid = Tags.MOD_ID)
    public static class ConfigHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equalsIgnoreCase(Tags.MOD_ID)) {
                ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
