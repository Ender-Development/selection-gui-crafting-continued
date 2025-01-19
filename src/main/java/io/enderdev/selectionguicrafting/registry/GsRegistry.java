package io.enderdev.selectionguicrafting.registry;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.stream.Collectors;

public class GsRegistry {
    public static final ArrayList<GsCategory> categories = new ArrayList<>();
    public static final ArrayList<GsRecipe> recipes = new ArrayList<>();

    public GsRegistry() {
    }

    public static void registerCategory(@NotNull GsCategory category) {
        categories.add(category);
    }

    public static void registerRecipe(@NotNull GsRecipe recipe) {
        recipes.add(recipe);
    }

    public static GsCategory getCategory(@NotNull String id) {
        return categories.stream().filter(category -> category.getId().equals(id)).findFirst().orElse(null);
    }

    public static ArrayList<GsRecipe> getRecipesForCategory(@NotNull String category) {
        return recipes.stream().filter(recipe -> recipe.getCategory().equals(category)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<GsRecipe> getRecipesForCategory(@NotNull GsCategory category) {
        return getRecipesForCategory(category.getId());
    }

    public static ArrayList<GsCategory> getCategories() {
        return categories;
    }

    public static ArrayList<GsRecipe> getRecipes() {
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
        registerCategory(new GsCategory().setId("invalid").setDisplayName("INVALID"));
        registerRecipe(new GsRecipe().setCategory("invalid").addInput(Blocks.BARRIER).addTool(Blocks.BARRIER, 1.0f).addOutput(Blocks.BARRIER, 1.0f));
    }
}
