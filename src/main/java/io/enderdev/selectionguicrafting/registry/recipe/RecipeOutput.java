package io.enderdev.selectionguicrafting.registry.recipe;

import net.minecraft.item.ItemStack;

import java.util.Random;

public class RecipeOutput {
    private final Random random = new Random();

    private final ItemStack output;
    private final double chance;

    public RecipeOutput(ItemStack output, double chance) {
        this.output = output;
        this.chance = chance;
    }

    public RecipeOutput(ItemStack output) {
        this.output = output;
        this.chance = 1.0;
    }

    public ItemStack getItemStack() {
        return output;
    }

    public double getChance() {
        return chance;
    }

    public boolean beConsumed() {
        return random.nextDouble() <= getChance();
    }
}
