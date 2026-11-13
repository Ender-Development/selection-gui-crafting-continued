/*
 * Copyright (c) 2023-2026 Ender-Development.org
 * SPDX-License-Identifier: MIT
 */

package org.ender_development.selectionguicrafting.integration.jei;

import mezz.jei.config.KeyBindings;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.ender_development.selectionguicrafting.Tags;
import org.ender_development.selectionguicrafting.gui.GuiScreenCrafting;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class GsGuiHandler {
    private static boolean showUses;

    private GsGuiHandler() {
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void keyInputEvent(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if (handleKeyEvent() && event.getGui() instanceof GuiScreenCrafting) {
            GuiScreenCrafting gui = (GuiScreenCrafting) event.getGui();
            ItemStack itemStack = gui.getHoveredItemStack();
            if (!itemStack.isEmpty()) {
                GsPlugin.showRecipesForItemStack(itemStack, showUses);
            }
        }
    }

    private static boolean handleKeyEvent() {
        char typedChar = Keyboard.getEventCharacter();
        int eventKey = Keyboard.getEventKey();
        return (eventKey == 0 && typedChar >= ' ' || Keyboard.getEventKeyState()) && handleKeyDown(eventKey);
    }

    private static boolean handleKeyDown(int eventKey) {
        if (!Loader.isModLoaded("jei")) {
            return false;
        }
        boolean showRecipe = KeyBindings.showRecipe.isActiveAndMatches(eventKey);
        showUses = KeyBindings.showUses.isActiveAndMatches(eventKey);
        return showRecipe || showUses;
    }
}
