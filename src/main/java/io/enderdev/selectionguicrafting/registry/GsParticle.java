package io.enderdev.selectionguicrafting.registry;

import net.minecraft.util.EnumParticleTypes;

@Deprecated
public class GsParticle {
    private final EnumParticleTypes particleType;
    private final Integer particleCount;
    private final Float particleSpeed;

    public GsParticle(EnumParticleTypes particleType, Integer particleCount, Float particleSpeed) {
        this.particleType = particleType;
        this.particleCount = particleCount;
        this.particleSpeed = particleSpeed;
    }

    public EnumParticleTypes getType() {
        return particleType;
    }

    public Integer getCount() {
        return particleCount;
    }

    public Float getSpeed() {
        return particleSpeed;
    }
}
