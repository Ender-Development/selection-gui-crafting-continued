package io.enderdev.selectionguicrafting.registry;

import io.enderdev.selectionguicrafting.registry.category.BlockTrigger;
import io.enderdev.selectionguicrafting.registry.category.Category;
import io.enderdev.selectionguicrafting.registry.category.ItemTrigger;
import io.enderdev.selectionguicrafting.registry.recipe.Recipe;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class Register {
    private static final ArrayList<Category> categories = new ArrayList<>();
	private static final ArrayList<Recipe> recipes = new ArrayList<>();

	private static final ArrayList<ItemTrigger> allTriggerItems = new ArrayList<>();
	private static final ArrayList<BlockTrigger> allTriggerBlocks = new ArrayList<>();

    private static final Category INVALID = new Category().id("invalid").trigger(Blocks.BARRIER, 1, 1,1).register();

	public static void addCategory(Category category) {
		categories.add(category);
		allTriggerItems.addAll(category.getTriggerItems());
		allTriggerBlocks.addAll(category.getTriggerBlocks());
	}

	public static void addRecipe(Recipe recipe) {
		recipes.add(recipe);
	}

    public static ArrayList<Category> getCategories() {
        return categories;
    }

    public static ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public static ArrayList<ItemTrigger> getTriggerItems() {
        return allTriggerItems;
    }

    public static ArrayList<BlockTrigger> getTriggerBlocks() {
        return allTriggerBlocks;
    }

    public static Category getCategoryByID(String id) {
        return categories.stream().filter(category -> category.getID().equals(id)).findFirst().orElse(INVALID);
    }

    public static Category getCategoryByTriggerItem(ItemTrigger item) {
        return categories.stream().filter(category -> category.getTriggerItems().contains(item)).findFirst().orElse(INVALID);
    }

    public static Category getCategoryByTriggerBlock(BlockTrigger block) {
        return categories.stream().filter(category -> category.getTriggerBlocks().contains(block)).findFirst().orElse(INVALID);
    }

    public static ArrayList<Recipe> getRecipesByCategory(Category category) {
        return recipes.stream().filter(recipe -> recipe.getCategory().equals(category.getID())).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public static boolean isTriggerItem(ItemStack itemStack) {
        return allTriggerItems.stream().anyMatch(triggerItem -> triggerItem.getTriggerItem().isItemEqual(itemStack));
    }

    public static boolean isTriggerBlock(Block block) {
        return allTriggerBlocks.stream().anyMatch(triggerBlock -> triggerBlock.getTriggerBlock().isAssociatedBlock(block));
    }
}
