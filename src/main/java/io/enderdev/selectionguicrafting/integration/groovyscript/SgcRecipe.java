package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.IRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.registry.*;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class SgcRecipe extends VirtualizedRegistry<GsRecipe> {
    @Override
    @GroovyBlacklist
    public void onReload() {
        GsRegistry.getRecipes().removeAll(removeScripted());
        GsRegistry.getRecipes().addAll(restoreFromBackup());
    }

    public void add(GsRecipe recipe) {
        if (recipe != null) {
            addScripted(recipe);
            GsRegistry.registerRecipe(recipe);
        }
    }

    public boolean remove(GsRecipe recipe) {
        if (GsRegistry.removeRecipe(recipe.getCategory(), recipe.getOutput())) {
            addBackup(recipe);
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example(value = "'dummy_category'", commented = true), description = "sgc.groovyscript.recipe.remove_by_category")
    public boolean removeByCategory(String category) {
        return GsRegistry.getRecipes().removeIf(recipe -> {
            if (recipe.getCategory().equals(category)) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:stone')"))
    public boolean removeByOutput(IIngredient output) {
        return GsRegistry.getRecipes().removeIf(recipe -> {
            if (recipe.getOutput().stream().map(GsOutput::getItemStack).anyMatch(output)) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:cobblestone')"))
    public boolean removeByInput(IIngredient input) {
        return GsRegistry.getRecipes().removeIf(recipe -> {
            if (recipe.getInput().contains(input.toMcIngredient())) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:wool')"), description = "sgc.groovyscript.recipe.remove_by_tool")
    public boolean removeByTool(IIngredient tool) {
        return GsRegistry.getRecipes().removeIf(recipe -> {
            if (recipe.getTool().stream().map(GsTool::getItemStack).anyMatch(tool)) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<GsRecipe> streamRecipes() {
        return new SimpleObjectStream<>(GsRegistry.getRecipes()).setRemover(this::remove);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
    public void removeAll() {
        GsRegistry.getRecipes().forEach(this::addBackup);
        GsRegistry.getRecipes().clear();
    }

    @RecipeBuilderDescription(example = {
            @Example(".category('dummy_category').input(item('minecraft:stone') * 3).output(item('minecraft:cobblestone') * 2, 0.5f).tool(item('minecraft:wooden_pickaxe'), 1.0f).time(200).xp(1).sound('minecraft:block.anvil.land', 1.0f, 1.0f)"),
            @Example(".category('blub').input(item('minecraft:diamond')).output(item('minecraft:wheat_seeds') * 5, 0.5f).tool(item('minecraft:grass') * 5, 1.0f)"),
            @Example(".category('dummy_category').input(item('minecraft:stone') * 32).output(item('minecraft:diamond') * 50, 0.5f).output(item('minecraft:clay') * 2, 0.1f).tool(item('minecraft:wooden_pickaxe'), 1.0f).tool(item('minecraft:diamond_pickaxe'), 10.0f, 10.0f).durability(10).time(200).xp(1).sound('minecraft:block.anvil.land', 1.0f, 1.0f)")
    })
    public RecipeBuilder newRecipe() {
        return new RecipeBuilder();
    }

    @Property(property = "category", comp = @Comp(not = "null"))
    @Property(property = "input", comp = @Comp(gte = 1))
    @Property(property = "output", comp = @Comp(gte = 1))
    @Property(property = "tool", comp = @Comp(gte = 1))
    @Property(property = "time", comp = @Comp(gte = 0))
    @Property(property = "xp", comp = @Comp(gte = 0))
    @Property(property = "durability", comp = @Comp(gte = 0))
    @Property(property = "sounds")
    @Property(property = "particles")
    @Property(property = "frame")
    @Property(property = "progressBar")
    @Property(property = "outputType")
    @Property(property = "queueable")
    @Property(property = "soundType")
    public static class RecipeBuilder extends GsRecipe implements IRecipeBuilder<GsRecipe> {

        @RecipeBuilderMethodDescription(field = "category")
        public RecipeBuilder category(String category) {
            super.setCategory(category);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "frame")
        public RecipeBuilder frame(String frame) {
            super.setFrame(new ResourceLocation(frame));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "progressBar")
        public RecipeBuilder progressBar(String progressBar) {
            super.setProgressBar(new ResourceLocation(progressBar));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "outputType")
        public RecipeBuilder outputType(String outputType) {
            super.setOutputType(GsEnum.OutputType.valueOf(outputType));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public RecipeBuilder queueable(String queueable) {
            super.setQueueable(GsEnum.QueueType.valueOf(queueable));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "soundType")
        public RecipeBuilder soundType(String soundType) {
            super.setSoundType(GsEnum.SoundType.valueOf(soundType));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "sounds")
        public RecipeBuilder sound(String sound, float volume, float pitch) {
            super.addSound(new ResourceLocation(sound), volume, pitch);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particles")
        public RecipeBuilder particle(String particle, int count, float speed) {
            super.addParticle(EnumParticleTypes.valueOf(particle), count, speed);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(IIngredient input) {
            super.addInput(input.toMcIngredient());
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(IIngredient output, float chance) {
            super.addOutput(output.getMatchingStacks()[0], chance);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(IIngredient output) {
            super.addOutput(output.getMatchingStacks()[0], 1);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "tool")
        public RecipeBuilder tool(IIngredient tool, float damageMultiplier, float timeMultiplier) {
            super.addTool(tool.getMatchingStacks()[0], damageMultiplier, timeMultiplier);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "tool")
        public RecipeBuilder tool(IIngredient tool, float timeMultiplier) {
            super.addTool(tool.getMatchingStacks()[0], 1, timeMultiplier);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "tool")
        public RecipeBuilder tool(IIngredient tool) {
            super.addTool(tool.getMatchingStacks()[0], 1, 1);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "time")
        public RecipeBuilder time(int time) {
            super.setTime(time);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "xp")
        public RecipeBuilder xp(int xp) {
            super.setXp(xp);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "durability")
        public RecipeBuilder durability(int durability) {
            super.setDurability(durability);
            return this;
        }

        @Override
        public boolean validate() {
            return super.getCategory() != null && !super.getInput().isEmpty() && !super.getOutput().isEmpty() && !super.getTool().isEmpty();
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable GsRecipe register() {
            if (!validate()) {
                return null;
            }
            GSPlugin.instance.recipe.add(this);
            return this;
        }
    }
}
