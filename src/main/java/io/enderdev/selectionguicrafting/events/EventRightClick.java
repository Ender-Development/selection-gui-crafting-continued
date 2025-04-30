package io.enderdev.selectionguicrafting.events;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.config.SelectionConfig;
import io.enderdev.selectionguicrafting.gui.ModGuiHandler;
import io.enderdev.selectionguicrafting.registry.Register;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class EventRightClick {

    private static final List<Class<?>> blacklistBlocks = new ArrayList<Class<?>>() {{
        Arrays.asList(SelectionConfig.GENERAL.blacklistClasses).forEach(entry -> {
            try {
                Class<?> clazz = Class.forName(entry);
                if (Block.class.isAssignableFrom(clazz)) add(clazz);
            } catch (ClassNotFoundException e) {
                SelectionGuiCrafting.LOGGER.error(e);
            }
        });
    }};

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void rightClickItem(PlayerInteractEvent.RightClickItem event) {

        EntityPlayer player = (EntityPlayer) event.getEntity();

        if (player == null) {
            return;
        }

        if (checkItems(player)) {
            event.setCanceled(true);
            openGui(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        EntityPlayer player = (EntityPlayer) event.getEntity();

        if (player == null) {
            return;
        }

        IBlockState eventState = player.getEntityWorld().getBlockState(event.getPos());
        Block eventBlock = eventState.getBlock();

        // ignore TileEntities
        if (eventBlock.hasTileEntity(eventState)) {
            return;
        }

        if (blacklistBlocks.stream().anyMatch(clazz -> eventBlock.getClass().equals(clazz))) {
            return;
        }

        if (checkItems(player)) {
            event.setCanceled(true);
            openGui(player);
            return;
        }

        if (Register.isTriggerBlock(eventBlock)) {
            event.setCanceled(true);
            openGui(player);
        }
    }

    private boolean checkItems(EntityPlayer player) {
        ItemStack eventItemMainhand = player.getHeldItemMainhand();
        ItemStack eventStackOffhand = player.getHeldItemOffhand();

        if (eventItemMainhand.isEmpty() && eventStackOffhand.isEmpty()) {
            return false;
        }

        return Register.isTriggerItem(eventItemMainhand) || Register.isTriggerItem(eventStackOffhand);
    }

    private void openGui(EntityPlayer player) {
        if (player.getEntityWorld().isRemote) {
            player.openGui(SelectionGuiCrafting.instance, ModGuiHandler.CRAFTING_GUI_ID, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
        }
    }
}
