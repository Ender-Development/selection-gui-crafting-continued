package io.enderdev.selectionguicrafting.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {
    void preInit(FMLPreInitializationEvent preEvent);

    void init(FMLInitializationEvent event);

    void postInit(FMLPostInitializationEvent postEvent);

    EntityPlayer getPlayerClient();
}
