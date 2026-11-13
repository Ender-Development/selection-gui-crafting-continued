/*
 * Copyright (c) 2023-2026 Ender-Development.org
 * SPDX-License-Identifier: MIT
 */

package org.ender_development.selectionguicrafting.integration.jei;

import javax.annotation.Nonnull;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipesGui;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.ender_development.selectionguicrafting.Tags;
import org.ender_development.selectionguicrafting.registry.Register;
import org.ender_development.selectionguicrafting.registry.recipe.Recipe;

@JEIPlugin
public class GsPlugin implements IModPlugin {
    private static IJeiRuntime jeiRuntime;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new GsGuiCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(Recipe.class, GsGuiWrapper::new, "jei." + Tags.MOD_ID + ".category");
        registry.addRecipes(Register.getRecipes(), "jei." + Tags.MOD_ID + ".category");
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
        GsPlugin.jeiRuntime = jeiRuntime;
    }

    public static boolean showRecipesForItemStack(ItemStack itemStack, boolean isUses) {
        jeiRuntime.getRecipesGui().show(jeiRuntime.getRecipeRegistry().createFocus(isUses ? IFocus.Mode.INPUT : IFocus.Mode.OUTPUT, itemStack));
        Minecraft mc = Minecraft.getMinecraft();
        return mc.currentScreen instanceof IRecipesGui;
    }
}
