package gliese832c.selectionGuiCrafting.recipe;

public class GuiSelectionRecipeCategory {

    public String displayName;
    public boolean usesDurability;

    public GuiSelectionRecipe[] recipes;

    public GuiSelectionRecipeCategory(String displayName, boolean usesDurability, GuiSelectionRecipe[] recipes) {
        this.displayName = displayName;
        this.usesDurability = usesDurability;
        this.recipes = recipes;
    }
}
