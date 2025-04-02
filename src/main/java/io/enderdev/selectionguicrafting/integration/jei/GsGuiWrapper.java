package io.enderdev.selectionguicrafting.integration.jei;

import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.gui.Assets;
import io.enderdev.selectionguicrafting.registry.GsOutput;
import io.enderdev.selectionguicrafting.registry.GsRecipe;
import io.enderdev.selectionguicrafting.registry.GsTool;
import io.enderdev.selectionguicrafting.registry.Register;
import io.enderdev.selectionguicrafting.registry.category.BlockTrigger;
import io.enderdev.selectionguicrafting.registry.category.ItemTrigger;
import io.enderdev.selectionguicrafting.registry.recipe.Recipe;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeInput;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeOutput;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;

public class GsGuiWrapper implements IRecipeWrapper {
    private final Recipe recipe;
    private final List<ItemStack> trigger = new ArrayList<>();
    private final List<ItemStack> mainHand = new ArrayList<>();
    private final List<ItemStack> offHand = new ArrayList<>();
    private final List<ItemStack> input = new ArrayList<>();
    private final List<ItemStack> output = new ArrayList<>();
    private final List<Double> inputChance = new ArrayList<>();
    private final List<Double> outputChance = new ArrayList<>();

    public GsGuiWrapper(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(@NotNull IIngredients iIngredients) {

        trigger.addAll(Register.getCategoryByID(recipe.getCategory()).getTriggerItems().stream().map(ItemTrigger::getTriggerItem).collect(Collectors.toList()));

        trigger.addAll(Register.getCategoryByID(recipe.getCategory()).getTriggerBlocks().stream().map(BlockTrigger::getTriggerBlock).map(ItemStack::new).collect(Collectors.toList()));

        input.addAll(recipe.getInputs().stream().map(RecipeInput::getIngredient).map(Ingredient::getMatchingStacks).flatMap(Arrays::stream).map(itemStack -> {
            ItemStack stack = new ItemStack(itemStack.getItem());
            stack.setCount(itemStack.getCount());
            stack.setItemDamage(itemStack.getItemDamage());
            stack.setTagCompound(itemStack.getTagCompound());
            return stack;
        }).collect(Collectors.toList()));

        inputChance.addAll(recipe.getInputs().stream().map(RecipeInput::getChance).collect(Collectors.toList()));

        output.addAll(recipe.getOutputs().stream().map(RecipeOutput::getItemStack).collect(Collectors.toList()));
        outputChance.addAll(recipe.getOutputs().stream().map(RecipeOutput::getChance).collect(Collectors.toList()));

        mainHand.addAll(Arrays.stream(recipe.getMainHand().getIngredient().getMatchingStacks()).collect(Collectors.toList()));
        offHand.addAll(Arrays.stream(recipe.getOffHand().getIngredient().getMatchingStacks()).collect(Collectors.toList()));

        List<List<ItemStack>> inputs = Arrays.asList(trigger, input, mainHand, offHand);
        List<List<ItemStack>> outputs = Collections.singletonList(output);

        iIngredients.setInputLists(VanillaTypes.ITEM, inputs);
        iIngredients.setOutputLists(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (minecraft.currentScreen == null) {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        minecraft.getTextureManager().bindTexture(Assets.JEI_LOCKED.get());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawModalRectWithCustomSizedTexture(64, 0, 0, 0, 18, 18, 18, 18);
        GlStateManager.popMatrix();
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY, 87, 0, 23, 18)) {
            ArrayList<String> tooltips = new ArrayList<>();
            tooltips.addAll(output.stream().map(itemStack -> I18n.format("jei.selectionguicrafting.output", itemStack.getCount(), itemStack.getDisplayName(), outputChance.get(output.indexOf(itemStack)) * 100)).collect(Collectors.toList()));
            tooltips.addAll(input.stream().map(itemStack -> I18n.format("jei.selectionguicrafting.input", itemStack.getCount(), itemStack.getDisplayName(), inputChance.get(input.indexOf(itemStack)) * 100)).collect(Collectors.toList()));
            return tooltips;
        }
//        if (isMouseOver(mouseX, mouseY, 64, 0, 18, 18) && secondary.isEmpty()) {
//            return Collections.singletonList(I18n.format("jei.selectionguicrafting.no_second"));
//        }
//        if (isMouseOver(mouseX, mouseY, 57, 6, 6, 6) && !secondary.isEmpty()) {
//            if (secondaryChance == 1) {
//                return Collections.singletonList(I18n.format("jei.selectionguicrafting.secondary_all"));
//            } else if (secondaryChance == 0) {
//                return Collections.singletonList(I18n.format("jei.selectionguicrafting.secondary_none"));
//            } else {
//                return Collections.singletonList(I18n.format("jei.selectionguicrafting.secondary_chance", secondaryChance * 100));
//            }
//        }
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
