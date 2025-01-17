package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.proxy.CommonProxy;
import io.enderdev.selectionguicrafting.recipe.GuiSelectionRecipeCategory;

import java.util.ArrayList;

@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class SelectionCategory extends VirtualizedRegistry<GuiSelectionRecipeCategory> {

    @Override
    @GroovyBlacklist
    public void onReload() {
        CommonProxy.recipeCategories.values().removeAll(removeScripted());
        CommonProxy.recipeCategories.values().addAll(restoreFromBackup());
    }

    public void add(String name, GuiSelectionRecipeCategory category) {
        if (category != null) {
            addScripted(category);
            CommonProxy.recipeCategories.put(name, category);
        }
    }

    public boolean remove(GuiSelectionRecipeCategory category) {
        if (CommonProxy.recipeCategories.values().removeIf(category::equals)) {
            addBackup(category);
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, description = "groovyscript.wiki.selectionguicrafting.removebyname", example = @Example("'test_category'"))
    public boolean removeByName(String name) {
        return CommonProxy.recipeCategories.keySet().removeIf(category_name -> {
            if (category_name.equals(name)) {
                addBackup(CommonProxy.recipeCategories.get(category_name));
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<GuiSelectionRecipeCategory> getCategories() {
        return new SimpleObjectStream<>(CommonProxy.recipeCategories.values()).setRemover(this::remove);
    }

    @RecipeBuilderDescription(example = {
            @Example(".name('test_category').displayName('Test Category')"),
            @Example(".name('another_category').displayName('Another Category')")
    })
    public static CategoryBuilder categoryBuilder() {
        return new CategoryBuilder();
    }

    public static class CategoryBuilder extends AbstractRecipeBuilder<GuiSelectionRecipeCategory> {
        @Property(comp = @Comp(not = "null"))
        private String name;

        @Property(comp = @Comp(not = "null"))
        private String displayName;

        @RecipeBuilderMethodDescription(field = "name")
        public CategoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "displayName")
        public CategoryBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        @Override
        public String getErrorMsg() {
            return "Error creating a new Selection GUI Crafting recipe category";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateFluids(msg);
            validateItems(msg);
            msg.add(name == null, "Name cannot be null");
            msg.add(displayName == null, "Display name cannot be null");
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable GuiSelectionRecipeCategory register() {
            if (!validate()) return null;
            GuiSelectionRecipeCategory category = new GuiSelectionRecipeCategory(displayName, new ArrayList<>());
            GSPlugin.instance.selectionCategory.add(name, category);
            return category;
        }
    }
}
