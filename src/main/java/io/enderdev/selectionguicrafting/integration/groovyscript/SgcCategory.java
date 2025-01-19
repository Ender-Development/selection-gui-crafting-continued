package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.registry.GsCategory;
import io.enderdev.selectionguicrafting.registry.GsEnum;
import io.enderdev.selectionguicrafting.registry.GsRegistry;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class SgcCategory extends VirtualizedRegistry<GsCategory> {
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

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example(value = "'dummy_category'",commented = true), description = "sgc.groovyscript.category.remove_by_name")
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
            @Example(".id('dummy_category').displayName('Dummy Category').background('selectionguicrafting:textures/gui/gui_wood.png')")
    })
    public CategoryBuilder createCategory() {
        return new CategoryBuilder();
    }

    public static class CategoryBuilder extends AbstractRecipeBuilder<GsCategory> {

        @Property
        private final GsCategory category = new GsCategory();

        @RecipeBuilderMethodDescription(field = "category")
        public CategoryBuilder id(String id) {
            category.setId(id);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "category")
        public CategoryBuilder displayName(String displayName) {
            category.setDisplayName(displayName);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "category")
        public CategoryBuilder background(String background) {
            category.setBackground(new ResourceLocation(background));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "category")
        public CategoryBuilder addSound(String sound) {
            category.addSound(new ResourceLocation(sound));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "category")
        public CategoryBuilder setOutputType(String outputType) {
            category.setOutputType(GsEnum.OutputType.valueOf(outputType));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "category")
        public CategoryBuilder setQueueable(String queueable) {
            category.setQueueable(GsEnum.QueueType.valueOf(queueable));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "category")
        public CategoryBuilder setBackgroundType(String backgroundType) {
            category.setBackgroundType(GsEnum.BackgroundType.valueOf(backgroundType));
            return this;
        }

        @Override
        public String getErrorMsg() {
            return "Encountered an error while building a category for " + Tags.MOD_ID;
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            msg.add(category.getId() == null, "Category id cannot be null");
            msg.add(category.getDisplayName() == null, "Category display name cannot be null");
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable GsCategory register() {
            if (!validate()) {
                return null;
            }
            GSPlugin.instance.category.add(category);
            return category;
        }
    }
}
