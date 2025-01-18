package io.enderdev.selectionguicrafting.gui;

import io.enderdev.selectionguicrafting.recipe.GSRecipe;
import io.enderdev.selectionguicrafting.recipe.GSRecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.Arrays;

public class ModGuiHandler implements IGuiHandler {
    public static final int CRAFTING_GUI_ID = 0;
    public static GuiScreenCrafting craftingGui;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return implementMethod(ID, player, world, x, y, z);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return implementMethod(ID, player, world, x, y, z);
    }

    private Object implementMethod(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        ItemStack heldStackOffhand = player.getHeldItemOffhand();

        float timeMultiplier = 0.0f;

        String recipeCategory = GSRecipeRegistry.getRecipes().stream()
                .filter(recipe -> recipe.getTools().stream().map(ItemStack::getItem).anyMatch(item -> item == heldItemMainhand.getItem()))
                .filter(recipe -> recipe.getInputs().stream().map(Ingredient::getMatchingStacks)
                        .anyMatch(stacks -> Arrays.stream(stacks).anyMatch(stack -> stack.getItem() == heldStackOffhand.getItem())))
                .map(GSRecipe::getCategory)
                .findFirst().orElse("invalid");

        if (ID == CRAFTING_GUI_ID) {
            return craftingGui = new GuiScreenCrafting(GSRecipeRegistry.getCategory(recipeCategory), timeMultiplier, player, world);
        }

        return null;
    }
}