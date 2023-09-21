package gliese832c.selectionGuiCrafting.proxy;


import gliese832c.SelectionGuiCrafting;
import gliese832c.selectionGuiCrafting.events.EventRightClick;
import gliese832c.selectionGuiCrafting.gui.ModGuiHandler;
import gliese832c.selectionGuiCrafting.network.SelectionMessageGiveItem;
import gliese832c.selectionGuiCrafting.network.SelectionPacketHandler;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionItemPair;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipe;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CommonProxy
{
    public static ArrayList<GuiSelectionItemPair> selectionCraftingItems = new ArrayList<>(); // Valid items and their associated category
    public static HashMap<String, GuiSelectionRecipeCategory> recipeCategories = new HashMap<>();

    public void preInit(FMLPreInitializationEvent preEvent)
    {
        MinecraftForge.EVENT_BUS.register(new EventRightClick());

        SelectionPacketHandler.SELECTION_NETWORK_WRAPPER.registerMessage(SelectionMessageGiveItem.SelectionMessageGiveItemHandler.class, SelectionMessageGiveItem.class, 0, Side.SERVER);
    }

    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(SelectionGuiCrafting.instance, new ModGuiHandler()); // Register GUI handler
    }

    public void postInit(FMLPostInitializationEvent postEvent)
    {
        assert Blocks.BARRIER != null;
        GuiSelectionRecipeCategory invalid = new GuiSelectionRecipeCategory("INVALID", true, new GuiSelectionRecipe[]{
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Blocks.BARRIER, 1) }, 20)
        });
        selectionCraftingItems.add(new GuiSelectionItemPair(new Item[]{Item.getItemFromBlock(Blocks.BARRIER)}, new Item[]{ Item.getItemFromBlock(Blocks.BARRIER) }, "invalid"));
        recipeCategories.put("invalid", invalid);





        GuiSelectionRecipeCategory schmongus = new GuiSelectionRecipeCategory("Schmongus", true, new GuiSelectionRecipe[]{
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.IRON_NUGGET, 2) }, 60),
                new GuiSelectionRecipe(2, new ItemStack[]{ new ItemStack(Items.BUCKET, 6) }, 30)
        });
        selectionCraftingItems.add(new GuiSelectionItemPair(new Item[]{ Items.IRON_PICKAXE }, new Item[]{ Items.IRON_INGOT }, "schmongus"));
        recipeCategories.put("schmongus", schmongus);

        GuiSelectionRecipeCategory schmongus2 = new GuiSelectionRecipeCategory("Schmongus 2", true, new GuiSelectionRecipe[]{
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.GOLD_NUGGET, 1) }, 60),
                new GuiSelectionRecipe(3, new ItemStack[]{ new ItemStack(Items.GOLDEN_APPLE, 5) }, 30)
        });
        selectionCraftingItems.add(new GuiSelectionItemPair(new Item[]{ Items.IRON_PICKAXE }, new Item[]{ Items.GOLD_INGOT }, "schmongus2"));
        recipeCategories.put("schmongus2", schmongus2);

        GuiSelectionRecipeCategory axing = new GuiSelectionRecipeCategory("Axing", true, new GuiSelectionRecipe[]{
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.BOAT, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WOODEN_AXE, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.ARMOR_STAND, 64) }, 30)
        });
        assert Blocks.PLANKS != null;
        selectionCraftingItems.add(new GuiSelectionItemPair(new Item[]{ Items.IRON_AXE }, new Item[]{ Item.getItemFromBlock(Blocks.PLANKS) }, "axing"));
        recipeCategories.put("axing", axing);

        GuiSelectionRecipeCategory shoveling = new GuiSelectionRecipeCategory("Shoveling", true, new GuiSelectionRecipe[]{
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.DIAMOND, 1) }, 90),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WATER_BUCKET, 5463) }, 60),
                new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.LAVA_BUCKET, 64) }, 30)
        });
        selectionCraftingItems.add(new GuiSelectionItemPair(new Item[]{ Items.IRON_SHOVEL }, new Item[]{ Items.EMERALD }, "shoveling"));
        recipeCategories.put("shoveling", shoveling);

        GuiSelectionRecipeCategory hoeing = new GuiSelectionRecipeCategory("Hoeing", true, new GuiSelectionRecipe[100]);
        for (int i = 0; i < 100; i++) {
            hoeing.recipes[i] = new GuiSelectionRecipe(1, new ItemStack[]{ new ItemStack(Items.WHEAT_SEEDS, 69) }, 10);
        }
        selectionCraftingItems.add(new GuiSelectionItemPair(new Item[]{ Items.IRON_HOE }, new Item[]{ Items.DIAMOND }, "hoeing"));
        recipeCategories.put("hoeing", hoeing);
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
}