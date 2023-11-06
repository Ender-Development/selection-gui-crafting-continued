package gliese832c;

import gliese832c.selectionGuiCrafting.events.EventRightClick;
import gliese832c.selectionGuiCrafting.gui.ModGuiHandler;
import gliese832c.selectionGuiCrafting.proxy.CommonProxy;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipe;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipeCategory;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.Map;

@Mod(modid = SelectionGuiCrafting.MOD_ID, name = SelectionGuiCrafting.NAME, version = SelectionGuiCrafting.VERSION)
public class SelectionGuiCrafting {

    public static final String MOD_ID = "selectionguicrafting";
    public static final String NAME = "Selection GUI Crafting";
    public static final String VERSION = "1.0.2";



    @Mod.Instance(MOD_ID)
    public static SelectionGuiCrafting instance;

    @SidedProxy(clientSide = "gliese832c.selectionGuiCrafting.proxy.ClientProxy", serverSide = "gliese832c.selectionGuiCrafting.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        proxy.postInit(event);
    }

}