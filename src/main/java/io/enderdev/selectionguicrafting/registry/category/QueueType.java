package io.enderdev.selectionguicrafting.registry.category;

public enum QueueType {
    YES,
    NO;

    public boolean isQueueable() {
        return this == YES;
    }
}
