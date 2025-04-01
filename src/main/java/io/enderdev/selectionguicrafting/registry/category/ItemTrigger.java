package io.enderdev.selectionguicrafting.registry.category;

import net.minecraft.item.ItemStack;

public class ItemTrigger extends AbstractTrigger {
    private final ItemStack triggerItem;

    public ItemTrigger(ItemStack triggerItem, double damageMultiplier, double timeMultiplier, double xpMultiplier) {
        super(damageMultiplier, timeMultiplier, xpMultiplier);
        this.triggerItem = triggerItem;
    }

    public ItemTrigger(ItemStack triggerItem) {
        this(triggerItem, 1.0, 1.0, 1.0);
    }

    public ItemStack getTriggerItem() {
        return triggerItem;
    }
}
