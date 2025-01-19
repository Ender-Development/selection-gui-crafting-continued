package io.enderdev.selectionguicrafting.registry;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GsTool {
    private final ItemStack item;
    private final float damageMultiplier;
    private final float timeMultiplier;

    public GsTool(ItemStack item, float damageMultiplier, float timeMultiplier) {
        this.item = item;
        this.damageMultiplier = damageMultiplier;
        this.timeMultiplier = timeMultiplier;
    }

    @NotNull
    public ItemStack getItemStack() {
        return item;
    }

    public float getDamageMultiplier() {
        return damageMultiplier == 0 ? 1.0f : damageMultiplier;
    }

    public float getTimeMultiplier() {
        return timeMultiplier == 0 ? 1.0f : timeMultiplier;
    }
}
