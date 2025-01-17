package io.enderdev.selectionguicrafting.recipe;

public class GSEnum {
    /**
     * How the output of the recipe should be handled
     */
    public enum OutputType {
        DROP,
        INVENTORY
    }

    /**
     * If the recipe can be queued
     */
    public enum QueueType {
        YES,
        NO;

        public boolean isQueueable() {
            return this == YES;
        }
    }
}
