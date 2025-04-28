package io.enderdev.selectionguicrafting.integration.jei;

import io.enderdev.selectionguicrafting.gui.Assets;
import io.enderdev.selectionguicrafting.registry.Register;
import io.enderdev.selectionguicrafting.registry.category.BlockTrigger;
import io.enderdev.selectionguicrafting.registry.category.ItemTrigger;
import io.enderdev.selectionguicrafting.registry.recipe.Recipe;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeHelper;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeInput;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeOutput;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;

public class GsGuiWrapper implements IRecipeWrapper {
    private final Recipe recipe;
    private final RecipeHelper recipeHelper;
    private final List<ItemStack> trigger = new ArrayList<>();
    private final List<ItemStack> mainHand = new ArrayList<>();
    private final List<ItemStack> offHand = new ArrayList<>();
    private final List<ItemStack> input = new ArrayList<>();
    private final List<ItemStack> output = new ArrayList<>();
    private final List<Double> inputChance = new ArrayList<>();
    private final List<Double> outputChance = new ArrayList<>();
    private final List<List<Double>> triggerStats = new ArrayList<>();

    public GsGuiWrapper(Recipe recipe) {
        this.recipe = recipe;
        this.recipeHelper = new RecipeHelper(recipe);
    }

    @Override
    public void getIngredients(@NotNull IIngredients iIngredients) {
        trigger.clear();
        mainHand.clear();
        offHand.clear();
        input.clear();
        output.clear();
        inputChance.clear();
        outputChance.clear();

        trigger.addAll(Register.getCategoryByID(recipe.getCategory()).getTriggerItems().stream().map(ItemTrigger::getTriggerItem).collect(Collectors.toList()));
        triggerStats.addAll(Register.getCategoryByID(recipe.getCategory()).getTriggerItems().stream().map(itemTrigger -> new ArrayList<Double>() {{
            add(itemTrigger.getDamageMultiplier());
            add(itemTrigger.getTimeMultiplier());
            add(itemTrigger.getXpMultiplier());
        }}).collect(Collectors.toList()));

        trigger.addAll(Register.getCategoryByID(recipe.getCategory()).getTriggerBlocks().stream().map(BlockTrigger::getTriggerBlock).map(ItemStack::new).collect(Collectors.toList()));
        triggerStats.addAll(Register.getCategoryByID(recipe.getCategory()).getTriggerBlocks().stream().map(blockTrigger -> new ArrayList<Double>() {{
            add(blockTrigger.getDamageMultiplier());
            add(blockTrigger.getTimeMultiplier());
            add(blockTrigger.getXpMultiplier());
        }}).collect(Collectors.toList()));

        input.addAll(recipe.getInputs().stream().map(RecipeInput::getIngredient).map(Ingredient::getMatchingStacks).flatMap(Arrays::stream).map(itemStack -> {
            ItemStack stack = new ItemStack(itemStack.getItem());
            stack.setCount(itemStack.getCount());
            stack.setItemDamage(itemStack.getItemDamage());
            stack.setTagCompound(itemStack.getTagCompound());
            return stack;
        }).collect(Collectors.toList()));

        inputChance.addAll(recipe.getInputs().stream().map(RecipeInput::getChance).collect(Collectors.toList()));

        output.addAll(recipe.getOutputs().stream().map(RecipeOutput::getItemStack).collect(Collectors.toList()));
        outputChance.addAll(recipe.getOutputs().stream().map(RecipeOutput::getChance).collect(Collectors.toList()));

        if (recipeHelper.hasMainHand()) {
            mainHand.addAll(Arrays.stream(recipe.getMainHand().getIngredient().getMatchingStacks()).filter(Objects::nonNull).collect(Collectors.toList()));
        } else {
            mainHand.add(ItemStack.EMPTY);
        }

        if (recipeHelper.hasOffHand()) {
            offHand.addAll(Arrays.stream(recipe.getOffHand().getIngredient().getMatchingStacks()).filter(Objects::nonNull).collect(Collectors.toList()));
        } else {
            offHand.add(ItemStack.EMPTY);
        }

        List<List<ItemStack>> inputs = Arrays.asList(trigger, mainHand, offHand, input);
        List<List<ItemStack>> outputs = Collections.singletonList(output);

        iIngredients.setInputLists(VanillaTypes.ITEM, inputs);
        iIngredients.setOutputLists(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (minecraft.currentScreen == null) {
            return;
        }
        minecraft.fontRenderer.drawString(I18n.format("jei.selectionguicrafting.crafting_time", String.format("%.1f", ((float) recipe.getTime() / 20))), 0, 150, Color.WHITE.getRGB());
        if (recipeHelper.hasMainHand()) {
            minecraft.fontRenderer.drawString(I18n.format("jei.selectionguicrafting.mainhand"), 12, 0, Color.WHITE.getRGB());
            minecraft.getTextureManager().bindTexture(Assets.JEI_SELECTION.get());
            drawModalRectWithCustomSizedTexture(21, 9, 160, 40, 18, 18, 256, 256);
        }
        if (recipeHelper.hasOffHand()) {
            minecraft.fontRenderer.drawString(I18n.format("jei.selectionguicrafting.offhand"), 12, 67, Color.WHITE.getRGB());
            minecraft.getTextureManager().bindTexture(Assets.JEI_SELECTION.get());
            drawModalRectWithCustomSizedTexture(21, 49, 160, 58, 18, 18, 256, 256);
        }
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        ArrayList<String> tooltips = new ArrayList<>();
        if (isMouseOver(mouseX, mouseY, 114, 31, 18, 14)) {
            tooltips.add(I18n.format("jei.selectionguicrafting.output"));
            tooltips.addAll(output.stream().map(itemStack -> "- " + I18n.format("jei.selectionguicrafting.output.entry", itemStack.getCount(), itemStack.getDisplayName(), outputChance.get(output.indexOf(itemStack)) * 100)).collect(Collectors.toList()));
            tooltips.add(I18n.format("jei.selectionguicrafting.input"));
            tooltips.addAll(input.stream().map(itemStack -> "- " + I18n.format("jei.selectionguicrafting.input.entry", itemStack.getCount(), itemStack.getDisplayName(), inputChance.get(input.indexOf(itemStack)) * 100)).collect(Collectors.toList()));
        }
        if (isMouseOver(mouseX, mouseY, 0, 31, 6, 14)) {
            tooltips.add(I18n.format("jei.selectionguicrafting.trigger"));
            trigger.forEach(item -> {
                double damage = triggerStats.get(trigger.indexOf(item)).get(0);
                double time = triggerStats.get(trigger.indexOf(item)).get(1);
                double xp = triggerStats.get(trigger.indexOf(item)).get(2);
                tooltips.add(I18n.format("jei.selectionguicrafting.trigger.item", item.getDisplayName()));
                if (damage != 1.0)
                    tooltips.add(I18n.format("jei.selectionguicrafting.trigger.damage", damage));
                if (time != 1.0)
                    tooltips.add(I18n.format("jei.selectionguicrafting.trigger.time", time));
                if (xp != 1.0)
                    tooltips.add(I18n.format("jei.selectionguicrafting.trigger.xp", xp));
                if (damage == 1.0 && time == 1.0 && xp == 1.0)
                    tooltips.add(I18n.format("jei.selectionguicrafting.trigger.nomodifier"));
            });
        }
        if (isMouseOver(mouseX, mouseY, 37, 30, 16, 16)) {
            // NO-OP
        }
        return tooltips;
    }

    @Override
    public boolean handleClick(@NotNull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
