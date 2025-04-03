package io.enderdev.selectionguicrafting.registry.recipe;

import io.enderdev.selectionguicrafting.registry.Register;
import io.enderdev.selectionguicrafting.registry.category.Category;
import io.enderdev.selectionguicrafting.registry.category.OutputType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class RecipeHandler {
    private final EntityPlayer player;
    private final Category category;
    private final Recipe recipe;
    private final RecipeHelper recipeHelper;
    private final double durabilityMultiplier;
    private final int xp;

    public RecipeHandler(EntityPlayer player, Category category, int recipeIndex, double durabilityMultiplier, int xp) {
        this.player = player;
        this.category = category;
        this.recipe = Register.getRecipesByCategory(category).get(recipeIndex);
        this.recipeHelper = new RecipeHelper(recipe);
        this.durabilityMultiplier = durabilityMultiplier;
        this.xp = xp;
    }

    public boolean validate() {
        if (player == null || category == null) {
            return false;
        }

        if (recipe == null) {
            return false;
        }

        return recipeHelper.canCraft(player);
    }

    public void craft() {
        if (!validate()) {
            return;
        }

        // If we got here, the recipe is valid and can be processed
        Random random = new Random();
        recipe.getOutputs().forEach(output -> {
            ItemStack stack = output.getItemStack().copy();
            OutputType outputType = recipeHelper.getOutputType();
            if (random.nextDouble() < output.getChance()) {
                if (outputType == OutputType.DROP) {
                    player.dropItem(stack, false, true);
                } else {
                    if (!player.inventory.addItemStackToInventory(stack)) {
                        player.dropItem(stack, false, true);
                    }
                }
            }
        });

        if (recipeHelper.hasMainHand()) {
            recipe.getMainHand().consume(player.getHeldItemMainhand());
        }

        if (recipeHelper.hasOffHand()) {
            recipe.getOffHand().consume(player.getHeldItemOffhand());
        }

        player.addExperience(xp);
    }
}
