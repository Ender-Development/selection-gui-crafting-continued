package gliese832c.selectionGuiCrafting.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import gliese832c.selectionGuiCrafting.proxy.CommonProxy;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionItemPair;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipe;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipeCategory;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@ZenRegister
@SuppressWarnings("unused")
@ZenClass("mods.selectionGuiCrafting.SelectionGuiCrafting")
public final class CraftTweakerPlugin
{
    @ZenMethod
    public static void createCategory(String name, String displayName)
    {
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                CommonProxy.recipeCategories.put(name, new GuiSelectionRecipeCategory(displayName, new ArrayList<GuiSelectionRecipe>()));
                CommonProxy.selectionCraftingItems.add(new GuiSelectionItemPair(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), name));
            }

            @Override
            public String describe()
            {
                return "Creating a new Selection GUI Crafting recipe category '" + name + "', displayName '" + displayName + "'";
            }
        });
    }

    @ZenMethod
    public static void addToolsToCategory(String categoryName, IItemStack[] tools, Float[] durabilityMultipliers)
    {
        int i = 0;
        for (IItemStack itemStack : tools) {
            int finalI = i;
            CraftTweakerAPI.apply(new IAction()
            {
                @Override
                public void apply()
                {
                    GuiSelectionItemPair pair = CommonProxy.getPairFromCategory(categoryName);

                    assert pair != null;

                    pair.tool.add(toStack(itemStack).getItem());
                    pair.durabilityMultipliers.add(durabilityMultipliers[finalI]);
                }

                @Override
                public String describe()
                {
                    return "Adding tool " + itemStack.getDisplayName() + " to Selection GUI Crafting recipe category '" + categoryName + "'";
                }
            });
            i++;
        }
    }

    @ZenMethod
    public static void addDestructibleToolsToCategory(String categoryName, IItemStack[] tools)
    {
        for (IItemStack itemStack : tools) {
            CraftTweakerAPI.apply(new IAction()
            {
                @Override
                public void apply()
                {
                    GuiSelectionItemPair pair = CommonProxy.getPairFromCategory(categoryName);

                    assert pair != null;

                    pair.tool.add(toStack(itemStack).getItem());
                    pair.durabilityMultipliers.add(Float.MAX_VALUE);
                }

                @Override
                public String describe()
                {
                    return "Adding tool " + itemStack.getDisplayName() + " to Selection GUI Crafting recipe category '" + categoryName + "'";
                }
            });
        }
    }

    @ZenMethod
    public static void addInputToCategory(String categoryName, IItemStack[] inputs)
    {
        for (IItemStack itemStack : inputs) {
            CraftTweakerAPI.apply(new IAction()
            {
                @Override
                public void apply()
                {
                    CommonProxy.getPairFromCategory(categoryName).input.add(toStack(itemStack));
                }

                @Override
                public String describe()
                {
                    return "Adding input " + itemStack.getDisplayName() + " to Selection GUI Crafting recipe category '" + categoryName + "'";
                }
            });
        }
    }

    @ZenMethod
    public static void addRecipe(String categoryName, int inputQuantity, IItemStack[] outputs, int time, int durabilityUsage)
    //public static void addRecipe(String categoryName, int inputQuantity, ItemStack[] outputs, int time, float chance, float[] chances)
    {
        CraftTweakerAPI.apply(new IAction()
        {
            @Override
            public void apply()
            {
                ItemStack[] outputsConverted = new ItemStack[outputs.length];
                int i = 0;
                for (IItemStack itemStack : outputs) {
                    outputsConverted[i] = toStack(itemStack);
                    i++;
                }

                CommonProxy.recipeCategories.get(categoryName).recipes.add(new GuiSelectionRecipe(categoryName, inputQuantity, outputsConverted, time, durabilityUsage, 1.0f));
            }

            @Override
            public String describe()
            {
                StringBuilder outputsString = new StringBuilder();
                for (int i = 0; i < outputs.length; i++) {
                    outputsString.append(outputs[i].getDisplayName());
                    outputsString.append(", ");
                }
                outputsString = new StringBuilder(outputsString.substring(0, outputsString.length() - 2));

                return "Adding outputs " + outputsString.toString() + " to Selection GUI Crafting recipe category '" + categoryName + "'";
            }
        });
    }

    @Nonnull
    static ItemStack toStack(IIngredient ingredient)
    {
        if (!(ingredient instanceof IItemStack))
        {
            throw new IllegalArgumentException("Must be an IItemStack!");
        }
        Object obj = ingredient.getInternal();
        return obj instanceof ItemStack ? (ItemStack) obj : ItemStack.EMPTY;
    }
}