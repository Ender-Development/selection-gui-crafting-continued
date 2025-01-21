package io.enderdev.selectionguicrafting.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenDoc;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import io.enderdev.selectionguicrafting.registry.GsCategory;
import io.enderdev.selectionguicrafting.registry.GsEnum;
import io.enderdev.selectionguicrafting.registry.GsRecipe;
import io.enderdev.selectionguicrafting.registry.GsRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@SuppressWarnings("unused")
@ZenRegister
@ZenClass("mods.selectionguicrafting.sgc")
public final class CTsgc {

    @ZenMethod
    public static void removeByName(String categoryName) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                GsRegistry.removeCategory(categoryName);
            }

            @Override
            public String describe() {
                return "Removing Selection GUI Crafting recipe category '" + categoryName + "'";
            }
        });
    }

    @ZenMethod
    public static void removeAllCategories() {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                GsRegistry.getCategories().clear();
            }

            @Override
            public String describe() {
                return "Removing all Selection GUI Crafting recipe categories";
            }
        });
    }

    @ZenMethod
    public static void removeAllRecipes() {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                GsRegistry.getRecipes().clear();
            }

            @Override
            public String describe() {
                return "Removing all Selection GUI Crafting recipes";
            }
        });
    }

    @ZenRegister
    @ZenClass("mods.selectionguicrafting.sgc_category")
    public static class CTCategoryBuilder {
        private final GsCategory category;

        public CTCategoryBuilder() {
            this.category = new GsCategory();
        }

        @ZenMethod
        @ZenDoc("Create a new category")
        public static CTCategoryBuilder newCategory() {
            return new CTCategoryBuilder();
        }

        @ZenMethod
        @ZenDoc("Set the ID of the category")
        public CTCategoryBuilder id(String id) {
            category.setId(id);
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the display name of the category")
        public CTCategoryBuilder displayName(String displayName) {
            category.setDisplayName(displayName);
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
            category.setOutputType(GsEnum.OutputType.valueOf(outputType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the sound type for the recipe. Possible values: RANDOM, COMBINED")
        public CTCategoryBuilder soundType(String soundType) {
            category.setSoundType(GsEnum.SoundType.valueOf(soundType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the background type for the recipe. Possible values: SINGLE_STRETCH, SINGLE_CUT, TILE")
        public CTCategoryBuilder backgroundType(String backgroundType) {
            category.setBackgroundType(GsEnum.BackgroundType.valueOf(backgroundType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the queueable type for the recipe. Possible values: YES, NO")
        public CTCategoryBuilder queueType(String queueType) {
            category.setQueueable(GsEnum.QueueType.valueOf(queueType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a sound to the category")
        public CTCategoryBuilder sound(String sound, float volume, float pitch) {
            category.addSound(new ResourceLocation(sound), volume, pitch);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a particle to the category")
        public CTCategoryBuilder particle(String particle, int count, float speed) {
            category.addParticle(EnumParticleTypes.valueOf(particle), count, speed);
            return this;
        }

        @ZenMethod
        @ZenDoc("Register the category")
        public void register() {
            if (category.getId() == null || category.getDisplayName() == null) {
                throw new IllegalArgumentException("Category ID and display name must be set before registering");
            }
            GsRegistry.registerCategory(category);
        }

        @Override
        @ZenMethod
        public String toString() {
            return "CTCategoryBuilder{category={id=" + category.getId() + "}, {displayName=" + category.getDisplayName() + "}}";
        }
    }

    @ZenRegister
    @ZenClass("mods.selectionguicrafting.sgc_recipe")
    public static class CTRecipeBuilder {
        private final GsRecipe recipe;

        public CTRecipeBuilder() {
            this.recipe = new GsRecipe();
        }

        @ZenMethod
        @ZenDoc("Create a new recipe")
        public static CTRecipeBuilder newRecipe() {
            return new CTRecipeBuilder();
        }

        @ZenMethod
        @ZenDoc("Set the category for the recipe")
        public CTRecipeBuilder category(String category) {
            recipe.setCategory(category);
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
            recipe.setOutputType(GsEnum.OutputType.valueOf(outputType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the queueable type for the recipe. Possible values: YES, NO")
        public CTRecipeBuilder queueable(String queueable) {
            recipe.setQueueable(GsEnum.QueueType.valueOf(queueable));
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the sound type for the recipe. Possible values: RANDOM, COMBINED")
        public CTRecipeBuilder soundType(String soundType) {
            recipe.setSoundType(GsEnum.SoundType.valueOf(soundType));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a sound to the recipe")
        public CTRecipeBuilder sound(String sound, float volume, float pitch) {
            recipe.addSound(new ResourceLocation(sound), volume, pitch);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a particle to the recipe")
        public CTRecipeBuilder particle(String particle, int count, float speed) {
            recipe.addParticle(EnumParticleTypes.valueOf(particle), count, speed);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an input to the recipe")
        public CTRecipeBuilder input(IIngredient input) {
            recipe.addInput(Ingredient.fromStacks((ItemStack) input.getInternal()));
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an output to the recipe, with a specified chance")
        public CTRecipeBuilder output(IIngredient output, float chance) {
            recipe.addOutput((ItemStack) output.getInternal(), chance);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds an output to the recipe, with a chance of 1.0")
        public CTRecipeBuilder output(IIngredient output) {
            recipe.addOutput((ItemStack) output.getInternal(), 1);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a tool to the recipe, specifying the damage and time multipliers")
        public CTRecipeBuilder tool(IIngredient tool, float damageMultiplier, float timeMultiplier) {
            recipe.addTool((ItemStack) tool.getInternal(), damageMultiplier, timeMultiplier);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a tool to the recipe, without specifying the time multiplier")
        public CTRecipeBuilder tool(IIngredient tool, float timeMultiplier) {
            recipe.addTool((ItemStack) tool.getInternal(), 1, timeMultiplier);
            return this;
        }

        @ZenMethod
        @ZenDoc("Adds a tool to the recipe, without specifying the damage and time multipliers")
        public CTRecipeBuilder tool(IIngredient tool) {
            recipe.addTool((ItemStack) tool.getInternal(), 1, 1);
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the time it takes to craft this recipe")
        public CTRecipeBuilder time(int time) {
            recipe.setTime(time);
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the amount of experience that is given when crafting this recipe")
        public CTRecipeBuilder xp(int xp) {
            recipe.setXp(xp);
            return this;
        }

        @ZenMethod
        @ZenDoc("Set the amount of durability that is conumed when crafting this recipe")
        public CTRecipeBuilder durability(int durability) {
            recipe.setDurability(durability);
            return this;
        }

        @ZenMethod
        @ZenDoc("Register the recipe")
        public void register() {
            if (recipe.getCategory() == null) {
                throw new IllegalArgumentException("Category must be set before registering");
            }
            if (recipe.getInput().isEmpty() || recipe.getOutput().isEmpty() || recipe.getTool().isEmpty()) {
                throw new IllegalArgumentException("Input, output, and tool must be set before registering");
            }
            GsRegistry.registerRecipe(recipe);
        }

        @Override
        @ZenMethod
        public String toString() {
            return "CTRecipeBuilder{category={id=" + recipe.getCategory() + "}, {outputs=" + recipe.getOutput() + "}}";
        }
    }
}
