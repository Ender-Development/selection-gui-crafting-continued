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
import io.enderdev.selectionguicrafting.recipe.GuiSelectionItemPair;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RegistryDescription(linkGenerator = Tags.MOD_ID)
public class SelectionPair extends VirtualizedRegistry<GuiSelectionItemPair> {

    @Override
    @GroovyBlacklist
    public void onReload() {
        CommonProxy.selectionCraftingItems.removeAll(removeScripted());
        CommonProxy.selectionCraftingItems.addAll(restoreFromBackup());
    }

    public void add(GuiSelectionItemPair pair) {
        if (pair != null) {
            addScripted(pair);
            CommonProxy.selectionCraftingItems.add(pair);
        }
    }

    public boolean remove(GuiSelectionItemPair pair) {
        if (CommonProxy.selectionCraftingItems.removeIf(pair::equals)) {
            addBackup(pair);
            return true;
        }
        return false;
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("'also_a_test_category'"), description = "groovyscript.wiki.selectionguicrafting.removebycategory")
    public boolean removeByCategory(String category) {
        return CommonProxy.selectionCraftingItems.removeIf(pair -> {
            if (pair.recipeCategory.equals(category)) {
                addBackup(pair);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:stone_pickaxe')"), description = "groovyscript.wiki.selectionguicrafting.removebytool")
    public boolean removeByTool(IIngredient tool) {
        return CommonProxy.selectionCraftingItems.removeIf(pair -> {
            if (pair.tool.stream().anyMatch(tool)) {
                addBackup(pair);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("item('minecraft:stone')"))
    public boolean removeByInput(IIngredient input) {
        return CommonProxy.selectionCraftingItems.removeIf(pair -> {
            if (pair.input.stream().anyMatch(input)) {
                addBackup(pair);
                return true;
            }
            return false;
        });
    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<GuiSelectionItemPair> streamPairs() {
        return new SimpleObjectStream<>(CommonProxy.selectionCraftingItems).setRemover(this::remove);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, priority = 2000, example = @Example(commented = true))
    public void removeAll() {
        CommonProxy.selectionCraftingItems.forEach(this::addBackup);
        CommonProxy.selectionCraftingItems.clear();
    }

    @RecipeBuilderDescription(example = {
            @Example(".input(item('minecraft:dirt'),item('minecraft:cobblestone')).addTool(item('minecraft:stone_pickaxe'), 1.0f, 1.0f).category('test_category')"),
            @Example(".input(item('minecraft:wool'),item('minecraft:diamond')).addTool(item('minecraft:golden_pickaxe'), 1.0f, 1.0f).addTool(item('minecraft:iron_pickaxe'), 1.0f, 1.0f).category('another_category')"),
    })
    public static PairBuilder pairBuilder() {
        return new PairBuilder();
    }

    @Property(property = "input", comp = @Comp(gte = 1))
    public static class PairBuilder extends AbstractRecipeBuilder<GuiSelectionItemPair> {
        @Property
        ArrayList<Float> timeMultipliers = new ArrayList<>();

        @Property
        ArrayList<Float> durabilityMultipliers = new ArrayList<>();

        @Property
        ArrayList<IIngredient> tool = new ArrayList<>();

        @Property(comp = @Comp(not = "null"))
        String recipeCategory;

        @RecipeBuilderMethodDescription(field = {"timeMultipliers", "durabilityMultipliers", "tool"})
        public PairBuilder addTool(IIngredient tool, float timeMultiplier, float durabilityMultiplier) {
            this.tool.add(tool);
            this.timeMultipliers.add(timeMultiplier);
            this.durabilityMultipliers.add(durabilityMultiplier);
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipeCategory")
        public PairBuilder category(String category) {
            this.recipeCategory = category;
            return this;
        }

        @Override
        public String getErrorMsg() {
            return "There was an error creating the selection pair";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateFluids(msg);
            validateItems(msg, 1, Integer.MAX_VALUE, 0, 0);
            msg.add(timeMultipliers.isEmpty(), "Time Multipliers cannot be empty");
            msg.add(durabilityMultipliers.isEmpty(), "Durability Multipliers cannot be empty");
            msg.add(tool.isEmpty(), "Tool cannot be empty");
            msg.add(recipeCategory == null, "Recipe Category cannot be null");
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable GuiSelectionItemPair register() {
            if (!validate()) {
                return null;
            }
            GuiSelectionItemPair pair = new GuiSelectionItemPair(
                    tool.stream().map(IIngredient::getMatchingStacks).map(itemStacks -> itemStacks[0]).collect(Collectors.toCollection(ArrayList::new)),
                    timeMultipliers,
                    durabilityMultipliers,
                    input.stream().map(IIngredient::getMatchingStacks).map(itemStacks -> itemStacks[0]).collect(Collectors.toCollection(ArrayList::new)),
                    recipeCategory);
            GSPlugin.instance.selectionPair.add(pair);
            return pair;
        }
    }
}
