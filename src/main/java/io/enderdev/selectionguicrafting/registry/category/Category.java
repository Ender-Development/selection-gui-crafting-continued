package io.enderdev.selectionguicrafting.registry.category;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.registry.IRegisterObject;
import io.enderdev.selectionguicrafting.registry.Register;
import io.enderdev.selectionguicrafting.registry.util.Particle;
import io.enderdev.selectionguicrafting.registry.util.Sound;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Category implements IRegisterObject {
    private final CategoryData ScreenData = new CategoryData();

    private final ArrayList<ItemTrigger> triggerItems = new ArrayList<>();
    private final ArrayList<BlockTrigger> triggerBlocks = new ArrayList<>();

    private String id;

    public Category() {
    }

    /* --------------------- */
    /* ------ ID & NAME ---- */
    /* --------------------- */

    public Category id(String id) {
        if (Register.getCategories().stream().anyMatch(ctg -> ctg.getID().equals(id))) {
            ErrorCheck.error("ID already exists");
        } else {
            this.id = id;
        }
        return this;
    }

    public String getID() {
        return id;
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return I18n.format(Tags.MOD_ID + ".category." + id + ".name");
    }

    /* --------------------- */
    /* ------ TRIGGER ------ */
    /* --------------------- */

    public Category trigger(Ingredient input, double damageMultiplier, double timeMultiplier, double xpMultiplier) {
        ArrayList<ItemStack> inputItems = new ArrayList<>(Arrays.asList(input.getMatchingStacks()));
        if (Register.getTriggerItems().stream().map(ItemTrigger::getTriggerItem).anyMatch(inputItems::contains)) {
            ErrorCheck.error("Invalid Trigger Item " + inputItems.get(0).getDisplayName() + " is already registered as Trigger.");
        } else {
            triggerItems.addAll(inputItems.stream().map(item -> new ItemTrigger(item, damageMultiplier, timeMultiplier, xpMultiplier)).collect(Collectors.toList()));
        }
        return this;
    }

    public Category trigger(Block input, double damageMultiplier, double timeMultiplier, double xpMultiplier) {
        if (Register.getTriggerBlocks().stream().map(BlockTrigger::getTriggerBlock).anyMatch(input::equals)) {
            ErrorCheck.error("Invalid Trigger Block " + input.getLocalizedName() + " is already registered as Trigger.");
        } else {
            triggerBlocks.add(new BlockTrigger(input, damageMultiplier, timeMultiplier, xpMultiplier));
        }
        return this;
    }

    public ArrayList<BlockTrigger> getTriggerBlocks() {
        return triggerBlocks;
    }

    public ArrayList<ItemTrigger> getTriggerItems() {
        return triggerItems;
    }

    /* --------------------- */
    /* ---- VALIDATION ----- */
    /* --------------------- */

    public boolean validate() {
        if (id == null) {
            ErrorCheck.error("Category ID must be set.");
        }
        if (triggerBlocks.isEmpty() && triggerItems.isEmpty()) {
            ErrorCheck.error("Category has no trigger!");
        }
        if (!ErrorCheck.valid()) {
            SelectionGuiCrafting.LOGGER.warn("Invalid Category {}. Error: {}", id, ErrorCheck.msg());
            return false;
        }
        return true;
    }

    public Category register() {
        if (!validate()) {
            return null;
        }
        Register.addCategory(this);
        return this;
    }

    /* --------------------- */
    /* ---- SCREENDATA ----- */
    /* --------------------- */

    public CategoryData getScreenData() {
        return ScreenData;
    }

    public Category setBackground(ResourceLocation background) {
        ScreenData.setBackground(background);
        return this;
    }

    public Category setBorder(ResourceLocation border) {
        ScreenData.setBorder(border);
        return this;
    }

    public Category setDecoration(ResourceLocation decoration) {
        ScreenData.setDecoration(decoration);
        return this;
    }

    public Category setBackgroundType(BackgroundType backgroundType) {
        ScreenData.setBackgroundType(backgroundType);
        return this;
    }

    public Category setFrame(ResourceLocation frame) {
        ScreenData.setFrame(frame);
        return this;
    }

    public Category setProgressBar(ResourceLocation progressBar) {
        ScreenData.setProgressBar(progressBar);
        return this;
    }

    public Category setOutputType(OutputType outputType) {
        ScreenData.setOutputType(outputType);
        return this;
    }

    public Category setQueueable(QueueType queueable) {
        ScreenData.setQueueable(queueable);
        return this;
    }

    public Category setSoundType(SoundType soundType) {
        ScreenData.setSoundType(soundType);
        return this;
    }

    public Category addSound(Sound sound) {
        ScreenData.addSound(sound);
        return this;
    }

    public Category addParticle(Particle particle) {
        ScreenData.addParticle(particle);
        return this;
    }
}
