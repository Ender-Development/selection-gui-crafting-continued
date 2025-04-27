package io.enderdev.selectionguicrafting.registry.util;

import net.minecraft.util.EnumParticleTypes;

public class Particle {
    private final EnumParticleTypes particleType;
    private final Integer particleCount;
    private final Float particleSpeed;

    public Particle(EnumParticleTypes particleType, Integer particleCount, Float particleSpeed) {
        this.particleType = particleType;
        this.particleCount = particleCount;
        this.particleSpeed = particleSpeed;
    }

    public Particle(EnumParticleTypes particleType) {
        this.particleType = particleType;
        this.particleCount = 10;
        this.particleSpeed = 0.1F;
    }

    public EnumParticleTypes getType() {
        return particleType == null ? EnumParticleTypes.VILLAGER_HAPPY : particleType;
    }

    public Integer getCount() {
        return particleCount == null ? 10 : particleCount;
    }

    public Float getSpeed() {
        return particleSpeed == null ? 0.1F : particleSpeed;
    }
}
