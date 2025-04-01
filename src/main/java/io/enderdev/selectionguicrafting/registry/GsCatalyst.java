package io.enderdev.selectionguicrafting.registry;

import net.minecraft.item.crafting.Ingredient;

@Deprecated
public class GsCatalyst {
    private final Ingredient ingredient;
    private final float chance;

    public GsCatalyst(Ingredient ingredient, float chance) {
        this.ingredient = ingredient;
        this.chance = chance;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public float getChance() {
        return chance;
    }
}
