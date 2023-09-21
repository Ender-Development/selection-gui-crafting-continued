package gliese832c.selectionGuiCrafting.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.IngredientNBT;

import java.util.Arrays;

public class GuiSelectionRecipe {

    public int inputQuantity;
    public ItemStack[] outputs;
    public int time;
    public float chance;
    public float[] chances;

    public GuiSelectionRecipe(int inputQuantity, ItemStack[] outputs, int time, float chance, float[] chances) {
        this.inputQuantity = inputQuantity;
        this.outputs = outputs;
        this.time = time;
        this.chance = chance;
        this.chances = chances;
    }

    public GuiSelectionRecipe(int inputQuantity, ItemStack[] outputs, int time, float chance) {
        this.inputQuantity = inputQuantity;
        this.outputs = outputs;
        this.time = time;
        this.chance = chance;

        float[] fullChances = new float[outputs.length];
        Arrays.fill(fullChances, 1.0f);
        this.chances = fullChances;
    }

    public GuiSelectionRecipe(int inputQuantity, ItemStack[] outputs, int time) {
        this.inputQuantity = inputQuantity;
        this.outputs = outputs;
        this.time = time;
        this.chance = 1.0f;

        float[] fullChances = new float[outputs.length];
        Arrays.fill(fullChances, 1.0f);
        this.chances = fullChances;
    }

    public GuiSelectionRecipe(int inputQuantity, ItemStack output, int time) {
        this.inputQuantity = inputQuantity;
        this.outputs = new ItemStack[] { output };
        this.time = time;
        this.chance = 1.0f;

        float[] fullChances = new float[outputs.length];
        Arrays.fill(fullChances, 1.0f);
        this.chances = fullChances;
    }
}