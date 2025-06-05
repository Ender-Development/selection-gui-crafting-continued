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
import io.enderdev.selectionguicrafting.registry.category.*;
import io.enderdev.selectionguicrafting.registry.util.Particle;
import io.enderdev.selectionguicrafting.registry.util.Sound;
import net.minecraft.block.Block;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.Nullable;

@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class Category extends VirtualizedRegistry<io.enderdev.selectionguicrafting.registry.category.Category> {
    @GroovyBlacklist
    @Override
    public void onReload() {
        removeScripted().forEach(Register::removeCategory);
        restoreFromBackup().forEach(Register::addCategory);
    }

    @MethodDescription(type = MethodDescription.Type.ADDITION)
    public void add(io.enderdev.selectionguicrafting.registry.category.Category category) {
        if (category != null) {
            addScripted(category);
            Register.addCategory(category);
        }
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL)
    public boolean remove(io.enderdev.selectionguicrafting.registry.category.Category category) {
        if (Register.removeCategory(category)) {
            addBackup(category);
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example(value = "'dummy_category_1'"), description = "groovyscript.wiki.selectionguicrafting.category.remove_by_name")
    public boolean removeByName(String name) {
        return remove(Register.getCategoryByID(name));
    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<io.enderdev.selectionguicrafting.registry.category.Category> streamCategories() {
        return new SimpleObjectStream<>(Register.getCategories()).setRemover(this::remove);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
    public void removeAll() {
        Register.getCategories().forEach(this::addBackup);
        Register.getCategories().clear();
    }

    @RecipeBuilderDescription(example = {
            @Example(".id('dummy_category').trigger(item('minecraft:diamond'), 0.2, 0.8, 2.65).background('selectionguicrafting:textures/gui/background/wood.png')"),
            @Example(".id('blub').trigger(item('minecraft:stone_shovel')).background('selectionguicrafting:textures/gui/background/lake.png').backgroundType('SINGLE_CUT')"),
            @Example(".id('dead').trigger(block('minecraft:snow')).background('selectionguicrafting:textures/gui/background/deadlands.png').decoration('selectionguicrafting:textures/gui/decor/gold.png').border('selectionguicrafting:textures/gui/background/wood.png').backgroundType('SINGLE_CUT')")
    })
    public CategoryBuilder categoryBuilder() {
        return new CategoryBuilder();
    }

    @Property(property = "id", comp = @Comp(not = "null", unique = "groovyscript.wiki.selectionguicrafting.category.unique_id"))
    @Property(property = "trigger", comp = @Comp(not = "null", unique = "groovyscript.wiki.selectionguicrafting.category.trigger"))
    @Property(property = "background", defaultValue = "selectionguicrafting:textures/gui/background/default.png")
    @Property(property = "border", defaultValue = "selectionguicrafting:textures/gui/background/default.png")
    @Property(property = "decoration", defaultValue = "selectionguicrafting:textures/gui/decor/default.png")
    @Property(property = "frame", defaultValue = "selectionguicrafting:textures/gui/frame/default.png")
    @Property(property = "progressBar", defaultValue = "selectionguicrafting:textures/gui/progress/default.png")
    @Property(property = "backgroundType", defaultValue = "TILE")
    @Property(property = "outputType", defaultValue = "INVENTORY")
    @Property(property = "queueable", defaultValue = "YES")
    @Property(property = "soundType", defaultValue = "RANDOM")
    @Property(property = "sound", defaultValue = "null")
    @Property(property = "particle", defaultValue = "null")
    public static class CategoryBuilder extends io.enderdev.selectionguicrafting.registry.category.Category implements IRecipeBuilder<io.enderdev.selectionguicrafting.registry.category.Category> {
        @Property
        private ResourceLocation border;
        @Property
        private Sound sound;
        @Property
        private OutputType outputType;
        @Property
        private AbstractTrigger trigger;
        @Property
        private ResourceLocation background;
        @Property
        private ResourceLocation progressBar;
        @Property
        private Particle particle;
        @Property
        private QueueType queueable;
        @Property
        private ResourceLocation decoration;
        @Property
        private BackgroundType backgroundType;
        @Property
        private SoundType soundType;
        @Property
        private ResourceLocation frame;

        // Register
        @Override
        public boolean validate() {
            GroovyLog.Msg msg = GroovyLog.msg(String.format("Error adding %s category!", Tags.MOD_NAME)).error();
            getErrorCheck().listMsg().forEach(msg::add);
            return !msg.postIfNotEmpty();
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable io.enderdev.selectionguicrafting.registry.category.Category register() {
            if (!validate()) {
                return null;
            }
            GSPlugin.instance.category.add(this);
            return this;
        }

        // Init
        @RecipeBuilderMethodDescription(field = "id")
        public CategoryBuilder id(String id) {
            super.id(id);
            return this;
        }

        // Trigger
        @RecipeBuilderMethodDescription(field = "trigger")
        public CategoryBuilder trigger(ItemTrigger itemTrigger) {
            super.trigger(itemTrigger);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "trigger")
        public CategoryBuilder trigger(IIngredient ingredient, double damageMultiplier, double timeMultiplier, double xpMultiplier) {
            super.trigger(ingredient.toMcIngredient(), damageMultiplier, timeMultiplier, xpMultiplier);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "trigger")
        public CategoryBuilder trigger(IIngredient ingredient) {
            super.trigger(ingredient.toMcIngredient());
            return this;
        }

        @RecipeBuilderMethodDescription(field = "trigger")
        public CategoryBuilder trigger(BlockTrigger blockTrigger) {
            super.trigger(blockTrigger);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "trigger")
        public CategoryBuilder trigger(Block block, double damageMultiplier, double timeMultiplier, double xpMultiplier) {
            super.trigger(block, damageMultiplier, timeMultiplier, xpMultiplier);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "trigger")
        public CategoryBuilder trigger(Block block) {
            super.trigger(block);
            return this;
        }

        // Textures
        // Background
        @RecipeBuilderMethodDescription(field = "background")
        public CategoryBuilder background(ResourceLocation background) {
            super.setBackground(background);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "background")
        public CategoryBuilder background(String background) {
            return background(new ResourceLocation(background));
        }

        // Border
        @RecipeBuilderMethodDescription(field = "border")
        public CategoryBuilder border(ResourceLocation border) {
            super.setBorder(border);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "border")
        public CategoryBuilder border(String border) {
            return border(new ResourceLocation(border));
        }

        // Frame
        @RecipeBuilderMethodDescription(field = "frame")
        public CategoryBuilder frame(ResourceLocation frame) {
            super.setFrame(frame);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "frame")
        public CategoryBuilder frame(String frame) {
            return frame(new ResourceLocation(frame));
        }

        // Progress Bar
        @RecipeBuilderMethodDescription(field = "progressBar")
        public CategoryBuilder bar(ResourceLocation progressBar) {
            super.setProgressBar(progressBar);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "progressBar")
        public CategoryBuilder bar(String progressBar) {
            return bar(new ResourceLocation(progressBar));
        }

        // Decoration
        @RecipeBuilderMethodDescription(field = "decoration")
        public CategoryBuilder decoration(ResourceLocation decoration) {
            super.setDecoration(decoration);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "decoration")
        public CategoryBuilder decoration(String decoration) {
            return decoration(new ResourceLocation(decoration));
        }

        // Types
        // Output
        @RecipeBuilderMethodDescription(field = "outputType")
        public CategoryBuilder outputType(OutputType outputType) {
            super.setOutputType(outputType);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "outputType")
        public CategoryBuilder outputType(String outputType) {
            return outputType(OutputType.valueOf(outputType));
        }

        // Sound
        @RecipeBuilderMethodDescription(field = "soundType")
        public CategoryBuilder soundType(SoundType soundType) {
            super.setSoundType(soundType);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "soundType")
        public CategoryBuilder soundType(String soundType) {
            return soundType(SoundType.valueOf(soundType));
        }

        // Background
        @RecipeBuilderMethodDescription(field = "backgroundType")
        public CategoryBuilder backgroundType(BackgroundType backgroundType) {
            super.setBackgroundType(backgroundType);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "backgroundType")
        public CategoryBuilder backgroundType(String backgroundType) {
            return backgroundType(BackgroundType.valueOf(backgroundType));
        }

        // Queue
        @RecipeBuilderMethodDescription(field = "queueable")
        public CategoryBuilder queueType(QueueType queueable) {
            super.setQueueable(queueable);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public CategoryBuilder queueType(String queueable) {
            return queueType(QueueType.valueOf(queueable));
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public CategoryBuilder queueType(boolean queueable) {
            return queueType(queueable ? QueueType.YES : QueueType.NO);
        }

        // Effects
        // Sounds
        @RecipeBuilderMethodDescription(field = "sound")
        public CategoryBuilder sound(Sound sound) {
            super.addSound(sound);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public CategoryBuilder sound(ResourceLocation sound, float volume, float pitch) {
            return sound(new Sound(sound, volume, pitch));
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public CategoryBuilder sound(ResourceLocation sound) {
            return sound(new Sound(sound));
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public CategoryBuilder sound(String sound, float volume, float pitch) {
            return sound(new ResourceLocation(sound), volume, pitch);
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public CategoryBuilder sound(String sound) {
            return sound(new ResourceLocation(sound));
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public CategoryBuilder sound(SoundEvent sound, float volume, float pitch) {
            return sound(sound.getSoundName(), volume, pitch);
        }

        @RecipeBuilderMethodDescription(field = "sound")
        public CategoryBuilder sound(SoundEvent sound) {
            return sound(sound.getSoundName());
        }

        // Particles
        @RecipeBuilderMethodDescription(field = "particle")
        public CategoryBuilder particle(Particle particle) {
            super.addParticle(particle);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particle")
        public CategoryBuilder particle(EnumParticleTypes particle, int count, float speed) {
            return  particle(new Particle(particle, count, speed));
        }

        @RecipeBuilderMethodDescription(field = "particle")
        public CategoryBuilder particle(EnumParticleTypes particle) {
            return  particle(new Particle(particle));
        }

        @RecipeBuilderMethodDescription(field = "particle")
        public CategoryBuilder particle(String particle, int count, float speed) {
            return particle(EnumParticleTypes.valueOf(particle), count, speed);
        }

        @RecipeBuilderMethodDescription(field = "particle")
        public CategoryBuilder particle(String particle) {
            return particle(EnumParticleTypes.valueOf(particle));
        }
    }
}
