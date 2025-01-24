package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.IRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.registry.GsCategory;
import io.enderdev.selectionguicrafting.registry.GsEnum;
import io.enderdev.selectionguicrafting.registry.GsRegistry;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("unused")
@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class Category extends VirtualizedRegistry<GsCategory> {
    @Override
    @GroovyBlacklist
    public void onReload() {
        GsRegistry.getCategories().removeAll(removeScripted());
        GsRegistry.getCategories().addAll(restoreFromBackup());
    }

    public void add(GsCategory category) {
        if (category != null) {
            addScripted(category);
            GsRegistry.registerCategory(category);
        }
    }

    public boolean remove(GsCategory category) {
        if (GsRegistry.removeCategory(category.getId())) {
            addBackup(category);
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example(value = "'dummy_category_1'"), description = "sgc.groovyscript.category.remove_by_name")
    public boolean removeByName(String name) {
        if (GsRegistry.removeCategory(name)) {
            addBackup(GsRegistry.getCategory(name));
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<GsCategory> streamCategories() {
        return new SimpleObjectStream<>(GsRegistry.getCategories()).setRemover(this::remove);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
    public void removeAll() {
        GsRegistry.getCategories().forEach(this::addBackup);
        GsRegistry.getCategories().clear();
    }

    @RecipeBuilderDescription(example = {
            @Example(".id('dummy_category').displayName('Your first Category').background('selectionguicrafting:textures/gui/background/wood.png')"),
            @Example(".id('blub').displayName('Pick your recipe').background('selectionguicrafting:textures/gui/background/lake.png').backgroundType('SINGLE_CUT')"),
            @Example(".id('dead').displayName('This is another dummy category to test').background('selectionguicrafting:textures/gui/background/deadlands.png').decoration('selectionguicrafting:textures/gui/decor/gold.png').border('selectionguicrafting:textures/gui/background/wood.png').backgroundType('SINGLE_CUT')")
    })
    public CategoryBuilder categoryBuilder() {
        return new CategoryBuilder();
    }

    @Property(property = "id", comp = @Comp(not = "null"))
    @Property(property = "displayName", comp = @Comp(not = "null"))
    @Property(property = "background", defaultValue = "selectionguicrafting:textures/gui/background/default.png")
    @Property(property = "border", defaultValue = "selectionguicrafting:textures/gui/background/default.png")
    @Property(property = "decoration", defaultValue = "selectionguicrafting:textures/gui/decor/default.png")
    @Property(property = "frame", defaultValue = "selectionguicrafting:textures/gui/frame/default.png")
    @Property(property = "progressBar", defaultValue = "selectionguicrafting:textures/gui/progress/default.png")
    @Property(property = "backgroundType", defaultValue = "'TILE'")
    @Property(property = "outputType", defaultValue = "'DROP'")
    @Property(property = "queueable", defaultValue = "'YES'")
    @Property(property = "soundType", defaultValue = "'RANDOM'")
    @Property(property = "sounds", defaultValue = "null")
    @Property(property = "particles", defaultValue = "null")
    public static class CategoryBuilder extends GsCategory implements IRecipeBuilder<GsCategory> {
        // Init
        @RecipeBuilderMethodDescription(field = "id")
        public CategoryBuilder id(String id) {
            super.setId(id);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "displayName")
        public CategoryBuilder displayName(String displayName) {
            super.setDisplayName(displayName);
            return this;
        }

        // Textures
        @RecipeBuilderMethodDescription(field = "background")
        public CategoryBuilder background(String background) {
            super.setBackground(new ResourceLocation(background));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "background")
        public CategoryBuilder background(ResourceLocation background) {
            super.setBackground(background);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "border")
        public CategoryBuilder border(String border) {
            super.setBorder(new ResourceLocation(border));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "border")
        public CategoryBuilder border(ResourceLocation border) {
            super.setBorder(border);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "frame")
        public CategoryBuilder frame(String frame) {
            super.setFrame(new ResourceLocation(frame));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "frame")
        public CategoryBuilder frame(ResourceLocation frame) {
            super.setFrame(frame);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "progressBar")
        public CategoryBuilder bar(String progressBar) {
            super.setProgressBar(new ResourceLocation(progressBar));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "progressBar")
        public CategoryBuilder bar(ResourceLocation progressBar) {
            super.setProgressBar(progressBar);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "decoration")
        public CategoryBuilder decoration(String decoration) {
            super.setDecoration(new ResourceLocation(decoration));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "decoration")
        public CategoryBuilder decoration(ResourceLocation decoration) {
            super.setDecoration(decoration);
            return this;
        }

        // Types
        @RecipeBuilderMethodDescription(field = "outputType")
        public CategoryBuilder outputType(String outputType) {
            super.setOutputType(GsEnum.OutputType.valueOf(outputType));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "outputType")
        public CategoryBuilder outputType(GsEnum.OutputType outputType) {
            super.setOutputType(outputType);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "soundType")
        public CategoryBuilder soundType(String soundType) {
            super.setSoundType(GsEnum.SoundType.valueOf(soundType));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "soundType")
        public CategoryBuilder soundType(GsEnum.SoundType soundType) {
            super.setSoundType(soundType);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "backgroundType")
        public CategoryBuilder backgroundType(String backgroundType) {
            super.setBackgroundType(GsEnum.BackgroundType.valueOf(backgroundType));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "backgroundType")
        public CategoryBuilder backgroundType(GsEnum.BackgroundType backgroundType) {
            super.setBackgroundType(backgroundType);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public CategoryBuilder queueType(String queueable) {
            super.setQueueable(GsEnum.QueueType.valueOf(queueable));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public CategoryBuilder queueType(GsEnum.QueueType queueable) {
            super.setQueueable(queueable);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "queueable")
        public CategoryBuilder queueType(boolean queueable) {
            super.setQueueable(queueable ? GsEnum.QueueType.YES : GsEnum.QueueType.NO);
            return this;
        }

        // Sounds and Particles
        @RecipeBuilderMethodDescription(field = "sounds")
        public CategoryBuilder sound(String sound, float volume, float pitch) {
            super.addSound(new ResourceLocation(sound), volume, pitch);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "sounds")
        public CategoryBuilder sound(ResourceLocation sound, float volume, float pitch) {
            super.addSound(sound, volume, pitch);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "sounds")
        public CategoryBuilder sound(SoundEvent sound, float volume, float pitch) {
            super.addSound(sound.getSoundName(), volume, pitch);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particles")
        public CategoryBuilder particle(String particle, int count, float speed) {
            super.addParticle(EnumParticleTypes.valueOf(particle), count, speed);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "particles")
        public CategoryBuilder particle(EnumParticleTypes particle, int count, float speed) {
            super.addParticle(particle, count, speed);
            return this;
        }

        @Override
        public boolean validate() {
            GroovyLog.Msg msg = GroovyLog.msg("Error adding SelectionGUI Crafting recipe").error();
            msg.add(super.getId() == null, "ID can not be null");
            msg.add(super.getDisplayName() == null, "Display Name can not be null");
            msg.add(GsRegistry.getCategories().stream().anyMatch(category -> Objects.equals(category.getId(), super.getId())), "ID already exists");
            return !msg.postIfNotEmpty();
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable GsCategory register() {
            if (!validate()) {
                return null;
            }
            GSPlugin.instance.category.add(this);
            return this;
        }
    }
}
