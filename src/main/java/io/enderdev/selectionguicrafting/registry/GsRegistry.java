package io.enderdev.selectionguicrafting.registry;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GsRegistry {
    private static final ArrayList<GsCategory> categories = new ArrayList<>();
    private static final ArrayList<GsRecipe> recipes = new ArrayList<>();

    public GsRegistry() {
    }

    public static void registerCategory(@NotNull GsCategory category) {
        if (categories.stream().anyMatch(gsCategory -> gsCategory.getId().equals(category.getId()))) {
            SelectionGuiCrafting.LOGGER.warn("Category with id {} already exists", category.getId());
            return;
        }
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

    public static ArrayList<GsRecipe> getValidRecipes(@NotNull GsCategory category, @NotNull ItemStack tool) {
        return GsRegistry.getRecipesForCategory(category).stream().filter(recipe -> recipe.getTool().stream().map(GsTool::getItemStack).anyMatch(itemStack -> itemStack.isItemEqualIgnoreDurability(tool))).collect(Collectors.toCollection(ArrayList::new));
    }

    public static boolean removeCategory(@NotNull String id) {
        return categories.removeIf(category -> category.getId().equals(id));
    }

    public static boolean removeRecipe(@NotNull String category, @NotNull ArrayList<GsOutput> output) {
        return recipes.removeIf(recipe -> recipe.getCategory().equals(category) && recipe.getOutput().equals(output));
    }

    @Nullable
    public static String getCategoryFromInput(ItemStack mainhand, ItemStack offhand) {
        return GsRegistry.getRecipes().stream()
                .filter(recipe -> recipe.getTool().stream().map(GsTool::getItemStack).map(ItemStack::getItem).anyMatch(item -> item == mainhand.getItem()))
                .filter(recipe -> recipe.getInput().stream().map(Ingredient::getMatchingStacks)
                        .anyMatch(stacks -> Arrays.stream(stacks).anyMatch(stack -> stack.getItem() == offhand.getItem())))
                .map(GsRecipe::getCategory)
                .findFirst().orElse(null);
    }
}
