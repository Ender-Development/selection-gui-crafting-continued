package io.enderdev.selectionguicrafting.registry.recipe;

import io.enderdev.selectionguicrafting.gui.Assets;
import io.enderdev.selectionguicrafting.registry.Register;
import io.enderdev.selectionguicrafting.registry.category.*;
import io.enderdev.selectionguicrafting.registry.util.Particle;
import io.enderdev.selectionguicrafting.registry.util.Sound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeHelper {
    private final Recipe recipe;
    private final RecipeData recipeData;
    private final Category category;
    private final CategoryData categoryData;

    public RecipeHelper(Recipe recipe) {
        this.recipe = recipe;
        this.category = Register.getCategoryByID(recipe.getCategory());
        this.recipeData = recipe.getRecipeData();
        this.categoryData = category.getScreenData();
    }

    public ResourceLocation getFrame() {
        if (!recipeData.getFrame().toString().equals(Assets.FRAME_DEFAULT.get().toString())) {
            return recipeData.getFrame();
        } else if (!categoryData.getFrame().toString().equals(Assets.FRAME_DEFAULT.get().toString())) {
            return recipeData.getFrame();
        }
        return categoryData.getFrame();
    }

    public ResourceLocation getProgressBar() {
        if (!recipeData.getProgressBar().toString().equals(Assets.BAR_DEFAULT.get().toString())) {
            return recipeData.getProgressBar();
        } else if (!categoryData.getProgressBar().toString().equals(Assets.BAR_DEFAULT.get().toString())) {
            return recipeData.getProgressBar();
        }
        return categoryData.getProgressBar();
    }

    public OutputType getOutputType() {
        if (recipeData.getOutputType() != OutputType.DROP) {
            return recipeData.getOutputType();
        } else if (categoryData.getOutputType() != OutputType.DROP) {
            return recipeData.getOutputType();
        }
        return categoryData.getOutputType();
    }

    public QueueType getQueueable() {
        if (recipeData.getQueueable() != QueueType.YES) {
            return recipeData.getQueueable();
        } else if (categoryData.getQueueable() != QueueType.YES) {
            return recipeData.getQueueable();
        }
        return categoryData.getQueueable();
    }

    public SoundType getSoundType() {
        if (recipeData.getSoundType() != SoundType.RANDOM) {
            return recipeData.getSoundType();
        } else if (categoryData.getSoundType() != SoundType.RANDOM) {
            return recipeData.getSoundType();
        }
        return categoryData.getSoundType();
    }

    public ArrayList<Sound> getSounds() {
        if (!recipeData.getSounds().isEmpty()) {
            return recipeData.getSounds();
        } else if (!categoryData.getSounds().isEmpty()) {
            return categoryData.getSounds();
        }
        return new ArrayList<Sound>() {{
            add(new Sound(new ResourceLocation("minecraft", "block.anvil.use"), 0.1f, 1));
            add(new Sound(new ResourceLocation("minecraft", "block.anvil.break"), 0.1f, 1));
        }};
    }

    public ArrayList<Particle> getParticles() {
        if (!recipeData.getParticles().isEmpty()) {
            return recipeData.getParticles();
        } else if (!categoryData.getParticles().isEmpty()) {
            return categoryData.getParticles();
        }
        return new ArrayList<Particle>() {{
            add(new Particle(EnumParticleTypes.VILLAGER_HAPPY, 10, 0.5f));
        }};
    }

    public boolean canCraft(EntityPlayer player) {
        ItemStack mainHand = player.getHeldItemMainhand();
        ItemStack offHand = player.getHeldItemOffhand();
        ArrayList<ItemStack> inventory = player.inventory.mainInventory.stream().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        if (hasMainHand() && (mainHand.isEmpty() || !recipe.getMainHand().getIngredient().test(mainHand)))
            return false;
        if (hasOffHand() && (offHand.isEmpty() || !recipe.getOffHand().getIngredient().test(offHand)))
            return false;

        // go through all recipeInputs, if all can be found in the simplified inventory, return true
        for (RecipeInput input : recipe.getInputs()) {
            boolean found = false;
            for (ItemStack stack : inventory)
                if (input.compare(stack)) {
                    // remove the stack from the inventory
                    // this way we will return false if we
                    // need more than one stack of the same item
                    inventory.remove(stack);
                    found = true;
                    break;
                }
            if (!found)
                return false;
        }

        return true;
    }

    public int getAbsoluteDamage(RecipeInput input, double multiplier) {
        if (!input.isDamageable()) {
            return 0;
        }
        return (int) (input.getDamage() * multiplier);
    }

    public int getAbsoluteXP(double multiplier) {
        return (int) (recipe.getXP() * multiplier);
    }

    public boolean hasMainHand() {
        return recipe.getMainHand() != null && recipe.getMainHand().getIngredient().getMatchingStacks().length != 0;
    }

    public boolean hasOffHand() {
        return recipe.getOffHand() != null && recipe.getOffHand().getIngredient().getMatchingStacks().length != 0;
    }
}
