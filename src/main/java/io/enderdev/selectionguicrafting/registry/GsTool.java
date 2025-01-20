package io.enderdev.selectionguicrafting.registry;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GsTool && item.isItemEqual(((GsTool) obj).item) && damageMultiplier == ((GsTool) obj).damageMultiplier && timeMultiplier == ((GsTool) obj).timeMultiplier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, damageMultiplier, timeMultiplier);
    }
}
