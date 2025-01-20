package io.enderdev.selectionguicrafting.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent preEvent) {
        super.preInit(preEvent);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent postEvent) {
        super.postInit(postEvent);
    }

    @Override
    public EntityPlayer getPlayerClient() {
        return Minecraft.getMinecraft().player;
    }
}