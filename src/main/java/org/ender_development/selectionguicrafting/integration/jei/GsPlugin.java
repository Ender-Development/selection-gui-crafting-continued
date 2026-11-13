/*
 * Copyright (c) 2023-2026 Ender-Development.org
 * SPDX-License-Identifier: MIT
 */

package org.ender_development.selectionguicrafting.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import org.ender_development.selectionguicrafting.Tags;
import org.ender_development.selectionguicrafting.registry.Register;
import org.ender_development.selectionguicrafting.registry.recipe.Recipe;

@JEIPlugin
public class GsPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new GsGuiCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(Recipe.class, GsGuiWrapper::new, "jei." + Tags.MOD_ID + ".category");
        registry.addRecipes(Register.getRecipes(), "jei." + Tags.MOD_ID + ".category");
    }
}
