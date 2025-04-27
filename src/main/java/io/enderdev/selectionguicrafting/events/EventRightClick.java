package io.enderdev.selectionguicrafting.events;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.gui.ModGuiHandler;
import io.enderdev.selectionguicrafting.registry.Register;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class EventRightClick {

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

        if (checkItems(player)) {
            event.setCanceled(true);
            openGui(player);
            return;
        }

        Block eventBlock = player.getEntityWorld().getBlockState(event.getPos()).getBlock();

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
