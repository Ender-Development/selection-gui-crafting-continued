package io.enderdev.selectionguicrafting.registry;

import net.minecraft.item.ItemStack;

public class GsOutput {
    private final ItemStack itemStack;
    private final float chance;

    public GsOutput(ItemStack itemStack, float chance) {
        this.itemStack = itemStack;
        this.chance = chance;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public float getChance() {
        return chance == 0 ? 1.0f : chance;
    }
}
