package io.enderdev.selectionguicrafting.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.stream.Collectors;

public class GSRecipeRegistry {
    public static final ArrayList<GSCategory> categories = new ArrayList<>();
    public static final ArrayList<GSRecipe> recipes = new ArrayList<>();

    public GSRecipeRegistry() {
    }

    public static void registerCategory(@NotNull GSCategory category) {
        categories.add(category);
    }

    public static void registerRecipe(@NotNull GSRecipe recipe) {
        recipes.add(recipe);
    }

    public static GSCategory getCategory(@NotNull String id) {
        return categories.stream().filter(category -> category.getId().equals(id)).findFirst().orElse(null);
    }

    public static ArrayList<GSRecipe> getRecipesForCategory(@NotNull String category) {
        return recipes.stream().filter(recipe -> recipe.getCategory().equals(category)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<GSRecipe> getRecipesForCategory(@NotNull GSCategory category) {
        return getRecipesForCategory(category.getId());
    }

    public static ArrayList<GSCategory> getCategories() {
        return categories;
    }

    public static ArrayList<GSRecipe> getRecipes() {
        return recipes;
    }

    public static boolean removeCategory(@NotNull String id) {
        return categories.removeIf(category -> category.getId().equals(id));
    }

    public static boolean removeRecipe(@NotNull String category, @NotNull ArrayList<ItemStack> output) {
        return recipes.removeIf(recipe -> recipe.getCategory().equals(category) && recipe.getOutputs().equals(output));
    }

    static {
        // create the category and placeholder recipe for invalid recipes
        registerCategory(new GSCategory().setId("invalid").setDisplayName("INVALID"));
        registerRecipe(new GSRecipe().setCategory("invalid").addInput(Blocks.BARRIER).addTool(Blocks.BARRIER, 1.0f).addOutput(Blocks.BARRIER, 1.0f));
    }
}
