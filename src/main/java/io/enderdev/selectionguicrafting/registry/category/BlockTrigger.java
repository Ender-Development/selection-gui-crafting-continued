package io.enderdev.selectionguicrafting.registry.category;

import net.minecraft.block.Block;

public class BlockTrigger extends AbstractTrigger {
    private final Block triggerBlock;

    public BlockTrigger(Block triggerBlock, double damageMultiplier, double timeMultiplier, double xpMultiplier) {
        super(damageMultiplier, timeMultiplier, xpMultiplier);
        this.triggerBlock = triggerBlock;
    }

    public BlockTrigger(Block triggerBlock) {
        this(triggerBlock, 1.0, 1.0, 1.0);
    }

    public Block getTriggerBlock() {
        return triggerBlock;
    }
}
