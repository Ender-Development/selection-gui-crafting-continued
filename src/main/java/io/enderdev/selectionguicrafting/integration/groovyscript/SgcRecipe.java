package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.recipe.GSEnum;
import io.enderdev.selectionguicrafting.recipe.GSRecipe;
import io.enderdev.selectionguicrafting.recipe.GSRecipeRegistry;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class SgcRecipe extends VirtualizedRegistry<GSRecipe> {
    @Override
    @GroovyBlacklist
    public void onReload() {
        GSRecipeRegistry.getRecipes().removeAll(removeScripted());
        GSRecipeRegistry.getRecipes().addAll(restoreFromBackup());
    }

    public void add(GSRecipe recipe) {
        if (recipe != null) {
            addScripted(recipe);
            GSRecipeRegistry.registerRecipe(recipe);
        }
    }

    public boolean remove(GSRecipe recipe) {
        if (GSRecipeRegistry.removeRecipe(recipe.getCategory(), recipe.getOutputs())) {
            addBackup(recipe);
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example(value = "'dummy_category'",commented = true), description = "sgc.groovyscript.recipe.remove_by_category")
    public boolean removeByCategory(String category) {
        return GSRecipeRegistry.getRecipes().removeIf(recipe -> {
            if (recipe.getCategory().equals(category)) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:stone')"))
    public boolean removeByOutput(IIngredient output) {
        return GSRecipeRegistry.getRecipes().removeIf(recipe -> {
            if (recipe.getOutputs().stream().anyMatch(output)) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:cobblestone')"))
    public boolean removeByInput(IIngredient input) {
        return GSRecipeRegistry.getRecipes().removeIf(recipe -> {
            if (recipe.getInputs().contains(input.toMcIngredient())) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:wool')"), description = "sgc.groovyscript.recipe.remove_by_tool")
    public boolean removeByTool(IIngredient tool) {
        return GSRecipeRegistry.getRecipes().removeIf(recipe -> {
            if (recipe.getTools().stream().anyMatch(tool)) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<GSRecipe> streamRecipes() {
        return new SimpleObjectStream<>(GSRecipeRegistry.getRecipes()).setRemover(this::remove);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
    public void removeAll() {
        GSRecipeRegistry.getRecipes().forEach(this::addBackup);
        GSRecipeRegistry.getRecipes().clear();
    }

    @RecipeBuilderDescription(example = {
            @Example(".category('dummy_category').input(item('minecraft:stone')).output(item('minecraft:cobblestone'), 0.5f).tool(item('minecraft:wooden_pickaxe'), 1.0f).time(200).xp(1).sound('minecraft:block.anvil.land')")
    })
    public RecipeBuilder createRecipe() {
        return new RecipeBuilder();
    }

    @Property(property = "input", comp = @Comp(gte = 1))
    public static class RecipeBuilder extends AbstractRecipeBuilder<GSRecipe> {

        @Property
        private final GSRecipe recipe = new GSRecipe();

        @RecipeBuilderMethodDescription(field = "recipe")
        public RecipeBuilder category(String category) {
            recipe.setCategory(category);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipe")
        public RecipeBuilder durability(int durability) {
            recipe.setDurability(durability);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipe")
        public RecipeBuilder consume(int amount) {
            recipe.setAmount(amount);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipe")
        public RecipeBuilder output(IIngredient output, float chance) {
            recipe.addOutput(output.getMatchingStacks()[0], chance);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipe")
        public RecipeBuilder tool(IIngredient tool, float damageMultiplier) {
            recipe.addTool(tool.getMatchingStacks()[0], damageMultiplier);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipe")
        public RecipeBuilder time(int time) {
            recipe.setTime(time);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipe")
        public RecipeBuilder xp(int xp) {
            recipe.setXp(xp);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipe")
        public RecipeBuilder sound(String sound) {
            recipe.addSound(new ResourceLocation(sound));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipe")
        public RecipeBuilder queueable(String queueable) {
            recipe.setQueueable(GSEnum.QueueType.valueOf(queueable));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipe")
        public RecipeBuilder outputType(String outputType) {
            recipe.setOutputType(GSEnum.OutputType.valueOf(outputType));
            return this;
        }

        @Override
        public String getErrorMsg() {
            return "Encountered an error while building a recipe for " + Tags.MOD_ID;
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateFluids(msg);
            validateItems(msg);
            msg.add(recipe.getCategory() == null, "Category is not set");
            msg.add(recipe.getInputs().isEmpty(), "Inputs are not set");
            msg.add(recipe.getOutputs().isEmpty(), "Outputs are not set");
            msg.add(recipe.getTools().isEmpty(), "Tools are not set");
        }

        @Override
        public @Nullable GSRecipe register() {
            if (!validate()) return null;
            input.stream().map(IIngredient::toMcIngredient).forEach(recipe::addInput);
            GSPlugin.instance.recipe.add(recipe);
            return recipe;
        }
    }
}
