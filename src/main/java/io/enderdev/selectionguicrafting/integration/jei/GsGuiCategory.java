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
import java.util.concurrent.atomic.AtomicInteger;

public class GsGuiCategory implements IRecipeCategory<GsGuiWrapper> {
    private final IDrawable drawable;
    private final IDrawable icon;

    public GsGuiCategory(IGuiHelper guiHelper) {
        drawable = guiHelper.createDrawable(Assets.JEI_SELECTION.get(), 0, 0, 160, 76);
        icon = guiHelper.createDrawable(Assets.JEI_SELECTION.get(), 160, 0, 18, 18);
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
        final int INDEX_OUTPUT = 0;
        final int INDEX_TRIGGER = 0;
        final int INDEX_MAINHAND = 1;
        final int INDEX_OFFHAND = 2;
        final int INDEX_INPUT = 3;

        // Output Items
        AtomicInteger index = new AtomicInteger();
        AtomicInteger slot = new AtomicInteger();
        iRecipeLayout.getItemStacks().init(index.get(), false, 138, 29);
        iRecipeLayout.getItemStacks().set(index.get(), iIngredients.getOutputs(VanillaTypes.ITEM).get(INDEX_OUTPUT));

        // Trigger Items
        index.getAndIncrement();
        iRecipeLayout.getItemStacks().init(index.get(), true, 13, 29);
        iRecipeLayout.getItemStacks().set(index.get(), iIngredients.getInputs(VanillaTypes.ITEM).get(INDEX_TRIGGER));

        // Main Hand
        index.getAndIncrement();
        iRecipeLayout.getItemStacks().init(index.get(), true, 21, 9);
        iRecipeLayout.getItemStacks().set(index.get(), iIngredients.getInputs(VanillaTypes.ITEM).get(INDEX_MAINHAND));

        // Offhand
        index.getAndIncrement();
        iRecipeLayout.getItemStacks().init(index.get(), true, 21, 49);
        iRecipeLayout.getItemStacks().set(index.get(), iIngredients.getInputs(VanillaTypes.ITEM).get(INDEX_OFFHAND));

        // Input Items
        for (int i = INDEX_INPUT; i < iIngredients.getInputs(VanillaTypes.ITEM).size(); i++) {
            index.getAndIncrement();
            iRecipeLayout.getItemStacks().init(index.get(), true, 58 + 18 * (slot.get() % 3), 11 + 18 * (slot.get() / 3));
            iRecipeLayout.getItemStacks().set(index.get(), iIngredients.getInputs(VanillaTypes.ITEM).get(i));
            slot.getAndIncrement();
        }
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Collections.emptyList();
    }
}
