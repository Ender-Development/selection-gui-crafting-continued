package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.IRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.registry.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class Recipe extends VirtualizedRegistry<GsRecipe> {
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
            @Example(".category('dummy_category').input(item('minecraft:stone') * 32).output(item('minecraft:diamond') * 50, 0.5f).output(item('minecraft:clay') * 2, 0.1f).tool(item('minecraft:wooden_pickaxe'), 1.0f).tool(item('minecraft:diamond_pickaxe'), 10.0f, 10.0f).durability(10).time(200).xp(1).sound('minecraft:block.anvil.land', 1.0f, 1.0f)"),
            @Example(".category('dead').input(item('minecraft:wheat_seeds') * 3).output(item('minecraft:sand') * 2).tool(item('minecraft:wooden_pickaxe'), 1.0f, 1.1f).tool(item('minecraft:golden_pickaxe'), 0.5f, 1.5f).catalyst(item('minecraft:apple') * 2, 0.9f).time(40).durability(1).queueType(false).outputType('INVENTORY').xp(1)"),
            @Example(".category('dead').input(item('minecraft:stick') * 3).output(item('minecraft:sand') * 2).tool(item('minecraft:wooden_pickaxe'), 1.0f, 1.1f).tool(item('minecraft:golden_pickaxe'), 0.5f, 1.5f).catalyst(item('minecraft:apple') * 2, 0.9f).frame('selectionguicrafting:textures/gui/frame/iron.png').time(40).durability(1).queueType(false)")
    })
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilder();
    }

    @Property(property = "category", comp = @Comp(not = "null", unique = "groovyscript.wiki.selectionguicrafting.recipe.unique_category"))
    @Property(property = "input", comp = @Comp(gte = 1))
    @Property(property = "output", comp = @Comp(gte = 1))
    @Property(property = "tool", comp = @Comp(gte = 1))
    @Property(property = "catalyst", comp = @Comp(gte = 0, lte = 1))
    @Property(property = "time", comp = @Comp(gte = 0))
    @Property(property = "xp", comp = @Comp(gte = 0))
    @Property(property = "durability", comp = @Comp(gte = 0))
    @Property(property = "sounds", defaultValue = "null")
    @Property(property = "particles", defaultValue = "null")
    @Property(property = "frame", defaultValue = "selectionguicrafting:textures/gui/frame/default.png")
    @Property(property = "progressBar", defaultValue = "selectionguicrafting:textures/gui/progress/default.png")
    @Property(property = "outputType", defaultValue = "null")
    @Property(property = "queueable", defaultValue = "null")
    @Property(property = "soundType", defaultValue = "null")
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

        @RecipeBuilderMethodDescription(field = "frame")
        public RecipeBuilder frame(ResourceLocation frame) {
            super.setFrame(frame);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "progressBar")
        public RecipeBuilder progressBar(String progressBar) {
            super.setProgressBar(new ResourceLocation(progressBar));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "progressBar")
        public RecipeBuilder progressBar(ResourceLocation progressBar) {
            super.setProgressBar(progressBar);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "outputType")
        public RecipeBuilder outputType(String outputType) {
            super.setOutputType(GsEnum.OutputType.valueOf(outputType));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "outputType")
        public RecipeBuilder outputType(GsEnum.OutputType outputType) {
            super.setOutputType(outputType);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public RecipeBuilder queueable(String queueable) {
            super.setQueueable(GsEnum.QueueType.valueOf(queueable));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public RecipeBuilder queueType(boolean queueable) {
            super.setQueueable(queueable ? GsEnum.QueueType.YES : GsEnum.QueueType.NO);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public RecipeBuilder queueable(GsEnum.QueueType queueable) {
            super.setQueueable(queueable);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "soundType")
        public RecipeBuilder soundType(String soundType) {
            super.setSoundType(GsEnum.SoundType.valueOf(soundType));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "soundType")
        public RecipeBuilder soundType(GsEnum.SoundType soundType) {
            super.setSoundType(soundType);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "sounds")
        public RecipeBuilder sound(String sound, float volume, float pitch) {
            super.addSound(new ResourceLocation(sound), volume, pitch);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "sounds")
        public RecipeBuilder sound(ResourceLocation sound, float volume, float pitch) {
            super.addSound(sound, volume, pitch);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "sounds")
        public RecipeBuilder sound(SoundEvent sound, float volume, float pitch) {
            super.addSound(sound.getSoundName(), volume, pitch);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "sounds")
        public RecipeBuilder sound(GsSound sound) {
            super.addSound(sound.getSound(), sound.getVolume(), sound.getPitch());
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particles")
        public RecipeBuilder particle(String particle, int count, float speed) {
            super.addParticle(EnumParticleTypes.valueOf(particle), count, speed);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particles")
        public RecipeBuilder particle(EnumParticleTypes particle, int count, float speed) {
            super.addParticle(particle, count, speed);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particles")
        public RecipeBuilder particle(GsParticle particle) {
            super.addParticle(particle.getType(), particle.getCount(), particle.getSpeed());
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(IIngredient input) {
            super.addInput(input.toMcIngredient());
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(IIngredient... input) {
            ArrayList<Ingredient> ingredients = Arrays.stream(input).map(IIngredient::toMcIngredient).collect(Collectors.toCollection(ArrayList::new));
            super.addInput(ingredients);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(Collection<IIngredient> inputs) {
            ArrayList<Ingredient> ingredients = inputs.stream().map(IIngredient::toMcIngredient).collect(Collectors.toCollection(ArrayList::new));
            super.addInput(ingredients);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(ItemStack output, float chance) {
            super.addOutput(output, chance);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(ItemStack output) {
            super.addOutput(output, 1);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(ItemStack... output) {
            super.addOutput(Arrays.stream(output).map(stack -> new GsOutput(stack, 1)).collect(Collectors.toCollection(ArrayList::new)));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(GsOutput output) {
            super.addOutput(output.getItemStack(), output.getChance());
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(GsOutput... output) {
            super.addOutput(Arrays.stream(output).collect(Collectors.toCollection(ArrayList::new)));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public <output> RecipeBuilder output(Collection<output> output) {
            ArrayList<GsOutput> outputs = output.stream().map(o -> {
                if (o instanceof GsOutput) {
                    return (GsOutput) o;
                } else if (o instanceof ItemStack) {
                    return new GsOutput((ItemStack) o, 1);
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
            super.addOutput(outputs);
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

        @RecipeBuilderMethodDescription(field = "catalyst")
        public RecipeBuilder catalyst(IIngredient catalyst, float chance) {
            super.setCatalyst(catalyst.toMcIngredient(), chance);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "catalyst")
        public RecipeBuilder catalyst(IIngredient catalyst) {
            super.setCatalyst(catalyst.toMcIngredient(), 1);
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
            GroovyLog.Msg msg = GroovyLog.msg("Error adding SelectionGUI Crafting recipe").error();
            msg.add(super.getCategory() == null, "Category can not be null");
            msg.add(super.getInput().isEmpty(), "Input can not be empty");
            msg.add(super.getOutput().isEmpty(), "Output can not be empty");
            msg.add(super.getTool().isEmpty(), "Tool can not be empty");
            msg.add(GsRegistry.getCategories().stream().noneMatch(category -> Objects.equals(category.getId(), super.getCategory())), "Category not found. Has the category been registered?");
            return !msg.postIfNotEmpty();
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
