package io.enderdev.selectionguicrafting.integration.jei;

import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.gui.Assets;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class GsGuiCategory implements IRecipeCategory<GsGuiWrapper> {
    private final IDrawable drawable;
    private final IDrawable icon;

    public GsGuiCategory(IGuiHelper guiHelper) {
        drawable = guiHelper.createDrawable(Assets.JEI_SELECTION.get(), 0, 0, 135, 18);
        icon = guiHelper.createDrawable(Assets.JEI_SELECTION.get(), 135, 0, 18, 18);
    }

    @Override
    public @NotNull String getUid() {
        return "jei." + Tags.MOD_ID + ".category";
    }

    @Override
    public @NotNull String getTitle() {
        return I18n.format(getUid());
    }

    @Override
    public @NotNull String getModName() {
        return Tags.MOD_NAME;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return drawable;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, @NotNull GsGuiWrapper gsGuiWrapper, IIngredients iIngredients) {
        int index = 0;
        iRecipeLayout.getItemStacks().init(index, true, 0, 0);
        iRecipeLayout.getItemStacks().set(index, iIngredients.getInputs(VanillaTypes.ITEM).get(index));

        index++;
        iRecipeLayout.getItemStacks().init(index, true, 38, 0);
        iRecipeLayout.getItemStacks().set(index, iIngredients.getInputs(VanillaTypes.ITEM).get(index));

        index++;
        iRecipeLayout.getItemStacks().init(index, true, 64, 0);
        iRecipeLayout.getItemStacks().set(index, iIngredients.getInputs(VanillaTypes.ITEM).get(index));

        index++;
        iRecipeLayout.getItemStacks().init(index, false, 117, 0);
        iRecipeLayout.getItemStacks().set(index, iIngredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Collections.emptyList();
    }
}
