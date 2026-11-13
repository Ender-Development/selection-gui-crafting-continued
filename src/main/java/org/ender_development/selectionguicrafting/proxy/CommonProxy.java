/*
 * Copyright (c) 2023-2026 Ender-Development.org
 * SPDX-License-Identifier: MIT
 */

package org.ender_development.selectionguicrafting.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.ender_development.selectionguicrafting.SelectionGuiCrafting;
import org.ender_development.selectionguicrafting.events.EventRightClick;
import org.ender_development.selectionguicrafting.gui.ModGuiHandler;
import org.ender_development.selectionguicrafting.network.SelectionMessageProcessRecipe;
import org.ender_development.selectionguicrafting.network.SelectionPacketHandler;

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

    public EntityPlayer getPlayerClient() {
        return null;
    }
}
