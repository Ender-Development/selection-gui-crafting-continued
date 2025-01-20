package io.enderdev.selectionguicrafting.proxy;


import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.events.EventRightClick;
import io.enderdev.selectionguicrafting.gui.ModGuiHandler;
import io.enderdev.selectionguicrafting.network.SelectionMessageProcessRecipe;
import io.enderdev.selectionguicrafting.network.SelectionPacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy implements IProxy {

    public void preInit(FMLPreInitializationEvent preEvent) {
        MinecraftForge.EVENT_BUS.register(new EventRightClick());
        SelectionPacketHandler.SELECTION_NETWORK_WRAPPER.registerMessage(SelectionMessageProcessRecipe.SelectionMessageProcessRecipeHandler.class, SelectionMessageProcessRecipe.class, 0, Side.SERVER);
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(SelectionGuiCrafting.instance, new ModGuiHandler());
    }

    public void postInit(FMLPostInitializationEvent postEvent) {
    }

    public void serverStart(FMLServerStartingEvent serverStartEvent) {
    }

    public void onIdMapping(FMLModIdMappingEvent idMappingEvent) {
    }

    public EntityPlayer getPlayerClient() {
        return null;
    }
}