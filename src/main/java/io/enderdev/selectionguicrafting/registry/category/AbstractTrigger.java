package io.enderdev.selectionguicrafting.registry.category;

public abstract class AbstractTrigger {
    private final double damageMultiplier;
    private final double timeMultiplier;
    private final double xpMultiplier;

    public AbstractTrigger(double damageMultiplier, double timeMultiplier, double xpMultiplier) {
        this.damageMultiplier = damageMultiplier;
        this.timeMultiplier = timeMultiplier;
        this.xpMultiplier = xpMultiplier;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public double getTimeMultiplier() {
        return timeMultiplier;
    }

    public double getXpMultiplier() {
        return xpMultiplier;
    }
}
