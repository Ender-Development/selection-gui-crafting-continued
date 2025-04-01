package io.enderdev.selectionguicrafting.registry.util;

import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Sound {
    private final ResourceLocation sound;
    private final float volume;
    private final float pitch;

    public Sound(ResourceLocation sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Sound(ResourceLocation sound) {
        this(sound, 1.0f, 1.0f);
    }

    @NotNull
    public ResourceLocation getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }
}
