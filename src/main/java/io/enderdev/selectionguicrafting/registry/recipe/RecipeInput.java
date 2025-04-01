package io.enderdev.selectionguicrafting.registry.recipe;

import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.Random;

public class RecipeInput {
    private final Random random = new Random();

    private final Ingredient input;
    private final double chance;
    private final int damage;

    public RecipeInput(Ingredient input, float chance) {
        this.input = input;
        this.chance = chance;
        this.damage = 0;
    }

    public RecipeInput(Ingredient input, int damage) {
        this.input = input;
        this.chance = 0.0;
        this.damage = damage;
    }

    public RecipeInput(Ingredient input) {
        this.input = input;
        this.chance = 1.0;
        this.damage = 0;
    }

    public Ingredient getIngredient() {
        return input;
    }

    public double getChance() {
        return chance;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isDamageable() {
        return Arrays.stream(getIngredient().getMatchingStacks()).anyMatch(e -> e.getItem().isDamageable());
    }

    public boolean beConsumed() {
        return random.nextDouble() <= getChance();
    }
}
