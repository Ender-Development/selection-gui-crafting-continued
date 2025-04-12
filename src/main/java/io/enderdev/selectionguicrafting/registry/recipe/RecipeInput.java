package io.enderdev.selectionguicrafting.registry.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.Random;

public class RecipeInput {
    private final Random random = new Random();

    private final Ingredient input;
    private final double chance;
    private final int damage;
    private final int amount;

    public RecipeInput(Ingredient input, double chance, int damage) {
        this.input = input;
        this.chance = chance;
        this.damage = damage;
        this.amount = input.getMatchingStacks()[0].getCount();
    }

    public RecipeInput(Ingredient input, double chance) {
        this(input, chance, 0);
    }

    public RecipeInput(Ingredient input, int damage) {
        this(input, 1.0, damage);
    }

    public RecipeInput(Ingredient input) {
        this(input, 1.0, 0);
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

    public int getAmount() {
        return Math.min(amount, Arrays.stream(getIngredient().getMatchingStacks()).mapToInt(ItemStack::getMaxStackSize).max().orElse(64));
    }

    public boolean isDamageable() {
        return Arrays.stream(getIngredient().getMatchingStacks()).anyMatch(e -> e.getItem().isDamageable());
    }

    public boolean beConsumed() {
        return random.nextDouble() <= getChance();
    }

    /**
     * Consumes the item stack if the chance is met.
     * If the item is damageable, it will damage the item.
     * If the item is not damageable, it will remove the amount from the stack.
     * @param stack the stack to consume
     */
    public void consume(ItemStack stack) {
        if (!beConsumed()) {
            return;
        }
        if (stack.isItemStackDamageable() && getDamage() > 0) {
            int damage = stack.getItemDamage() + getDamage();
            if (damage >= stack.getMaxDamage()) {
                stack.shrink(1);
            } else {
                stack.setItemDamage(damage);
            }
        } else {
            if (stack.getCount() > getAmount()) {
                stack.shrink(getAmount());
            } else {
                stack.setCount(0);
            }
        }
    }

    /**
     * Compares the input with the given stack.
     * (The +1 is to account that tool durability is 0 based)
     * @param stack the stack to compare with
     * @return true if the input matches the stack, false otherwise
     */
    public boolean compare(ItemStack stack) {
        boolean isItemEqual = Arrays.stream(getIngredient().getMatchingStacks()).anyMatch(matching -> matching.isItemEqualIgnoreDurability(stack));
        boolean hasEnoughDurability = !stack.getItem().isDamageable() || stack.getMaxDamage() - stack.getItemDamage() + 1 >= getDamage();
        boolean hasEnoughStackSize = stack.getCount() >= getAmount();

        return isItemEqual && hasEnoughDurability && hasEnoughStackSize;
    }
}
