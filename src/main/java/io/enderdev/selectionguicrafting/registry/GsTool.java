package io.enderdev.selectionguicrafting.registry;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GsTool {
    private ItemStack item;
    private Float damageMultiplier;
    private Float timeMultiplier;

    public GsTool(ItemStack item, float damageMultiplier, float timeMultiplier) {
        this.item = item;
        this.damageMultiplier = damageMultiplier;
        this.timeMultiplier = timeMultiplier;
    }

    public GsTool() {
    }

    public GsTool setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public GsTool setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
        return this;
    }

    public GsTool setTimeMultiplier(float timeMultiplier) {
        this.timeMultiplier = timeMultiplier;
        return this;
    }

    @NotNull
    public ItemStack getItemStack() {
        return item;
    }

    public float getDamageMultiplier() {
        return damageMultiplier == null ? 1.0f : damageMultiplier;
    }

    public float getTimeMultiplier() {
        return timeMultiplier == null ? 1.0f : timeMultiplier;
    }
}
