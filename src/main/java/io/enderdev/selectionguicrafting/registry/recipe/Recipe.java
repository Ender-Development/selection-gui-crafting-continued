package io.enderdev.selectionguicrafting.registry.recipe;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.registry.IRegisterObject;
import io.enderdev.selectionguicrafting.registry.Register;
import io.enderdev.selectionguicrafting.registry.category.OutputType;
import io.enderdev.selectionguicrafting.registry.category.QueueType;
import io.enderdev.selectionguicrafting.registry.category.SoundType;
import io.enderdev.selectionguicrafting.registry.util.Particle;
import io.enderdev.selectionguicrafting.registry.util.Sound;
import io.enderdev.selectionguicrafting.registry.util.Validation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class Recipe implements IRegisterObject {
    private final Validation ErrorCheck = new Validation();
    private final RecipeData ScreenData = new RecipeData();
    // required
    private final ArrayList<RecipeInput> inputs = new ArrayList<>();
    private final ArrayList<RecipeOutput> outputs = new ArrayList<>();
    private String category;

    // optional
    private RecipeInput mainHand;
    private RecipeInput offHand;
    private Integer time;
    private Integer xp;

    public Recipe() {
        this.time = 20;
        this.xp = 0;
    }

    /* --------------------- */
    /* ---- VALIDATION ----- */
    /* --------------------- */

    @Override
    public IRegisterObject register() {
        if (!validate()) {
            return null;
        }
        Register.addRecipe(this);
        return this;
    }

    @Override
    public boolean validate() {
        if (category == null) {
            ErrorCheck.error("Category must be set.");
        }
        if (inputs.isEmpty() && mainHand == null && offHand == null) {
            ErrorCheck.error("At least one input must be set.");
        }
        if (inputs.stream().anyMatch(rIn -> Arrays.stream(rIn.getIngredient().getMatchingStacks()).mapToInt(ItemStack::getMaxStackSize).max().orElse(64) > rIn.getAmount())) {
            ErrorCheck.error("Input amount is greater than max stack size.");
        }
        if (outputs.isEmpty()) {
            ErrorCheck.error("At least one output must be set.");
        }
        if (!ErrorCheck.valid()) {
            SelectionGuiCrafting.LOGGER.error("Recipe is invalid: {}", ErrorCheck.msg());
            return false;
        }
        return true;
    }

    /* --------------------- */
    /* ------ CATEGORY ----- */
    /* --------------------- */

    public Recipe category(String category) {
        this.category = category;
        return this;
    }

    public String getCategory() {
        return this.category;
    }

    /* --------------------- */
    /* ------ INPUTS ------- */
    /* --------------------- */

    public Recipe input(Collection<?> inputs) {
        for (Object input : inputs) {
            if (input instanceof RecipeInput) {
                this.inputs.add((RecipeInput) input);
            } else if (input instanceof Ingredient) {
                this.inputs.add(new RecipeInput((Ingredient) input));
            }
        }
        return this;
    }

    public Recipe input(RecipeInput input) {
        this.inputs.add(input);
        return this;
    }

    public Recipe input(RecipeInput... inputs) {
        Collections.addAll(this.inputs, inputs);
        return this;
    }

    public Recipe input(Ingredient input) {
        this.inputs.add(new RecipeInput(input));
        return this;
    }

    public Recipe input(Ingredient... inputs) {
        for (Ingredient input : inputs) {
            this.inputs.add(new RecipeInput(input));
        }
        return this;
    }

    public Recipe input(Ingredient input, int damage) {
        this.inputs.add(new RecipeInput(input, damage));
        return this;
    }

    public Recipe input(Ingredient input, double chance) {
        this.inputs.add(new RecipeInput(input, chance));
        return this;
    }

    public Recipe input(Ingredient input, double chance, int damage) {
        this.inputs.add(new RecipeInput(input, chance, damage));
        return this;
    }

    public List<RecipeInput> getInputs() {
        return inputs;
    }

    /* --------------------- */
    /* ------ OUTPUTS ------ */
    /* --------------------- */

    public Recipe output(Collection<?> outputs) {
        for (Object output : outputs) {
            if (output instanceof RecipeOutput) {
                this.outputs.add((RecipeOutput) output);
            } else if (output instanceof ItemStack) {
                this.outputs.add(new RecipeOutput((ItemStack) output));
            }
        }
        return this;
    }

    public Recipe output(RecipeOutput output) {
        this.outputs.add(output);
        return this;
    }

    public Recipe output(RecipeOutput... outputs) {
        Collections.addAll(this.outputs, outputs);
        return this;
    }

    public Recipe output(ItemStack output) {
        this.outputs.add(new RecipeOutput(output));
        return this;
    }

    public Recipe output(ItemStack... outputs) {
        for (ItemStack output : outputs) {
            this.outputs.add(new RecipeOutput(output));
        }
        return this;
    }

    public Recipe output(ItemStack output, double chance) {
        this.outputs.add(new RecipeOutput(output, chance));
        return this;
    }

    public List<RecipeOutput> getOutputs() {
        return outputs;
    }

    /* --------------------- */
    /* ------ HANDS -------- */
    /* --------------------- */

    public Recipe mainHand(RecipeInput input) {
        this.mainHand = input;
        return this;
    }

    public Recipe mainHand(Ingredient input) {
        this.mainHand = new RecipeInput(input);
        return this;
    }

    public Recipe mainHand(Ingredient input, int damage) {
        this.mainHand = new RecipeInput(input, damage);
        return this;
    }

    public Recipe mainHand(Ingredient input, double chance) {
        this.mainHand = new RecipeInput(input, chance);
        return this;
    }

    public Recipe mainHand(Ingredient input, double chance, int damage) {
        this.mainHand = new RecipeInput(input, chance, damage);
        return this;
    }

    public RecipeInput getMainHand() {
        return mainHand;
    }

    public Recipe offHand(RecipeInput input) {
        this.offHand = input;
        return this;
    }

    public Recipe offHand(Ingredient input) {
        this.offHand = new RecipeInput(input);
        return this;
    }

    public Recipe offHand(Ingredient input, int damage) {
        this.offHand = new RecipeInput(input, damage);
        return this;
    }

    public Recipe offHand(Ingredient input, double chance) {
        this.offHand = new RecipeInput(input, chance);
        return this;
    }

    public Recipe offHand(Ingredient input, double chance, int damage) {
        this.offHand = new RecipeInput(input, chance, damage);
        return this;
    }

    public RecipeInput getOffHand() {
        return offHand;
    }

    /* --------------------- */
    /* ------ TIME --------- */
    /* --------------------- */

    public Recipe time(int time) {
        this.time = time;
        return this;
    }

    public int getTime() {
        return time;
    }

    /* --------------------- */
    /* ------- XP ---------- */
    /* --------------------- */

    public Recipe xp(int xp) {
        this.xp = xp;
        return this;
    }

    public int getXP() {
        return xp;
    }

    /* --------------------- */
    /* ---- SCREENDATA ----- */
    /* --------------------- */

    public RecipeData getRecipeData() {
        return ScreenData;
    }

    public Recipe setFrame(ResourceLocation frame) {
        ScreenData.setFrame(frame);
        return this;
    }

    public Recipe setProgressBar(ResourceLocation progressBar) {
        ScreenData.setProgressBar(progressBar);
        return this;
    }

    public Recipe setOutputType(OutputType outputType) {
        ScreenData.setOutputType(outputType);
        return this;
    }

    public Recipe setQueueable(QueueType queueable) {
        ScreenData.setQueueable(queueable);
        return this;
    }

    public Recipe setSoundType(SoundType soundType) {
        ScreenData.setSoundType(soundType);
        return this;
    }

    public Recipe addSound(Sound sound) {
        ScreenData.addSound(sound);
        return this;
    }

    public Recipe addParticle(Particle particle) {
        ScreenData.addParticle(particle);
        return this;
    }
}
