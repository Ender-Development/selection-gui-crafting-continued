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

        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        ItemStack heldStackOffhand = player.getHeldItemOffhand();

        String recipeCategory = "invalid";
        float timeMultiplier =  0.0f;
        //GuiSelectionItemPair pair = CommonProxy.selectionCraftingItems.get(0);

        for (GuiSelectionItemPair itemPair : CommonProxy.selectionCraftingItems) {
            int i = 0;
            for (ItemStack itemTool : itemPair.tool) {
                if (ItemStack.areItemStacksEqual(new ItemStack(itemTool.getItem(), 1, 0, itemTool.getTagCompound()), (new ItemStack(heldItemMainhand.getItem(), 1, 0, heldItemMainhand.getTagCompound())))) {
                    boolean validMeta = false;
                    if (itemPair.durabilityMultipliers.get(i) == Float.MAX_VALUE) {
                        if (itemTool.getMetadata() == heldItemMainhand.getMetadata()) {
                            validMeta = true;
                        }
                    } else {
                        validMeta = true;
                    }
                    if (validMeta) {
                        for (ItemStack itemInput : itemPair.input) {
                            if (itemInput.getMetadata() == Short.MAX_VALUE) {
                                if (ItemStack.areItemStacksEqual(new ItemStack(itemInput.getItem(), 1, Short.MAX_VALUE, itemInput.getTagCompound()), (new ItemStack(heldStackOffhand.getItem(), 1, Short.MAX_VALUE, heldStackOffhand.getTagCompound())))) {
                                    recipeCategory = itemPair.recipeCategory;
                                    timeMultiplier = itemPair.timeMultipliers.get(i);
                                }
                            } else {
                                if (ItemStack.areItemStacksEqual(new ItemStack(itemInput.getItem(), 1, itemInput.getMetadata(), itemInput.getTagCompound()), (new ItemStack(heldStackOffhand.getItem(), 1, heldStackOffhand.getMetadata(), heldStackOffhand.getTagCompound())))) {
                                    recipeCategory = itemPair.recipeCategory;
                                    timeMultiplier = itemPair.timeMultipliers.get(i);
                                }
                            }
                        }
                    }
                }
                i++;
            }
        }

        if (ID == CRAFTING_GUI_ID)
            //return craftingGui = new GuiScreenCrafting(CommonProxy.recipeCategories.get(recipeCategory));
            return craftingGui = new GuiScreenCrafting(CommonProxy.recipeCategories.get(recipeCategory), timeMultiplier, player, world);
        //return craftingGui = new GuiScreenCraftingContainer(CommonProxy.recipeCategories.get(recipeCategory), new ContainerSelectionGui(player.inventory, pair));

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        ItemStack heldStackOffhand = player.getHeldItemOffhand();

        String recipeCategory = "invalid";
        float timeMultiplier =  0.0f;
        //GuiSelectionItemPair pair = CommonProxy.selectionCraftingItems.get(0);

        for (GuiSelectionItemPair itemPair : CommonProxy.selectionCraftingItems) {
            int i = 0;
            for (ItemStack itemTool : itemPair.tool) {
                if (ItemStack.areItemStacksEqual(new ItemStack(itemTool.getItem(), 1, 0, itemTool.getTagCompound()), (new ItemStack(heldItemMainhand.getItem(), 1, 0, heldItemMainhand.getTagCompound())))) {
                    boolean validMeta = false;
                    if (itemPair.durabilityMultipliers.get(i) == Float.MAX_VALUE) {
                        if (itemTool.getMetadata() == heldItemMainhand.getMetadata()) {
                            validMeta = true;
                        }
                    } else {
                        validMeta = true;
                    }
                    if (validMeta) {
                        for (ItemStack itemInput : itemPair.input) {
                            if (itemInput.getMetadata() == Short.MAX_VALUE) {
                                if (ItemStack.areItemStacksEqual(new ItemStack(itemInput.getItem(), 1, Short.MAX_VALUE, itemInput.getTagCompound()), (new ItemStack(heldStackOffhand.getItem(), 1, Short.MAX_VALUE, heldStackOffhand.getTagCompound())))) {
                                    recipeCategory = itemPair.recipeCategory;
                                    timeMultiplier = itemPair.timeMultipliers.get(i);
                                }
                            } else {
                                if (ItemStack.areItemStacksEqual(new ItemStack(itemInput.getItem(), 1, itemInput.getMetadata(), itemInput.getTagCompound()), (new ItemStack(heldStackOffhand.getItem(), 1, heldStackOffhand.getMetadata(), heldStackOffhand.getTagCompound())))) {
                                    recipeCategory = itemPair.recipeCategory;
                                    timeMultiplier = itemPair.timeMultipliers.get(i);
                                }
                            }
                        }
                    }
                }
                i++;
            }
        }

        if (ID == CRAFTING_GUI_ID && !recipeCategory.equals("invalid"))
            //return craftingGui = new GuiScreenCrafting(CommonProxy.recipeCategories.get(recipeCategory));
            return craftingGui = new GuiScreenCrafting(CommonProxy.recipeCategories.get(recipeCategory), timeMultiplier, player, world);
            //return craftingGui = new GuiScreenCraftingContainer(CommonProxy.recipeCategories.get(recipeCategory), new ContainerSelectionGui(player.inventory, pair));

        return null;
    }
}