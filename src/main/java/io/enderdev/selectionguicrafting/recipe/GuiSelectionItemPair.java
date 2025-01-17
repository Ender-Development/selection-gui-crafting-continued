package io.enderdev.selectionguicrafting.recipe;

import io.enderdev.selectionguicrafting.proxy.CommonProxy;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GuiSelectionItemPair {
    public ArrayList<ItemStack> tool;
    public ArrayList<Float> timeMultipliers;
    public ArrayList<Float> durabilityMultipliers;
    public ArrayList<ItemStack> input;
    public String recipeCategory;

    public GuiSelectionItemPair(ArrayList<ItemStack> tool, ArrayList<Float> timeMultipliers, ArrayList<Float> durabilityMultipliers, ArrayList<ItemStack> input, String recipeCategory) {
        this.tool = tool;
        this.timeMultipliers = timeMultipliers;
        this.durabilityMultipliers = durabilityMultipliers;
        this.input = input;
        this.recipeCategory = recipeCategory;
    }

    public GuiSelectionRecipeCategory getRecipeCategory() {
        return CommonProxy.recipeCategories.get(recipeCategory);
    }
}
