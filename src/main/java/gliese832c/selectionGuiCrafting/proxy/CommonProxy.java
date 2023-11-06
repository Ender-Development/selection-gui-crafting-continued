package gliese832c.selectionGuiCrafting.proxy;


import gliese832c.SelectionGuiCrafting;
import gliese832c.selectionGuiCrafting.events.EventRightClick;
import gliese832c.selectionGuiCrafting.gui.ModGuiHandler;
import gliese832c.selectionGuiCrafting.network.SelectionMessageProcessRecipe;
import gliese832c.selectionGuiCrafting.network.SelectionPacketHandler;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionItemPair;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipe;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipeCategory;
import gliese832c.selectionGuiCrafting.recipe.RecipePairPair;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

public class CommonProxy
{
    public static ArrayList<GuiSelectionItemPair> selectionCraftingItems = new ArrayList<>(); // Valid items and their associated category
    public static HashMap<String, GuiSelectionRecipeCategory> recipeCategories = new HashMap<>();

    public void preInit(FMLPreInitializationEvent preEvent)
    {
        MinecraftForge.EVENT_BUS.register(new EventRightClick());

        SelectionPacketHandler.SELECTION_NETWORK_WRAPPER.registerMessage(SelectionMessageProcessRecipe.SelectionMessageProcessRecipeHandler.class, SelectionMessageProcessRecipe.class, 0, Side.SERVER);
    }

    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(SelectionGuiCrafting.instance, new ModGuiHandler()); // Register GUI handler
    }

    public void postInit(FMLPostInitializationEvent postEvent)
    {
        assert Blocks.BARRIER != null;

        ArrayList<GuiSelectionRecipe> invalidRecipes = new ArrayList<GuiSelectionRecipe>();
        invalidRecipes.add(new GuiSelectionRecipe("invalid", 1, new ItemStack[]{ new ItemStack(Blocks.BARRIER, 1) }, 20, 0));

        GuiSelectionRecipeCategory invalid = new GuiSelectionRecipeCategory("INVALID", invalidRecipes);

        ArrayList<ItemStack> barrierList = new ArrayList<ItemStack>(Collections.singletonList(new ItemStack(Item.getItemFromBlock(Blocks.BARRIER))));

        selectionCraftingItems.add(new GuiSelectionItemPair(barrierList, new ArrayList<Float>(Collections.singletonList(1.0f)), new ArrayList<Float>(Collections.singletonList(1.0f)), barrierList, "invalid"));
        recipeCategories.put("invalid", invalid);
    }

    public void serverStart(FMLServerStartingEvent serverStartEvent) {
    }

    public void onIdMapping(FMLModIdMappingEvent idMappingEvent)
    {

    }

    public void registerFluidBlockRendering(Block block, String name)
    {
        name = name.toLowerCase(Locale.ROOT);
    }

    public EntityPlayer getPlayerClient()
    {
        return null;
    }




    public static ArrayList<GuiSelectionRecipe> getAllRecipes() {
        ArrayList<GuiSelectionRecipe> recipesList = new ArrayList<>();
        for (GuiSelectionRecipeCategory recipeCategory : recipeCategories.values()) {
            recipesList.addAll(recipeCategory.recipes);
        }
        return recipesList;
    }

    public static GuiSelectionItemPair getPairFromCategory(String categoryName) {
        for (GuiSelectionItemPair itemPair : selectionCraftingItems) {
            if (Objects.equals(itemPair.recipeCategory, categoryName)) {
                return itemPair;
            }
        }
        return null;
    }

    public static ArrayList<ItemStack> getToolFromCategory(String categoryName) {
        for (GuiSelectionItemPair itemPair : selectionCraftingItems) {
            if (Objects.equals(itemPair.recipeCategory, categoryName)) {
                return itemPair.tool;
            }
        }
        return null;
    }

    public static ArrayList<ItemStack> getItemFromCategory(String categoryName) {
        for (GuiSelectionItemPair itemPair : selectionCraftingItems) {
            if (Objects.equals(itemPair.recipeCategory, categoryName)) {
                return itemPair.input;
            }
        }
        return null;
    }


    public static ArrayList<RecipePairPair> getAllRecipePairPairs() {
        ArrayList<RecipePairPair> recipesList = new ArrayList<>();
        for (Map.Entry<String, GuiSelectionRecipeCategory> recipeCategory : recipeCategories.entrySet()) {
            if (recipeCategory.getKey().equals("invalid"))
                continue;
            for (GuiSelectionRecipe recipe : recipeCategory.getValue().recipes) {
                recipesList.add(new RecipePairPair(getPairFromCategory(recipeCategory.getKey()), recipe));
            }
        }
        return recipesList;
    }
}