/*
 * Copyright (c) 2023-2026 Ender-Development.org
 * SPDX-License-Identifier: MIT
 */

package org.ender_development.selectionguicrafting.gui;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import org.ender_development.selectionguicrafting.registry.Register;
import org.ender_development.selectionguicrafting.registry.category.AbstractTrigger;
import org.ender_development.selectionguicrafting.registry.category.BlockTrigger;
import org.ender_development.selectionguicrafting.registry.category.Category;
import org.ender_development.selectionguicrafting.registry.category.ItemTrigger;

public class ModGuiHandler implements IGuiHandler {
    public static final int CRAFTING_GUI_ID = 0;
    public static GuiScreenCrafting craftingGui;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return implementMethod(ID, player, world, x, y, z);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return implementMethod(ID, player, world, x, y, z);
    }

    private Object implementMethod(int ID, EntityPlayer player, World world, int x, int y, int z) {
        AbstractTrigger trigger = null;

        /* BLOCK: HIGHEST PRIORITY */
        RayTraceResult rayTraceResult = player.rayTrace(Minecraft.getMinecraft().playerController.getBlockReachDistance(), 1.0F);
        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            Block rayBlock = world.getBlockState(rayTraceResult.getBlockPos()).getBlock();
            trigger = Register.getTriggerBlock(rayBlock);
        }

        /* MAINHAND: HIGHER PRIORITY */
        if (trigger == null) {
            ItemStack heldStackMainhand = player.getHeldItemMainhand();
            trigger = Register.getTriggerItem(heldStackMainhand);
        }

        /* OFFHAND: LOWEST PRIORITY */
        if (trigger == null) {
            ItemStack heldStackOffhand = player.getHeldItemOffhand();
            trigger = Register.getTriggerItem(heldStackOffhand);
        }

        Category recipeCategory = null;
        if (trigger instanceof BlockTrigger) {
            recipeCategory = Register.getCategoryByTriggerBlock((BlockTrigger) trigger);
        } else if (trigger instanceof ItemTrigger) {
            recipeCategory = Register.getCategoryByTriggerItem((ItemTrigger) trigger);
        }

        if (ID == CRAFTING_GUI_ID && recipeCategory != null) {
            return craftingGui = new GuiScreenCrafting(recipeCategory, trigger, player, world);
        }

        return recipeCategory;
    }
}
