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
import io.enderdev.selectionguicrafting.registry.category.OutputType;
import io.enderdev.selectionguicrafting.registry.category.QueueType;
import io.enderdev.selectionguicrafting.registry.category.SoundType;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeInput;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeOutput;
import io.enderdev.selectionguicrafting.registry.util.Particle;
import io.enderdev.selectionguicrafting.registry.util.Sound;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class Recipe extends VirtualizedRegistry<io.enderdev.selectionguicrafting.registry.recipe.Recipe> {
    @GroovyBlacklist
    @Override
    public void onReload() {
        removeScripted().forEach(Register::removeRecipe);
        restoreFromBackup().forEach(Register::addRecipe);
    }

    @MethodDescription(type = MethodDescription.Type.ADDITION)
    public void add(io.enderdev.selectionguicrafting.registry.recipe.Recipe recipe) {
        if (recipe != null) {
            addScripted(recipe);
            Register.addRecipe(recipe);
        }
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL)
    public boolean remove(io.enderdev.selectionguicrafting.registry.recipe.Recipe recipe) {
        if (Register.removeRecipe(recipe)) {
            addBackup(recipe);
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example(value = "'dummy_category'", commented = true), description = "groovyscript.wiki.selectionguicrafting.recipe.remove_by_category")
    public boolean removeByCategory(String category) {
        return Register.getRecipes().removeIf(recipe -> {
            if (recipe.getCategory().equals(category)) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:stone')"))
    public boolean removeByOutput(IIngredient output) {
        return Register.getRecipes().removeIf(recipe -> {
            if (recipe.getOutputs().stream().map(RecipeOutput::getItemStack).anyMatch(output)) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:cobblestone')"))
    public boolean removeByInput(IIngredient input) {
        return Register.getRecipes().removeIf(recipe -> {
            if (recipe.getInputs().stream().map(RecipeInput::getIngredient).anyMatch(ingredient -> ingredient.equals(input.toMcIngredient()))) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<io.enderdev.selectionguicrafting.registry.recipe.Recipe> streamRecipes() {
        return new SimpleObjectStream<>(Register.getRecipes()).setRemover(this::remove);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
    public void removeAll() {
        Register.getRecipes().forEach(this::addBackup);
        Register.getRecipes().clear();
    }

    @RecipeBuilderDescription(example = {
            @Example(".category('dummy_category').input(item('minecraft:stone') * 3).output(item('minecraft:cobblestone') * 2, 0.5f).time(200).xp(1).sound('minecraft:block.anvil.land', 1.0f, 1.0f)"),
            @Example(".category('blub').input(item('minecraft:diamond')).output(item('minecraft:wheat_seeds') * 5, 0.5f)"),
            @Example(".category('dummy_category').input(item('minecraft:stone') * 32).output(item('minecraft:diamond') * 50, 0.5f).output(item('minecraft:clay') * 2, 0.1f).time(200).xp(1).sound('minecraft:block.anvil.land', 1.0f, 1.0f)"),
            @Example(".category('dead').input(item('minecraft:wheat_seeds') * 3).output(item('minecraft:sand') * 2).time(40).queueable(false).outputType('DROP').xp(1)"),
            @Example(".category('dead').input(item('minecraft:stick') * 3).output(item('minecraft:sand') * 2).frame('selectionguicrafting:textures/gui/frame/iron.png').time(40).queueable(false).command('kill @p')")
    })
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilder();
    }

    @Property(property = "category", comp = @Comp(not = "null", unique = "groovyscript.wiki.selectionguicrafting.recipe.unique_category"))
    @Property(property = "input", comp = @Comp(gte = 0))
    @Property(property = "output", comp = @Comp(gte = 1))
    @Property(property = "mainhand", comp = @Comp(gte = 0, lte = 1))
    @Property(property = "offhand", comp = @Comp(gte = 0, lte = 1))
    @Property(property = "time", comp = @Comp(gte = 0), defaultValue = "20")
    @Property(property = "xp", comp = @Comp(gte = 0), defaultValue = "0")
    @Property(property = "sound", defaultValue = "null")
    @Property(property = "particles", defaultValue = "null")
    @Property(property = "frame", defaultValue = "selectionguicrafting:textures/gui/frame/default.png")
    @Property(property = "progressBar", defaultValue = "selectionguicrafting:textures/gui/progress/default.png")
    @Property(property = "outputType", defaultValue = "null")
    @Property(property = "queueable", defaultValue = "null")
    @Property(property = "soundType", defaultValue = "null")
    @Property(property = "command", defaultValue = "null")
    public static class RecipeBuilder extends io.enderdev.selectionguicrafting.registry.recipe.Recipe implements IRecipeBuilder<io.enderdev.selectionguicrafting.registry.recipe.Recipe> {
        @Property
        private RecipeInput mainhand;
        @Property
        private OutputType outputType;
        @Property
        private RecipeInput offhand;
        @Property
        private RecipeOutput output;
        @Property
        private RecipeInput input;
        @Property
        private Sound sound;
        @Property
        private ResourceLocation progressBar;
        @Property
        private QueueType queueable;
        @Property
        private Particle particle;
        @Property
        private SoundType soundType;
        @Property
        private ResourceLocation frame;
        @Property
        private String command;

        // Register
        @Override
        public boolean validate() {
            GroovyLog.Msg msg = GroovyLog.msg(String.format("Error adding %s recipe!", Tags.MOD_NAME)).error();
            getErrorCheck().listMsg().forEach(msg::add);
            return !msg.postIfNotEmpty();
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable io.enderdev.selectionguicrafting.registry.recipe.Recipe register() {
            if (!validate()) {
                return null;
            }
            GSPlugin.instance.recipe.add(this);
            return this;
        }

        // Init
        @RecipeBuilderMethodDescription(field = "category")
        public RecipeBuilder category(String category) {
            super.category(category);
            return this;
        }

        // Frame
        @RecipeBuilderMethodDescription(field = "frame")
        public RecipeBuilder frame(ResourceLocation frame) {
            super.setFrame(frame);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "frame")
        public RecipeBuilder frame(String frame) {
            return frame(new ResourceLocation(frame));
        }

        // Progress Bar
        @RecipeBuilderMethodDescription(field = "progressBar")
        public RecipeBuilder progressBar(ResourceLocation progressBar) {
            super.setProgressBar(progressBar);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "progressBar")
        public RecipeBuilder progressBar(String progressBar) {
            return progressBar(new ResourceLocation(progressBar));
        }

        // Output Type
        @RecipeBuilderMethodDescription(field = "outputType")
        public RecipeBuilder outputType(OutputType outputType) {
            super.setOutputType(outputType);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "outputType")
        public RecipeBuilder outputType(String outputType) {
            return outputType(OutputType.valueOf(outputType));
        }

        // Sound Type
        @RecipeBuilderMethodDescription(field = "soundType")
        public RecipeBuilder soundType(SoundType soundType) {
            super.setSoundType(soundType);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "soundType")
        public RecipeBuilder soundType(String soundType) {
            return soundType(SoundType.valueOf(soundType));
        }

        // Queue Type
        @RecipeBuilderMethodDescription(field = "queueable")
        public RecipeBuilder queueable(QueueType queueable) {
            super.setQueueable(queueable);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public RecipeBuilder queueable(String queueable) {
            return queueable(QueueType.valueOf(queueable));
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public RecipeBuilder queueable(boolean queueable) {
            return queueable(queueable ? QueueType.YES : QueueType.NO);
        }

        // Effects
        // Sounds
        @RecipeBuilderMethodDescription(field = "sound")
        public RecipeBuilder sound(Sound sound) {
            super.addSound(sound);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public RecipeBuilder sound(ResourceLocation sound, float volume, float pitch) {
            return sound(new Sound(sound, volume, pitch));
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public RecipeBuilder sound(ResourceLocation sound) {
            return sound(new Sound(sound));
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public RecipeBuilder sound(String sound, float volume, float pitch) {
            return sound(new ResourceLocation(sound), volume, pitch);
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public RecipeBuilder sound(String sound) {
            return sound(new ResourceLocation(sound));
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public RecipeBuilder sound(SoundEvent sound, float volume, float pitch) {
            return sound(sound.getSoundName(), volume, pitch);
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public RecipeBuilder sound(SoundEvent sound) {
            return sound(sound.getSoundName());
        }

        // Particles
        @RecipeBuilderMethodDescription(field = "particle")
        public RecipeBuilder particle(Particle particle) {
            super.addParticle(particle);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particle")
        public RecipeBuilder particle(EnumParticleTypes particle, int count, float speed) {
            return particle(new Particle(particle, count, speed));
        }

        @RecipeBuilderMethodDescription(field = "particle")
        public RecipeBuilder particle(EnumParticleTypes particle) {
            return particle(new Particle(particle));
        }

        @RecipeBuilderMethodDescription(field = "particle")
        public RecipeBuilder particle(String particle, int count, float speed) {
            return particle(EnumParticleTypes.valueOf(particle), count, speed);
        }

        @RecipeBuilderMethodDescription(field = "particle")
        public RecipeBuilder particle(String particle) {
            return particle(EnumParticleTypes.valueOf(particle));
        }

        // Input
        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(IIngredient input, double chance, int damage) {
            super.input(input.toMcIngredient(), chance, damage);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(IIngredient input, int damage) {
            super.input(input.toMcIngredient(), damage);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(IIngredient input, double chance) {
            super.input(input.toMcIngredient(), chance);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(IIngredient input) {
            super.input(input.toMcIngredient());
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(IIngredient... input) {
            super.input(Arrays.stream(input).map(IIngredient::toMcIngredient).collect(Collectors.toList()));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(RecipeInput input) {
            super.input(input);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(RecipeInput... input) {
            super.input(input);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "input")
        public RecipeBuilder input(Collection<?> inputs) {
            inputs.forEach(entry -> {
                if (entry instanceof RecipeInput) {
                    input((RecipeInput) entry);
                } else if (entry instanceof IIngredient) {
                    input((IIngredient) entry);
                }
            });
            return this;
        }

        // Output
        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(ItemStack output, double chance) {
            super.output(output, chance);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(ItemStack output) {
            super.output(output);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(ItemStack... output) {
            super.output(output);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(RecipeOutput output) {
            super.output(output);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(RecipeOutput... output) {
            super.output(output);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "output")
        public RecipeBuilder output(Collection<?> outputs) {
            outputs.forEach(entry -> {
                if (entry instanceof RecipeOutput) {
                    output((RecipeOutput) entry);
                } else if (entry instanceof ItemStack) {
                    output((ItemStack) entry);
                }
            });
            return this;
        }

        // Mainhand
        @RecipeBuilderMethodDescription(field = "mainhand")
        public RecipeBuilder mainhand(RecipeInput input) {
            super.mainHand(input);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "mainhand")
        public RecipeBuilder mainhand(IIngredient input) {
            super.mainHand(input.toMcIngredient());
            return this;
        }

        @RecipeBuilderMethodDescription(field = "mainhand")
        public RecipeBuilder mainhand(IIngredient input, int damage) {
            super.mainHand(input.toMcIngredient(), damage);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "mainhand")
        public RecipeBuilder mainhand(IIngredient input, double chance) {
            super.mainHand(input.toMcIngredient(), chance);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "mainhand")
        public RecipeBuilder mainhand(IIngredient input, double chance, int damage) {
            super.mainHand(input.toMcIngredient(), chance, damage);
            return this;
        }

        // Offhand
        @RecipeBuilderMethodDescription(field = "offhand")
        public RecipeBuilder offhand(RecipeInput input) {
            super.offHand(input);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "offhand")
        public RecipeBuilder offhand(IIngredient input) {
            super.offHand(input.toMcIngredient());
            return this;
        }

        @RecipeBuilderMethodDescription(field = "offhand")
        public RecipeBuilder offhand(IIngredient input, int damage) {
            super.offHand(input.toMcIngredient(), damage);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "offhand")
        public RecipeBuilder offhand(IIngredient input, double chance) {
            super.offHand(input.toMcIngredient(), chance);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "offhand")
        public RecipeBuilder offhand(IIngredient input, double chance, int damage) {
            super.offHand(input.toMcIngredient(), chance, damage);
            return this;
        }

        // XP
        @RecipeBuilderMethodDescription(field = "xp")
        public RecipeBuilder xp(int xp) {
            super.xp(xp);
            return this;
        }

        // Time
        @RecipeBuilderMethodDescription(field = "time")
        public RecipeBuilder time(int ticks) {
            super.time(ticks);
            return this;
        }

        // Command
        @RecipeBuilderMethodDescription(field = "command")
        public RecipeBuilder command(String command) {
            super.command(command);
            return this;
        }
    }
}
