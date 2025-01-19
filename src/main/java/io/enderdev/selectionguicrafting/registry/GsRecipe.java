package io.enderdev.selectionguicrafting.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GsRecipe {
    // Required
    private String category;
    private final ArrayList<Ingredient> inputs = new ArrayList<>();
    private final ArrayList<GsOutput> outputs = new ArrayList<>();
    private final ArrayList<GsTool> tools = new ArrayList<>();

    // Optional
    private Integer time;
    private Integer xp;
    private Integer durability;
    private Integer amount;

    // Override category
    private final ArrayList<ResourceLocation> sounds = new ArrayList<>();
    private GsEnum.OutputType outputType;
    private GsEnum.QueueType queueable;

    public GsRecipe() {
    }

    /**
     * Set the category of the recipe
     *
     * @param category The category
     * @return The recipe
     */
    public GsRecipe setCategory(@NotNull String category) {
        this.category = category;
        return this;
    }

    /**
     * Set the output type of the recipe
     *
     * @param outputType The output type
     * @return The recipe
     */
    public GsRecipe setOutputType(@NotNull GsEnum.OutputType outputType) {
        this.outputType = outputType;
        return this;
    }

    /**
     * Set the recipe to be queueable
     *
     * @param queueable The queueable type
     * @return The recipe
     */
    public GsRecipe setQueueable(@NotNull GsEnum.QueueType queueable) {
        this.queueable = queueable;
        return this;
    }

    /**
     * Add an input to the recipe
     *
     * @param input The input to add
     * @return The recipe
     */
    public GsRecipe addInput(@NotNull Ingredient input) {
        inputs.add(input);
        return this;
    }

    public GsRecipe addInput(@NotNull ItemStack input) {
        inputs.add(Ingredient.fromStacks(input));
        return this;
    }

    public GsRecipe addInput(@NotNull Block input) {
        inputs.add(Ingredient.fromItems(Item.getItemFromBlock(input)));
        return this;
    }

    public GsRecipe addInput(@NotNull ArrayList<Ingredient> input) {
        inputs.addAll(input);
        return this;
    }

    /**
     * Add a sound to the recipe
     *
     * @param sound The sound to add
     * @return The recipe
     */
    public GsRecipe addSound(@NotNull ResourceLocation sound) {
        sounds.add(sound);
        return this;
    }

    /**
     * Add an output to the recipe
     *
     * @param output The output to add
     * @param chance The chance of the output
     * @return The recipe
     */
    public GsRecipe addOutput(@NotNull ItemStack output, float chance) {
        outputs.add(new GsOutput(output, chance));
        return this;
    }

    public GsRecipe addOutput(@NotNull Block output, float chance) {
        outputs.add(new GsOutput(new ItemStack(output), chance));
        return this;
    }

    public GsRecipe addOutput(@NotNull ArrayList<GsOutput> output) {
        outputs.addAll(output);
        return this;
    }

    /**
     * Add a tool to the recipe
     *
     * @param tool             The tool to add
     * @param damageMultiplier The damage multiplier for the tool
     * @param timeMultiplier   The time multiplier for the tool
     * @return The recipe
     */
    public GsRecipe addTool(@NotNull ItemStack tool, float damageMultiplier, float timeMultiplier) {
        tools.add(new GsTool(tool, damageMultiplier, timeMultiplier));
        return this;
    }

    public GsRecipe addTool(@NotNull Block tool, float damageMultiplier, float timeMultiplier) {
        tools.add(new GsTool(new ItemStack(tool), damageMultiplier, timeMultiplier));
        return this;
    }

    /**
     * Set the time the recipe will take
     *
     * @param time The time the recipe will take
     * @return The recipe
     */
    public GsRecipe setTime(int time) {
        this.time = time;
        return this;
    }

    /**
     * Set the experience the recipe will give
     *
     * @param xp The experience the recipe will give
     * @return The recipe
     */
    public GsRecipe setXp(int xp) {
        this.xp = xp;
        return this;
    }

    /**
     * Set the durability the recipe will consume
     *
     * @param durability The durability the recipe will consume
     * @return The recipe
     */
    public GsRecipe setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    /**
     * Set the amount of inputs to be consumed
     *
     * @param amount The amount of inputs to be consumed
     * @return The recipe
     */
    public GsRecipe setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getCategory() {
        return category;
    }

    @NotNull
    public ArrayList<Ingredient> getInputs() {
        return inputs;
    }

    @NotNull
    public ArrayList<GsOutput> getOutputs() {
        return outputs;
    }

    /**
     * Get the chance of an output
     *
     * @param output The output to get the chance for
     * @return The chance of the output
     */
    public float getOutputChance(GsOutput output) {
        return output.getChance();
    }

    /**
     * Get the valid tools for the recipe
     *
     * @return The valid tools for the recipe
     */
    public ArrayList<GsTool> getTools() {
        return tools;
    }

    /**
     * Get the damage multiplier for a tool
     *
     * @param tool The tool to get the damage multiplier for
     * @return The damage multiplier for the tool
     */
    public float getDamageMultiplier(GsTool tool) {
        return tool.getDamageMultiplier();
    }

    /**
     * Get the time multiplier for a tool
     *
     * @param tool The tool to get the time multiplier for
     * @return The time multiplier for the tool
     */
    public float getTimeMultiplier(GsTool tool) {
        return tool.getTimeMultiplier();
    }

    /**
     * Get the time the recipe will take
     *
     * @return The time the recipe will take
     */
    public int getTime() {
        return time == null ? 20 : time;
    }

    /**
     * Get the experience the recipe will give
     *
     * @return The experience the recipe will give
     */
    public int getXp() {
        return xp == null ? 0 : xp;
    }

    /**
     * Get the durability the recipe will consume
     *
     * @return The durability the recipe will consume
     */
    public int getDurability() {
        return durability == null ? 0 : durability;
    }

    /**
     * Get the amount of inputs to be consumed
     *
     * @return The amount of inputs to be consumed
     */
    public int getAmount() {
        return amount == null ? 1 : amount;
    }

    @Nullable
    public ArrayList<ResourceLocation> getSounds() {
        return sounds;
    }

    @Nullable
    public GsEnum.OutputType getOutputType() {
        return outputType;
    }

    @Nullable
    public GsEnum.QueueType getQueueable() {
        return queueable;
    }
}
