package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.recipe.GSCategory;
import io.enderdev.selectionguicrafting.recipe.GSEnum;
import io.enderdev.selectionguicrafting.recipe.GSRecipeRegistry;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class SgcCategory extends VirtualizedRegistry<GSCategory> {
    @Override
    @GroovyBlacklist
    public void onReload() {
        GSRecipeRegistry.getCategories().removeAll(removeScripted());
        GSRecipeRegistry.getCategories().addAll(restoreFromBackup());
    }

    public void add(GSCategory category) {
        if (category != null) {
            addScripted(category);
            GSRecipeRegistry.registerCategory(category);
        }
    }

    public boolean remove(GSCategory category) {
        if (GSRecipeRegistry.removeCategory(category.getId())) {
            addBackup(category);
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example(value = "'dummy_category'",commented = true), description = "sgc.groovyscript.category.remove_by_name")
    public boolean removeByName(String name) {
        if (GSRecipeRegistry.removeCategory(name)) {
            addBackup(GSRecipeRegistry.getCategory(name));
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<GSCategory> streamCategories() {
        return new SimpleObjectStream<>(GSRecipeRegistry.getCategories()).setRemover(this::remove);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
    public void removeAll() {
        GSRecipeRegistry.getCategories().forEach(this::addBackup);
        GSRecipeRegistry.getCategories().clear();
    }

    @RecipeBuilderDescription(example = {
            @Example(".id('dummy_category').displayName('Dummy Category')")
    })
    public CategoryBuilder createCategory() {
        return new CategoryBuilder();
    }

    public static class CategoryBuilder extends AbstractRecipeBuilder<GSCategory> {

        @Property
        private final GSCategory category = new GSCategory();

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
            category.setOutputType(GSEnum.OutputType.valueOf(outputType));
            return this;
        }

        @RecipeBuilderMethodDescription(field = "category")
        public CategoryBuilder setQueueable(String queueable) {
            category.setQueueable(GSEnum.QueueType.valueOf(queueable));
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
        public @Nullable GSCategory register() {
            if (!validate()) {
                return null;
            }
            GSPlugin.instance.category.add(category);
            return category;
        }
    }
}
