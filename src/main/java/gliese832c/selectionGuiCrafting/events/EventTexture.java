package gliese832c.selectionGuiCrafting.events;

import gliese832c.SelectionGuiCrafting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventTexture {

    @SubscribeEvent
    public static void TextureStitchEvent(TextureStitchEvent event) {
        event.getMap().registerSprite(new ResourceLocation(SelectionGuiCrafting.MOD_ID, "gui/itembackground"));
    }
}