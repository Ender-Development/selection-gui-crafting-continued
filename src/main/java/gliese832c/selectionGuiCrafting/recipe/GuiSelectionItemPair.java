package gliese832c.selectionGuiCrafting.recipe;

import gliese832c.selectionGuiCrafting.proxy.CommonProxy;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class GuiSelectionItemPair {
    public ArrayList<Item> tool;
    public ArrayList<Float> durabilityMultipliers;
    public ArrayList<ItemStack> input;
    public String recipeCategory;

    public GuiSelectionItemPair(ArrayList<Item> tool, ArrayList<Float> durabilityMultipliers, ArrayList<ItemStack> input, String recipeCategory) {
        this.tool = tool;
        this.durabilityMultipliers = durabilityMultipliers;
        this.input = input;
        this.recipeCategory = recipeCategory;
    }

    public GuiSelectionRecipeCategory getRecipeCategory() {
        return CommonProxy.recipeCategories.get(recipeCategory);
    }
}
