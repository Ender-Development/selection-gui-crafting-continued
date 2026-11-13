/*
 * Copyright (c) 2023-2026 Ender-Development.org
 * SPDX-License-Identifier: MIT
 */

package org.ender_development.selectionguicrafting.registry.recipe;

import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import org.ender_development.selectionguicrafting.registry.category.OutputType;
import org.ender_development.selectionguicrafting.registry.category.QueueType;
import org.ender_development.selectionguicrafting.registry.category.SoundType;
import org.ender_development.selectionguicrafting.registry.util.Particle;
import org.ender_development.selectionguicrafting.registry.util.Sound;
import org.jetbrains.annotations.Nullable;

public class RecipeData {
    private final ArrayList<Sound> sounds = new ArrayList<>();
    private final ArrayList<Particle> particles = new ArrayList<>();
    // set by category, can be overridden by recipe
    private ResourceLocation frame;
    private ResourceLocation progressBar;
    private OutputType outputType;
    private QueueType queueable;
    private SoundType soundType;

    public RecipeData() {
    }

    @Nullable public ResourceLocation getFrame() {
        return frame;
    }

    public RecipeData setFrame(ResourceLocation frame) {
        this.frame = frame;
        return this;
    }

    @Nullable public ResourceLocation getProgressBar() {
        return progressBar;
    }

    public RecipeData setProgressBar(ResourceLocation progressBar) {
        this.progressBar = progressBar;
        return this;
    }

    @Nullable public OutputType getOutputType() {
        return outputType;
    }

    public RecipeData setOutputType(OutputType outputType) {
        this.outputType = outputType;
        return this;
    }

    @Nullable public QueueType getQueueable() {
        return queueable;
    }

    public RecipeData setQueueable(QueueType queueable) {
        this.queueable = queueable;
        return this;
    }

    @Nullable public SoundType getSoundType() {
        return soundType;
    }

    public RecipeData setSoundType(SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    public ArrayList<Sound> getSounds() {
        return sounds;
    }

    public RecipeData addSound(Sound sound) {
        this.sounds.add(sound);
        return this;
    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public RecipeData addParticle(Particle particle) {
        this.particles.add(particle);
        return this;
    }
}
