package gliese832c.selectionGuiCrafting.gui;

import gliese832c.selectionGuiCrafting.proxy.CommonProxy;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionItemPair;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipeCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
    // GUI IDs
    public static final int CRAFTING_GUI_ID = 0;
    public static GuiScreenCrafting craftingGui;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        Item heldItemMainhand = player.getHeldItemMainhand().getItem();
        Item heldItemOffhand = player.getHeldItemOffhand().getItem();

        String recipeCategory = "invalid";

        for (GuiSelectionItemPair itemPair : CommonProxy.selectionCraftingItems) {
            for (Item itemTool : itemPair.tool) {
                if (ItemStack.areItemStacksEqual(new ItemStack(itemTool), (new ItemStack(heldItemMainhand)))) {
                    for (Item itemInput : itemPair.input) {
                        if (ItemStack.areItemStacksEqual(new ItemStack(itemInput), (new ItemStack(heldItemOffhand)))) {
                            recipeCategory = itemPair.recipeCategory;
                        }
                    }
                }
            }
        }

        if (ID == CRAFTING_GUI_ID)
            return craftingGui = new GuiScreenCrafting(CommonProxy.recipeCategories.get(recipeCategory));

        return null;
    }
}