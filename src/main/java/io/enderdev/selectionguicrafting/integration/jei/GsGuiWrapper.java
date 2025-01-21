package io.enderdev.selectionguicrafting.integration.jei;

import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.registry.GsOutput;
import io.enderdev.selectionguicrafting.registry.GsRecipe;
import io.enderdev.selectionguicrafting.registry.GsTool;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
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
    private List<ItemStack> output;
    private List<Float> outputChance;

    public GsGuiWrapper(GsRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(@NotNull IIngredients iIngredients) {
        tool = recipe.getTools().stream().map(GsTool::getItemStack).map(itemStack -> {
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

        input = recipe.getInputs().stream().map(Ingredient::getMatchingStacks).flatMap(Arrays::stream).map(itemStack -> {
            ItemStack stack = new ItemStack(itemStack.getItem());
            stack.setCount(itemStack.getCount());
            stack.setItemDamage(itemStack.getItemDamage());
            stack.setTagCompound(itemStack.getTagCompound());
            return stack;
        }).collect(Collectors.toList());

        secondary = Collections.emptyList(); // TODO: Implement secondary output

        output = recipe.getOutputs().stream().map(GsOutput::getItemStack).collect(Collectors.toList());
        outputChance = recipe.getOutputs().stream().map(GsOutput::getChance).collect(Collectors.toList());

        List<List<ItemStack>> inputs = Arrays.asList(tool, input, secondary);
        List<List<ItemStack>> outputs = Collections.singletonList(output);


        iIngredients.setInputLists(VanillaTypes.ITEM, inputs);
        iIngredients.setOutputLists(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (secondary.isEmpty() && minecraft.currentScreen != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            minecraft.getTextureManager().bindTexture(new ResourceLocation(Tags.MOD_ID, "textures/jei/locked.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawModalRectWithCustomSizedTexture(64, 0, 0, 0, 18, 18, 18, 18);
            GlStateManager.popMatrix();
        }
//        if (recipe() > 0) {
//            minecraft.fontRenderer.drawString(String.valueOf(-recipe.getAmount()), 32, 14, Color.red.getRGB());
//        }
//        if (recipe.getAmount() < 0) {
//            minecraft.fontRenderer.drawString("+" + -recipe.getAmount(), 32, 14, Color.green.getRGB());
//        }
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (isMouseOver(mouseX, mouseY, 87, 0,23,18)) {
            return output.stream().map(itemStack -> itemStack.getCount()+ "x " + itemStack.getDisplayName()+ " " + outputChance.get(output.indexOf(itemStack))*100 + "%").collect(Collectors.toList());
        } else if (isMouseOver(mouseX,mouseY,64,0,18,18)  && secondary.isEmpty()) {
            return Collections.singletonList("No secondary input.");
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
