package io.enderdev.selectionguicrafting.recipe;

import java.util.ArrayList;

public class GuiSelectionRecipeCategory {

    public String displayName;

    public ArrayList<GuiSelectionRecipe> recipes;

    public GuiSelectionRecipeCategory(String displayName, ArrayList<GuiSelectionRecipe> recipes) {
        this.displayName = displayName;
        this.recipes = recipes;
    }
}
