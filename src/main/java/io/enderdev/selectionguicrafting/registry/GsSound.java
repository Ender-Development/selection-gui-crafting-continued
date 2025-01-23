package io.enderdev.selectionguicrafting.registry;

import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GsSound {
    private final ResourceLocation sound;
    private final float volume;
    private final float pitch;

    public GsSound(ResourceLocation sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @NotNull
    public ResourceLocation getSound() {
        return sound;
    }

    public float getVolume() {
        return volume == 0 ? 1 : volume;
    }

    public float getPitch() {
        return pitch == 0 ? 1 : pitch;
    }
}
