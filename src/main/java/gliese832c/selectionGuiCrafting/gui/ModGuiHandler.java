package gliese832c.selectionGuiCrafting.gui;

import gliese832c.selectionGuiCrafting.proxy.CommonProxy;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionItemPair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
    // GUI IDs
    public static final int CRAFTING_GUI_ID = 0;
    public static GuiScreenCrafting craftingGui;
    //public static GuiScreenCraftingContainer craftingGui;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        Item heldItemMainhand = player.getHeldItemMainhand().getItem();
        ItemStack heldStackOffhand = player.getHeldItemOffhand();

        String recipeCategory = "invalid";
        //GuiSelectionItemPair pair = CommonProxy.selectionCraftingItems.get(0);

        for (GuiSelectionItemPair itemPair : CommonProxy.selectionCraftingItems) {
            for (Item itemTool : itemPair.tool) {
                if (ItemStack.areItemStacksEqual(new ItemStack(itemTool), (new ItemStack(heldItemMainhand)))) {
                    for (ItemStack itemInput : itemPair.input) {
                        if (itemInput.getMetadata() == Short.MAX_VALUE) {
                            if (ItemStack.areItemStacksEqual(new ItemStack(itemInput.getItem(), 1, Short.MAX_VALUE, itemInput.getTagCompound()), (new ItemStack(heldStackOffhand.getItem(), 1, Short.MAX_VALUE, heldStackOffhand.getTagCompound())))) {
                                recipeCategory = itemPair.recipeCategory;
                                //pair = itemPair;
                            }
                        } else {
                            if (ItemStack.areItemStacksEqual(new ItemStack(itemInput.getItem(), 1, itemInput.getMetadata(), itemInput.getTagCompound()), (new ItemStack(heldStackOffhand.getItem(), 1, heldStackOffhand.getMetadata(), heldStackOffhand.getTagCompound())))) {
                                recipeCategory = itemPair.recipeCategory;
                                //pair = itemPair;
                            }
                        }
                    }
                }
            }
        }

        if (ID == CRAFTING_GUI_ID)
            //return craftingGui = new GuiScreenCrafting(CommonProxy.recipeCategories.get(recipeCategory));
            return craftingGui = new GuiScreenCrafting(CommonProxy.recipeCategories.get(recipeCategory), player, world);
        //return craftingGui = new GuiScreenCraftingContainer(CommonProxy.recipeCategories.get(recipeCategory), new ContainerSelectionGui(player.inventory, pair));

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        Item heldItemMainhand = player.getHeldItemMainhand().getItem();
        ItemStack heldStackOffhand = player.getHeldItemOffhand();

        String recipeCategory = "invalid";
        //GuiSelectionItemPair pair = CommonProxy.selectionCraftingItems.get(0);

        for (GuiSelectionItemPair itemPair : CommonProxy.selectionCraftingItems) {
            for (Item itemTool : itemPair.tool) {
                if (ItemStack.areItemStacksEqual(new ItemStack(itemTool), (new ItemStack(heldItemMainhand)))) {
                    for (ItemStack itemInput : itemPair.input) {
                        if (itemInput.getMetadata() == Short.MAX_VALUE) {
                            if (ItemStack.areItemStacksEqual(new ItemStack(itemInput.getItem(), 1, Short.MAX_VALUE, itemInput.getTagCompound()), (new ItemStack(heldStackOffhand.getItem(), 1, Short.MAX_VALUE, heldStackOffhand.getTagCompound())))) {
                                recipeCategory = itemPair.recipeCategory;
                                //pair = itemPair;
                            }
                        } else {
                            if (ItemStack.areItemStacksEqual(new ItemStack(itemInput.getItem(), 1, itemInput.getMetadata(), itemInput.getTagCompound()), (new ItemStack(heldStackOffhand.getItem(), 1, heldStackOffhand.getMetadata(), heldStackOffhand.getTagCompound())))) {
                                recipeCategory = itemPair.recipeCategory;
                                //pair = itemPair;
                            }
                        }
                    }
                }
            }
        }

        if (ID == CRAFTING_GUI_ID && !recipeCategory.equals("invalid"))
            //return craftingGui = new GuiScreenCrafting(CommonProxy.recipeCategories.get(recipeCategory));
            return craftingGui = new GuiScreenCrafting(CommonProxy.recipeCategories.get(recipeCategory), player, world);
            //return craftingGui = new GuiScreenCraftingContainer(CommonProxy.recipeCategories.get(recipeCategory), new ContainerSelectionGui(player.inventory, pair));

        return null;
    }
}