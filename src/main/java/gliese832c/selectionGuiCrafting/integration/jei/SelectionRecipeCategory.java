package gliese832c.selectionGuiCrafting.integration.jei;

import com.google.common.collect.ImmutableList;
import gliese832c.SelectionGuiCrafting;
import gliese832c.selectionGuiCrafting.proxy.CommonProxy;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipe;
import gliese832c.selectionGuiCrafting.recipe.RecipePairPair;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static gliese832c.SelectionGuiCrafting.MOD_ID;
import static gliese832c.selectionGuiCrafting.integration.jei.JeiPlugin.SELECTION_UID;

@ParametersAreNonnullByDefault
public class SelectionRecipeCategory implements IRecipeCategory<SelectionRecipeCategory.Wrapper>
{
    private static final String TRANSLATION_KEY = "jei.category.selection_recipe";
    private static final ResourceLocation GUI_LOCATION = new ResourceLocation(MOD_ID, "textures/jei/selection.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SelectionRecipeCategory(IGuiHelper guiHelper)
    {
        background = guiHelper.createDrawable(GUI_LOCATION, 0, 0, 135, 18);
        icon = guiHelper.createDrawable(GUI_LOCATION, 135, 0, 16, 16);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return SELECTION_UID;
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public String getTitle()
    {
        return I18n.format(TRANSLATION_KEY);
    }

    @Nonnull
    @Override
    public String getModName()
    {
        return SelectionGuiCrafting.NAME;
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Wrapper wrapper, IIngredients ingredients)
    {
        int index = 0;
        recipeLayout.getItemStacks().init(index, true, 0, 0);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(0));

        index++;
        recipeLayout.getItemStacks().init(index, true, 54, 0);
        recipeLayout.getItemStacks().set(index, ingredients.getInputs(ItemStack.class).get(1));

        index++;
        recipeLayout.getItemStacks().init(index, false, 117, 0);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(ItemStack.class).get(0));
    }

    public static class Wrapper implements IRecipeWrapper
    {
        private final List<List<ItemStack>> input;
        private final List<List<ItemStack>> output;

        private final int durabilityUsage;

        public Wrapper(RecipePairPair recipe) {

            durabilityUsage = recipe.recipe.durabilityUsage;

            ImmutableList.Builder<List<ItemStack>> builder = ImmutableList.builder();

            // Add the valid input items and tools

            List<ItemStack> inputItems = recipe.pair.input;
            if (inputItems == null) {
                builder.add(Collections.singletonList(new ItemStack(Blocks.BARRIER)));
            } else {
                List<ItemStack> inputStacks = new ArrayList<>();
                for (ItemStack itemStack : inputItems) {
                    inputStacks.add(new ItemStack(itemStack.getItem(), recipe.recipe.inputQuantity, itemStack.getMetadata(), itemStack.getTagCompound()));
                }
                builder.add(inputStacks);
            }

            List<Item> inputTools = recipe.pair.tool;
            if (inputTools == null) {
                builder.add(Collections.singletonList(new ItemStack(Blocks.BARRIER)));
            } else {
                List<ItemStack> inputToolsList = new ArrayList<>();
                assert inputTools != null;
                for (Item tool : inputTools) {
                    inputToolsList.add(new ItemStack(tool));
                }
                builder.add(inputToolsList);
            }

            // Set the input
            input = builder.build();

            // Reset builder and add output
            builder = ImmutableList.builder();
            builder.add(Arrays.asList(recipe.recipe.outputs));

            // Set the output
            output = builder.build();
        }

        @Override
        public void getIngredients(@Nonnull IIngredients ingredients)
        {
            ingredients.setInputLists(ItemStack.class, input);
            ingredients.setOutputLists(ItemStack.class, output);
        }

        @Override
        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            if (durabilityUsage > 0) {
                minecraft.fontRenderer.drawString(String.valueOf(-durabilityUsage), 70, 14, Color.red.getRGB());
            }
            if (durabilityUsage < 0) {
                minecraft.fontRenderer.drawString("+" + String.valueOf(-durabilityUsage), 70, 14, Color.green.getRGB());
            }
        }
    }
}