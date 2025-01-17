package io.enderdev.selectionguicrafting.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.proxy.CommonProxy;
import io.enderdev.selectionguicrafting.recipe.GuiSelectionRecipe;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Objects;

@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class SelectionRecipe extends VirtualizedRegistry<GuiSelectionRecipe> {

    @Override
    @GroovyBlacklist
    public void onReload() {
        CommonProxy.getAllRecipes().removeAll(removeScripted());
        CommonProxy.getAllRecipes().addAll(restoreFromBackup());
    }

    public void add(String category, GuiSelectionRecipe recipe) {
        if (recipe != null) {
            addScripted(recipe);
            CommonProxy.recipeCategories.get(category).recipes.add(recipe);
        }
    }

    public boolean remove(GuiSelectionRecipe recipe) {
        if (CommonProxy.getAllRecipes().removeIf(recipe::equals)) {
            addBackup(recipe);
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:stone')"))
    public boolean removeByOutput(IIngredient output) {
        return CommonProxy.getAllRecipes().removeIf(recipe -> {
            if (Arrays.stream(recipe.outputs).anyMatch(output)) {
                addBackup(recipe);
                return true;
            }
            return false;
        });
    }

//    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("'yet_another_test_category'"))
//    public boolean removeByCategory(String category) {
//        return CommonProxy.getAllRecipes().removeIf(recipe -> {
//            if (recipe.parentCategoryName.equals(category)) {
//                addBackup(recipe);
//                return true;
//            }
//            return false;
//        });
//    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<GuiSelectionRecipe> streamRecipes() {
        return new SimpleObjectStream<>(CommonProxy.getAllRecipes()).setRemover(this::remove);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
    public void removeAll() {
        CommonProxy.getAllRecipes().forEach(this::addBackup);
        CommonProxy.getAllRecipes().clear();
    }

    @RecipeBuilderDescription(example = {
            @Example(".category('test_category').inputQuantity(1).durabilityUsage(1).time(0).output(item('minecraft:apple'))"),
            @Example(".category('another_category').inputQuantity(1).durabilityUsage(1).time(0).output(item('minecraft:clay'), item('minecraft:snowball'))")
    })
    public static RecipeBuilder recipeBuilder() {
        return new RecipeBuilder();
    }

    @Property(property = "output", comp = @Comp(gte = 1))
    public static class RecipeBuilder extends AbstractRecipeBuilder<GuiSelectionRecipe> {

        @Property(comp = @Comp(not = "null"))
        private String parentCategoryName;

        @Property(defaultValue = "1")
        private int inputQuantity = 1;

        @Property(defaultValue = "1")
        private int durabilityUsage = 1;

        @Property(defaultValue = "0")
        private int time = 0;

        @RecipeBuilderMethodDescription(field = "category")
        public RecipeBuilder category(String parentCategoryName) {
            this.parentCategoryName = parentCategoryName;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "inputQuantity")
        public RecipeBuilder inputQuantity(int inputQuantity) {
            this.inputQuantity = inputQuantity;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "durabilityUsage")
        public RecipeBuilder durabilityUsage(int durabilityUsage) {
            this.durabilityUsage = durabilityUsage;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "time")
        public RecipeBuilder time(int time) {
            this.time = time;
            return this;
        }

        @Override
        public String getErrorMsg() {
            return "Error adding" + Tags.MOD_NAME + "recipe";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateFluids(msg);
            validateItems(msg, 0, 0, 1, Integer.MAX_VALUE);
            msg.add(parentCategoryName == null, "Category name cannot be null");
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable GuiSelectionRecipe register() {
            if (!validate()) return null;
            ItemStack[] outputStack = output.toArray(new ItemStack[0]);
            GuiSelectionRecipe recipe = new GuiSelectionRecipe(parentCategoryName, inputQuantity, outputStack, time, durabilityUsage);
            GSPlugin.instance.selectionRecipe.add(parentCategoryName, recipe);
            return recipe;
        }
    }
}
