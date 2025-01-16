package io.enderdev.selectionguicrafting.recipe;

import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class GuiSelectionRecipe {

    public String parentCategoryName;

    public int inputQuantity;
    public ItemStack[] outputs;
    public int time;
    public int durabilityUsage;
    public float chance;
    public float[] chances;

    public GuiSelectionRecipe(String parentCategoryName, int inputQuantity, ItemStack[] outputs, int time, int durabilityUsage, float chance, float[] chances) {
        this.inputQuantity = inputQuantity;
        this.outputs = outputs;
        this.time = time;
        this.durabilityUsage = durabilityUsage;
        this.chance = chance;
        this.chances = chances;
    }

    public GuiSelectionRecipe(String parentCategoryName, int inputQuantity, ItemStack[] outputs, int time, int durabilityUsage, float chance) {
        this.inputQuantity = inputQuantity;
        this.outputs = outputs;
        this.time = time;
        this.durabilityUsage = durabilityUsage;
        this.chance = chance;

        float[] fullChances = new float[outputs.length];
        Arrays.fill(fullChances, 1.0f);
        this.chances = fullChances;
    }

    public GuiSelectionRecipe(String parentCategoryName, int inputQuantity, ItemStack[] outputs, int time, int durabilityUsage) {
        this.inputQuantity = inputQuantity;
        this.outputs = outputs;
        this.time = time;
        this.durabilityUsage = durabilityUsage;
        this.chance = 1.0f;

        float[] fullChances = new float[outputs.length];
        Arrays.fill(fullChances, 1.0f);
        this.chances = fullChances;
    }

    public GuiSelectionRecipe(String parentCategoryName, int inputQuantity, ItemStack output, int time, int durabilityUsage) {
        this.inputQuantity = inputQuantity;
        this.outputs = new ItemStack[] { output };
        this.time = time;
        this.durabilityUsage = durabilityUsage;
        this.chance = 1.0f;

        float[] fullChances = new float[outputs.length];
        Arrays.fill(fullChances, 1.0f);
        this.chances = fullChances;
    }
}