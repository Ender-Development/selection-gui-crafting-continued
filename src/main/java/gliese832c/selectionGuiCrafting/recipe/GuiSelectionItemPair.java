package gliese832c.selectionGuiCrafting.recipe;

import net.minecraft.item.Item;

public class GuiSelectionItemPair {
    public Item[] tool;
    public Item[] input;
    public String recipeCategory;

    public GuiSelectionItemPair(Item[] tool, Item[] input, String recipeCategory) {
        this.tool = tool;
        this.input = input;
        this.recipeCategory = recipeCategory;
    }
}
