package gliese832c.selectionGuiCrafting.recipe;

import gliese832c.selectionGuiCrafting.proxy.CommonProxy;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

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
