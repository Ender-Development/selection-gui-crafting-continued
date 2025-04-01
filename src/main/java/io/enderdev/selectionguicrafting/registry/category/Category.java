package io.enderdev.selectionguicrafting.registry.category;

import io.enderdev.selectionguicrafting.SelectionGuiCrafting;
import io.enderdev.selectionguicrafting.registry.Register;
import io.enderdev.selectionguicrafting.registry.util.Validation;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Category {
    private final Validation ErrorCheck = new Validation();
    private final CategoryData ScreenData = new CategoryData();

    private final ArrayList<ItemTrigger> triggerItems = new ArrayList<>();
    private final ArrayList<BlockTrigger> triggerBlocks = new ArrayList<>();

    private String id;
    private String name;

    public Category() {
    }

    public Category id(String id) {
        if (Register.getCategories().stream().anyMatch(ctg -> ctg.getID().equals(id))) {
            ErrorCheck.error("ID already exists");
        }
        return this;
    }

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

    public void register() {
        if (!validate()) {
            return;
        }
        Register.addCategory(this);
    }

    public ArrayList<BlockTrigger> getTriggerBlocks() {
        return triggerBlocks;
    }

    public ArrayList<ItemTrigger> getTriggerItems() {
        return triggerItems;
    }

    public String getID() {
        return id;
    }

    private boolean validate() {
        if (this.getID() == null) {
            ErrorCheck.error("Category ID must be set.");
        }
        if (triggerBlocks.isEmpty() && triggerItems.isEmpty()) {
            ErrorCheck.error("Category has no trigger!");
        }
        if (!ErrorCheck.valid()) {
            SelectionGuiCrafting.LOGGER.warn("Invalid Category {}. Error: {}", this.getID(), ErrorCheck.msg());
            return false;
        }
        return true;
    }
}
