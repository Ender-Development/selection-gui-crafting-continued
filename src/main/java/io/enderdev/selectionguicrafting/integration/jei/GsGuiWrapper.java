package io.enderdev.selectionguicrafting.integration.jei;

import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.gui.Assets;
import io.enderdev.selectionguicrafting.registry.GsOutput;
import io.enderdev.selectionguicrafting.registry.GsRecipe;
import io.enderdev.selectionguicrafting.registry.GsTool;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;

public class GsGuiWrapper implements IRecipeWrapper {
    public final GsRecipe recipe;
    private List<ItemStack> tool;
    private List<ItemStack> input;
    private List<ItemStack> secondary;
    private float secondaryChance;
    private List<ItemStack> output;
    private List<Float> outputChance;

    public GsGuiWrapper(GsRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(@NotNull IIngredients iIngredients) {
        tool = recipe.getTool().stream().map(GsTool::getItemStack).map(itemStack -> {
            ItemStack stack = new ItemStack(itemStack.getItem());
            if (itemStack.isItemStackDamageable()) {
                stack.setCount(1);
            } else {
                stack.setCount(itemStack.getCount());
            }
            stack.setItemDamage(itemStack.getItemDamage());
            stack.setTagCompound(itemStack.getTagCompound());
            return stack;
        }).collect(Collectors.toList());

        input = recipe.getInput().stream().map(Ingredient::getMatchingStacks).flatMap(Arrays::stream).map(itemStack -> {
            ItemStack stack = new ItemStack(itemStack.getItem());
            stack.setCount(itemStack.getCount());
            stack.setItemDamage(itemStack.getItemDamage());
            stack.setTagCompound(itemStack.getTagCompound());
            return stack;
        }).collect(Collectors.toList());

        secondary = recipe.getCatalyst() == null ? Collections.emptyList() : Arrays.asList(recipe.getCatalyst().getIngredient().getMatchingStacks());
        secondaryChance = recipe.getCatalyst() == null ? 1 : recipe.getCatalyst().getChance();

        output = recipe.getOutput().stream().map(GsOutput::getItemStack).collect(Collectors.toList());
        outputChance = recipe.getOutput().stream().map(GsOutput::getChance).collect(Collectors.toList());

        List<List<ItemStack>> inputs = Arrays.asList(tool, input, secondary);
        List<List<ItemStack>> outputs = Collections.singletonList(output);


        iIngredients.setInputLists(VanillaTypes.ITEM, inputs);
        iIngredients.setOutputLists(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (minecraft.currentScreen == null) {
            return;
        }
        if (secondary.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            minecraft.getTextureManager().bindTexture(Assets.JEI_LOCKED.get());
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawModalRectWithCustomSizedTexture(64, 0, 0, 0, 18, 18, 18, 18);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY, 87, 0, 23, 18)) {
            return output.stream().map(itemStack -> I18n.format("jei.selectionguicrafting.output", itemStack.getCount(), itemStack.getDisplayName(), outputChance.get(output.indexOf(itemStack)) * 100)).collect(Collectors.toList());
        }
        if (isMouseOver(mouseX, mouseY, 64, 0, 18, 18) && secondary.isEmpty()) {
            return Collections.singletonList(I18n.format("jei.selectionguicrafting.no_second"));
        }
        if (isMouseOver(mouseX, mouseY, 57, 6, 6, 6) && !secondary.isEmpty()) {
            if (secondaryChance == 1) {
                return Collections.singletonList(I18n.format("jei.selectionguicrafting.secondary_all"));
            } else if (secondaryChance == 0) {
                return Collections.singletonList(I18n.format("jei.selectionguicrafting.secondary_none"));
            } else {
                return Collections.singletonList(I18n.format("jei.selectionguicrafting.secondary_chance", secondaryChance * 100));
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean handleClick(@NotNull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
