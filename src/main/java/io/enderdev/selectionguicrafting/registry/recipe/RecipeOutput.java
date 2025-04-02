package io.enderdev.selectionguicrafting.registry.recipe;

import net.minecraft.item.ItemStack;

import java.util.Random;

public class RecipeOutput {
    private final Random random = new Random();

    private final ItemStack input;
    private final double chance;

    public RecipeOutput(ItemStack input, double chance) {
        this.input = input;
        this.chance = chance;
    }

    public RecipeOutput(ItemStack input) {
        this.input = input;
        this.chance = 1.0;
    }

    public ItemStack getItemStack() {
        return input;
    }

    public double getChance() {
        return chance;
    }

    public boolean beConsumed() {
        return random.nextDouble() <= getChance();
    }
}
