package gliese832c.selectionGuiCrafting.container;

import gliese832c.selectionGuiCrafting.gui.GuiScreenCrafting;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionItemPair;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipe;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipeCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerSelectionGui extends Container {

    public ContainerSelectionGui(InventoryPlayer playerInv, final GuiSelectionItemPair recipes) {

        GuiSelectionRecipeCategory recipeCategory = recipes.getRecipeCategory();

        IItemHandler inventory = new IItemHandler() {
            @Override
            public int getSlots() {
                return recipeCategory.recipes.length;
            }

            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot) {
                return recipeCategory.recipes[slot].outputs[0];
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return null;
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return null;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 0;
            }
        };

        int iconDistance = GuiScreenCrafting.ICON_DISTANCE;
        int maxItemsPerRow = GuiScreenCrafting.MAX_ITEMS_PER_ROW;

        int i = 0;
        GuiSelectionRecipe[] recipesList = recipeCategory.recipes;
        for (GuiSelectionRecipe recipe : recipesList) {

            int rowNumber = i / maxItemsPerRow;

            //int xPos = left + 10 + (i % GuiScreenCrafting.MAX_ITEMS_PER_ROW) * GuiScreenCrafting.ICON_DISTANCE;
            //int yPos = top + 24 + (rowNumber * GuiScreenCrafting.ICON_DISTANCE);
            int xPos = (i % maxItemsPerRow) * iconDistance;
            int yPos = (rowNumber * iconDistance);

            addSlotToContainer(new SlotItemHandler(inventory, i, xPos, yPos));

            i++;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}