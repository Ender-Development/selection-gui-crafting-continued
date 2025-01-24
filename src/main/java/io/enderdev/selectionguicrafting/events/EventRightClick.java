package io.enderdev.selectionguicrafting.events;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.gui.ModGuiHandler;
import io.enderdev.selectionguicrafting.registry.GsRegistry;
import io.enderdev.selectionguicrafting.registry.GsTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class EventRightClick {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void rightClickItem(PlayerInteractEvent.RightClickItem event) {

        EntityPlayer player = (EntityPlayer) event.getEntity();

        if (player == null) {
            return;
        }

        ItemStack eventItemMainhand = player.getHeldItemMainhand();
        ItemStack eventStackOffhand = player.getHeldItemOffhand();

        if (eventItemMainhand.isEmpty() || eventStackOffhand.isEmpty()) {
            return;
        }

        if (GsRegistry.getCategoryFromInput(eventItemMainhand, eventStackOffhand) == null) {
            return;
        }

        event.setCanceled(true);
        if (player.getEntityWorld().isRemote) {
            player.openGui(SelectionGuiCrafting.instance, ModGuiHandler.CRAFTING_GUI_ID, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        EntityPlayer player = (EntityPlayer) event.getEntity();

        if (player == null) {
            return;
        }

        ItemStack eventItemMainhand = player.getHeldItemMainhand();
        ItemStack eventItemOffhand = player.getHeldItemOffhand();

        if (eventItemMainhand.isEmpty() || eventItemOffhand.isEmpty()) {
            return;
        }

        if (GsRegistry.getCategoryFromInput(eventItemMainhand, eventItemOffhand) == null) {
            return;
        }

        event.setCanceled(true);
    }
}
