package io.enderdev.selectionguicrafting.registry;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("UnusedReturnValue")
public class GsRecipe {
    // Required
    private String category;
    private final ArrayList<Ingredient> input = new ArrayList<>();
    private final ArrayList<GsOutput> output = new ArrayList<>();
    private final ArrayList<GsTool> tool = new ArrayList<>();

    // Optional
    private GsCatalyst catalyst;
    private Integer time;
    private Integer xp;
    private Integer durability;

    // Override category
    private final ArrayList<GsSound> sounds = new ArrayList<>();
    private final ArrayList<GsParticle> particles = new ArrayList<>();
    private ResourceLocation frame;
    private ResourceLocation progressBar;
    private GsEnum.OutputType outputType;
    private GsEnum.QueueType queueable;
    private GsEnum.SoundType soundType;

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

    public GsRecipe setFrame(@NotNull ResourceLocation frame) {
        this.frame = frame;
        return this;
    }

    public GsRecipe setProgressBar(@NotNull ResourceLocation progressBar) {
        this.progressBar = progressBar;
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
     * Set the sound type of the recipe
     *
     * @param soundType The sound type
     * @return The recipe
     */
    public GsRecipe setSoundType(@NotNull GsEnum.SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    /**
     * Set the catalyst of the recipe
     *
     * @param ingredient The catalyst
     * @param chance The chance of the catalyst being consumed
     * @return The recipe
     */
    public GsRecipe setCatalyst(Ingredient ingredient, float chance) {
        this.catalyst = new GsCatalyst(ingredient, chance);
        return this;
    }

    /**
     * Add an input to the recipe
     *
     * @param input The input to add
     * @return The recipe
     */
    public GsRecipe addInput(@NotNull Ingredient input) {
        this.input.add(input);
        return this;
    }

    public GsRecipe addInput(@NotNull ItemStack input) {
        this.input.add(Ingredient.fromStacks(input));
        return this;
    }

    public GsRecipe addInput(@NotNull Block input) {
        this.input.add(Ingredient.fromItems(Item.getItemFromBlock(input)));
        return this;
    }

    public GsRecipe addInput(@NotNull ArrayList<Ingredient> input) {
        this.input.addAll(input);
        return this;
    }

    /**
     * Add a sound to the recipe
     *
     * @param sound The sound to add
     * @return The recipe
     */
    public GsRecipe addSound(@NotNull ResourceLocation sound, float volume, float pitch) {
        sounds.add(new GsSound(sound, volume, pitch));
        return this;
    }

    /**
     * Add a particle to the recipe
     *
     * @param particle The particle to add
     * @param count The count of the particle
     * @param speed The speed of the particle
     * @return The recipe
     */
    public GsRecipe addParticle(@NotNull EnumParticleTypes particle, int count, float speed) {
        particles.add(new GsParticle(particle, count, speed));
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
        this.output.add(new GsOutput(output, chance));
        return this;
    }

    public GsRecipe addOutput(@NotNull Block output, float chance) {
        this.output.add(new GsOutput(new ItemStack(output), chance));
        return this;
    }

    public GsRecipe addOutput(@NotNull ArrayList<GsOutput> output) {
        this.output.addAll(output);
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
        if (this.tool.stream().anyMatch(gsTool -> gsTool.getItemStack().isItemEqual(tool))) {
            SelectionGuiCrafting.LOGGER.warn("Tool {} was already added to recipe", tool);
            return this;
        }
        this.tool.add(new GsTool(tool, damageMultiplier, timeMultiplier));
        return this;
    }

    public GsRecipe addTool(@NotNull Block tool, float damageMultiplier, float timeMultiplier) {
        addTool(new ItemStack(tool), damageMultiplier, timeMultiplier);
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

    public String getCategory() {
        return category;
    }

    @NotNull
    public ArrayList<Ingredient> getInput() {
        return input;
    }

    @Nullable
    public Ingredient getInput(ItemStack itemStack) {
        return input.stream().filter(ingredient -> ingredient.apply(itemStack)).findFirst().orElse(null);
    }

    public int getInputStackSize(ItemStack itemStack) {
        return Objects.requireNonNull(getInput(itemStack)).getMatchingStacks()[0].getCount();
    }

    @NotNull
    public ArrayList<GsOutput> getOutput() {
        return output;
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
    public ArrayList<GsTool> getTool() {
        return tool;
    }

    /**
     * Get a tool by item stack
     *
     * @param itemStack The item stack to get the tool for
     * @return The tool for the item stack
     */
    @Nullable
    public GsTool getTool(ItemStack itemStack) {
        return tool.stream().filter(tool -> {
            Item item = tool.getItemStack().getItem();
            int meta = tool.getItemStack().getMetadata();
            NBTTagCompound tag = tool.getItemStack().getTagCompound();
            return item == itemStack.getItem() && (tool.getItemStack().isItemStackDamageable() || meta == itemStack.getMetadata()) && tag == itemStack.getTagCompound();
        }).findFirst().orElse(null);
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
     * Get the catalyst of the recipe
     *
     * @return The catalyst of the recipe
     */
    @Nullable
    public GsCatalyst getCatalyst() {
        return catalyst;
    }

    @Nullable
    public ArrayList<GsSound> getSounds() {
        return sounds;
    }

    @Nullable
    public ArrayList<GsParticle> getParticles() {
        return particles;
    }

    @Nullable
    public GsEnum.OutputType getOutputType() {
        return outputType;
    }

    @Nullable
    public GsEnum.QueueType getQueueable() {
        return queueable;
    }

    @Nullable
    public GsEnum.SoundType getSoundType() {
        return soundType;
    }

    @Nullable
    public ResourceLocation getFrame() {
        return frame;
    }

    @Nullable
    public ResourceLocation getProgressBar() {
        return progressBar;
    }

    /**
     * Check if a tool is valid for the recipe, it checks the item, metadata and tag are the same as well as
     * if the durability ({@link #setDurability(int)}) is less than the remaining durability of the tool.
     * If the tool is not a damageable item, it also checks the stack size. (The +1 is to account that tool durability
     * is 0 based)
     *
     * @param itemStack The tool to check
     * @return If the tool is valid for the recipe
     */
    public boolean isToolValid(ItemStack itemStack) {
        return tool.stream().map(GsTool::getItemStack).anyMatch(tool -> {
            Item item = tool.getItem();
            int meta = tool.getMetadata();
            int stackSize = tool.getCount();
            NBTTagCompound tag = tool.getTagCompound();
            if (itemStack.isItemStackDamageable()) {
                int remainingDurability = itemStack.getMaxDamage() - itemStack.getItemDamage();
                return item == itemStack.getItem()
                        && getDurability() <= remainingDurability + 1
                        && stackSize == 1
                        && tag == itemStack.getTagCompound();
            } else {
                return item == itemStack.getItem()
                        && meta == itemStack.getMetadata()
                        && stackSize <= itemStack.getCount()
                        && tag == itemStack.getTagCompound();
            }
        });
    }

    /**
     * Check if an input is valid for the recipe, it checks the item, metadata and tag a re the same as well as if the
     * stack size is more than the size of the input stack.
     *
     * @param itemStack The input to check
     * @return If the input is valid for the recipe
     */
    public boolean isInputValid(ItemStack itemStack) {
        return input.stream().map(Ingredient::getMatchingStacks).flatMap(Arrays::stream).anyMatch(input -> {
            Item item = input.getItem();
            int meta = input.getMetadata();
            NBTTagCompound tag = input.getTagCompound();
            return item == itemStack.getItem()
                    && meta == itemStack.getMetadata()
                    && input.getCount() <= itemStack.getCount()
                    && tag == itemStack.getTagCompound();
        });
    }

    /**
     * Check if a catalyst is valid for the recipe, it checks the item, metadata and tag are the same as well as
     * if the stack size is more than the size of the catalyst stack.
     *
     * @param player The player to check the catalyst for
     * @return If the catalyst is valid for the recipe
     */
    public boolean isCatalystValid(EntityPlayer player) {
        if (catalyst == null) return true;
        return player.inventory.mainInventory.stream().anyMatch(itemStack -> Arrays.stream(catalyst.getIngredient().getMatchingStacks()).anyMatch(ingredient -> {
            Item item = ingredient.getItem();
            int meta = ingredient.getMetadata();
            int stackSize = ingredient.getCount();
            NBTTagCompound tag = ingredient.getTagCompound();
            return item == itemStack.getItem()
                    && meta == itemStack.getMetadata()
                    && stackSize <= itemStack.getCount()
                    && tag == itemStack.getTagCompound();
        }));
    }
}
