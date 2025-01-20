package io.enderdev.selectionguicrafting.registry;

public class GsEnum {
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

    /**
     * How the background should be rendered
     */
    public enum BackgroundType {
        SINGLE,
        TILE
    }

    /**
     * How the sound should be played
     */
    public enum SoundType {
        RANDOM,
        COMBINED
    }
}
