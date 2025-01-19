package io.enderdev.selectionguicrafting.integration.jei;

import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.registry.GsRecipe;
import io.enderdev.selectionguicrafting.registry.GsRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class GsPlugin implements IModPlugin {

    public GsPlugin() {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new GsGuiCategory(registry.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(GsRecipe.class, GsGuiWrapper::new, "jei." + Tags.MOD_ID + ".category");
        registry.addRecipes(GsRegistry.getRecipes(), "jei." + Tags.MOD_ID + ".category");
    }

}
