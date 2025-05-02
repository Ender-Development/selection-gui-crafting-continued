package io.enderdev.selectionguicrafting.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenDoc;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlock;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.registry.category.*;
import io.enderdev.selectionguicrafting.registry.recipe.Recipe;
import io.enderdev.selectionguicrafting.registry.Register;
import io.enderdev.selectionguicrafting.registry.util.Particle;
import io.enderdev.selectionguicrafting.registry.util.Sound;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class CTsgc {

    @ZenRegister
    @ZenClass("mods.selectionguicrafting.category")
    public static class CTCategoryBuilder {
        private final Category category;

        public CTCategoryBuilder() {
            this.category = new Category();
        }

        @ZenMethod
        @ZenDoc("Create a new category")
        public static CTCategoryBuilder categoryBuilder() {
            return new CTCategoryBuilder();
        }

        @ZenMethod
        @ZenDoc("Set the ID of the category")
        public CTCategoryBuilder id(String id) {
            category.id(id);
            return this;
        }

        @ZenMethod
        @ZenDoc("Add a trigger item to the category")
        public CTCategoryBuilder trigger(IItemStack input, double damageMultiplier, double timeMultiplier, double xpMultiplier) {
            category.trigger((ItemStack) input.getInternal(), damageMultiplier, timeMultiplier, xpMultiplier);
            return this;
        }

        @ZenMethod
        @ZenDoc("Add a trigger item to the category")
        public CTCategoryBuilder trigger(IItemStack input) {
            category.trigger((ItemStack) input.getInternal());
            return this;
        }

        @ZenMethod
        @ZenDoc("Add a trigger item to the category")
        public CTCategoryBuilder trigger(IIngredient input, double damageMultiplier, double timeMultiplier, double xpMultiplier) {
            category.trigger(Ingredient.fromStacks((ItemStack) input.getInternal()), damageMultiplier, timeMultiplier, xpMultiplier);
            return this;
        }

        @ZenMethod
        @ZenDoc("Add a trigger item to the category")
        public CTCategoryBuilder trigger(IIngredient input) {
            category.trigger(Ingredient.fromStacks((ItemStack) input.getInternal()));
            return this;
        }

        @ZenMethod
        @ZenDoc("Add a trigger block to the category")
        public CTCategoryBuilder trigger(IBlock input, double damageMultiplier, double timeMultiplier, double xpMultiplier) {
            category.trigger((Block) input.getDefinition().getInternal(), damageMultiplier, timeMultiplier, xpMultiplier);
            return this;
        }

        @ZenMethod
        @ZenDoc("Add a trigger block to the category")
        public CTCategoryBuilder trigger(IBlock input) {
            category.trigger((Block) input.getDefinition().getInternal());
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the path to the background texture for the category")
        public CTCategoryBuilder background(String background) {
            category.setBackground(new ResourceLocation(background));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the path to the border texture for the category")
        public CTCategoryBuilder border(String border) {
            category.setBorder(new ResourceLocation(border));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the path to the frame texture for the category")
        public CTCategoryBuilder frame(String frame) {
            category.setFrame(new ResourceLocation(frame));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the path to the progress bar texture for the category")
        public CTCategoryBuilder bar(String progressBar) {
            category.setProgressBar(new ResourceLocation(progressBar));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the path to the decoration texture for the category")
        public CTCategoryBuilder decoration(String decoration) {
            category.setDecoration(new ResourceLocation(decoration));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the output type for the recipe. Possible values: DROP, INVENTORY")
        public CTCategoryBuilder outputType(String outputType) {
            category.setOutputType(OutputType.valueOf(outputType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the sound type for the recipe. Possible values: RANDOM, COMBINED")
        public CTCategoryBuilder soundType(String soundType) {
            category.setSoundType(SoundType.valueOf(soundType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the background type for the recipe. Possible values: SINGLE_STRETCH, SINGLE_CUT, TILE")
        public CTCategoryBuilder backgroundType(String backgroundType) {
            category.setBackgroundType(BackgroundType.valueOf(backgroundType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the queueable type for the recipe. Possible values: YES, NO")
        public CTCategoryBuilder queueable(String queueType) {
            category.setQueueable(QueueType.valueOf(queueType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the queueable type for the recipe. Possible values: true, false")
        public CTCategoryBuilder queueable(boolean queueType) {
            category.setQueueable(queueType ? QueueType.YES : QueueType.NO);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a sound to the category")
        public CTCategoryBuilder sound(String sound, float volume, float pitch) {
            category.addSound(new Sound(new ResourceLocation(sound), volume, pitch));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a particle to the category")
        public CTCategoryBuilder particle(String particle, int count, float speed) {
            category.addParticle(new Particle(EnumParticleTypes.valueOf(particle), count, speed));
            return this;
        }

        @ZenMethod
        @ZenDoc("Register the category")
        public void register() {
            if (!category.validate()) {
                throw new IllegalArgumentException("Category is not valid: " + category.getID());
            }
            Register.addCategory(category);
        }

        @Override
        @ZenMethod
        public String toString() {
            return "CTCategoryBuilder{category={id=" + category.getID() + "}}";
        }

        @ZenMethod
        @ZenDoc("Removes a category by its ID.")
        public static void removeByName(String categoryName) {
            CraftTweakerAPI.apply(new IAction() {
                @Override
                public void apply() {
                    Register.removeCategory(Register.getCategoryByID(categoryName));
                }

                @Override
                public String describe() {
                    return "Removing Selection GUI Crafting recipe category '" + categoryName + "'";
                }
            });
        }

        @ZenMethod
        @ZenDoc("Removes all categories.")
        public static void removeAllCategories() {
            CraftTweakerAPI.apply(new IAction() {
                @Override
                public void apply() {
                    Register.getCategories().clear();
                }

                @Override
                public String describe() {
                    return "Removing all Selection GUI Crafting recipe categories";
                }
            });
        }
    }

    @ZenRegister
    @ZenClass("mods.selectionguicrafting.recipe")
    public static class CTRecipeBuilder {
        private final Recipe recipe;

        public CTRecipeBuilder() {
            this.recipe = new Recipe();
        }

        @ZenMethod
        @ZenDoc("Create a new recipe")
        public static CTRecipeBuilder recipeBuilder() {
            return new CTRecipeBuilder();
        }

        @ZenMethod
        @ZenDoc("Set the category for the recipe")
        public CTRecipeBuilder category(String category) {
            recipe.category(category);
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the path to the frame texture for the recipe")
        public CTRecipeBuilder frame(String frame) {
            recipe.setFrame(new ResourceLocation(frame));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the path to the progress bar texture for the recipe")
        public CTRecipeBuilder progressBar(String progressBar) {
            recipe.setProgressBar(new ResourceLocation(progressBar));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the output type for the recipe. Possible values: DROP, INVENTORY")
        public CTRecipeBuilder outputType(String outputType) {
            recipe.setOutputType(OutputType.valueOf(outputType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the queueable type for the recipe. Possible values: YES, NO")
        public CTRecipeBuilder queueable(String queueable) {
            recipe.setQueueable(QueueType.valueOf(queueable));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the queueable type for the recipe. Possible values: true, false")
        public CTRecipeBuilder queueable(boolean queueable) {
            recipe.setQueueable(queueable ? QueueType.YES : QueueType.NO);
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the sound type for the recipe. Possible values: RANDOM, COMBINED")
        public CTRecipeBuilder soundType(String soundType) {
            recipe.setSoundType(SoundType.valueOf(soundType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a sound to the recipe")
        public CTRecipeBuilder sound(String sound, float volume, float pitch) {
            recipe.addSound(new Sound(new ResourceLocation(sound), volume, pitch));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a particle to the recipe")
        public CTRecipeBuilder particle(String particle, int count, float speed) {
            recipe.addParticle(new Particle(EnumParticleTypes.valueOf(particle), count, speed));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an input to the recipe")
        public CTRecipeBuilder input(IIngredient input) {
            recipe.input(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an input to the recipe")
        public CTRecipeBuilder input(IItemStack input) {
            recipe.input(Ingredient.fromStacks((ItemStack) input.getInternal()));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an input to the recipe")
        public CTRecipeBuilder input(IIngredient input, int damage) {
            recipe.input(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())), damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an input to the recipe")
        public CTRecipeBuilder input(IItemStack input, int damage) {
            recipe.input(Ingredient.fromStacks((ItemStack) input.getInternal()), damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an input to the recipe")
        public CTRecipeBuilder input(IIngredient input, double chance) {
            recipe.input(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())), chance);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an input to the recipe")
        public CTRecipeBuilder input(IItemStack input, double chance) {
            recipe.input(Ingredient.fromStacks((ItemStack) input.getInternal()), chance);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an input to the recipe")
        public CTRecipeBuilder input(IIngredient input, int damage, double chance) {
            recipe.input(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())), chance, damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an input to the recipe")
        public CTRecipeBuilder input(IItemStack input, int damage, double chance) {
            recipe.input(Ingredient.fromStacks((ItemStack) input.getInternal()), chance, damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a main hand input to the recipe")
        public CTRecipeBuilder mainHand(IIngredient input) {
            recipe.mainHand(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a main hand input to the recipe")
        public CTRecipeBuilder mainHand(IItemStack input) {
            recipe.mainHand(Ingredient.fromStacks((ItemStack) input.getInternal()));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a main hand input to the recipe")
        public CTRecipeBuilder mainHand(IIngredient input, int damage) {
            recipe.mainHand(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())), damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a main hand input to the recipe")
        public CTRecipeBuilder mainHand(IItemStack input, int damage) {
            recipe.mainHand(Ingredient.fromStacks((ItemStack) input.getInternal()), damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a main hand input to the recipe")
        public CTRecipeBuilder mainHand(IIngredient input, double chance) {
            recipe.mainHand(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())), chance);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a main hand input to the recipe")
        public CTRecipeBuilder mainHand(IItemStack input, double chance) {
            recipe.mainHand(Ingredient.fromStacks((ItemStack) input.getInternal()), chance);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a main hand input to the recipe")
        public CTRecipeBuilder mainHand(IIngredient input, int damage, double chance) {
            recipe.mainHand(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())), chance, damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a main hand input to the recipe")
        public CTRecipeBuilder mainHand(IItemStack input, int damage, double chance) {
            recipe.mainHand(Ingredient.fromStacks((ItemStack) input.getInternal()), chance, damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an off hand input to the recipe")
        public CTRecipeBuilder offHand(IIngredient input) {
            recipe.offHand(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an off hand input to the recipe")
        public CTRecipeBuilder offHand(IItemStack input) {
            recipe.offHand(Ingredient.fromStacks((ItemStack) input.getInternal()));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an off hand input to the recipe")
        public CTRecipeBuilder offHand(IIngredient input, int damage) {
            recipe.offHand(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())), damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an off hand input to the recipe")
        public CTRecipeBuilder offHand(IItemStack input, int damage) {
            recipe.offHand(Ingredient.fromStacks((ItemStack) input.getInternal()), damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an off hand input to the recipe")
        public CTRecipeBuilder offHand(IIngredient input, double chance) {
            recipe.offHand(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())), chance);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an off hand input to the recipe")
        public CTRecipeBuilder offHand(IItemStack input, double chance) {
            recipe.offHand(Ingredient.fromStacks((ItemStack) input.getInternal()), chance);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an off hand input to the recipe")
        public CTRecipeBuilder offHand(IIngredient input, int damage, double chance) {
            recipe.offHand(Ingredient.merge(OreDictionary.getOres((String) input.getInternal()).stream().map(Ingredient::fromStacks).collect(Collectors.toList())), chance, damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an off hand input to the recipe")
        public CTRecipeBuilder offHand(IItemStack input, int damage, double chance) {
            recipe.offHand(Ingredient.fromStacks((ItemStack) input.getInternal()), chance, damage);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an output to the recipe")
        public CTRecipeBuilder output(IIngredient output, float chance) {
            recipe.output((ItemStack) output.getInternal(), chance);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an output to the recipe")
        public CTRecipeBuilder output(IItemStack output, float chance) {
            recipe.output((ItemStack) output.getInternal(), chance);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an output to the recipe")
        public CTRecipeBuilder output(IIngredient output) {
            recipe.output((ItemStack) output.getInternal());
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an output to the recipe")
        public CTRecipeBuilder output(IItemStack output) {
            recipe.output((ItemStack) output.getInternal());
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the time it takes to craft this recipe")
        public CTRecipeBuilder time(int time) {
            recipe.time(time);
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the amount of experience that is given when crafting this recipe")
        public CTRecipeBuilder xp(int xp) {
            recipe.xp(xp);
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the to execute when crafting the recipe")
        public CTRecipeBuilder command(String command) {
            recipe.command(command);
            return this;
        }

        @ZenMethod
        @ZenDoc("Register the recipe")
        public void register() {
            if (!recipe.validate()) {
                throw new IllegalArgumentException("Recipe is not valid: " + recipe.getErrorCheck().msg());
            }
            Register.addRecipe(recipe);
        }

        @Override
        @ZenMethod
        public String toString() {
            return "CTRecipeBuilder{category={id=" + recipe.getCategory() + "}}";
        }

        @ZenMethod
        @ZenDoc("Removes all recipes.")
        public static void removeAllRecipes() {
            CraftTweakerAPI.apply(new IAction() {
                @Override
                public void apply() {
                    Register.getRecipes().clear();
                }

                @Override
                public String describe() {
                    return "Removing all Selection GUI Crafting recipes";
                }
            });
        }
    }
}
