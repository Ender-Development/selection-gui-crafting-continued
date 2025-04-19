package io.enderdev.selectionguicrafting.registry.recipe;

import io.enderdev.selectionguicrafting.gui.Assets;
import io.enderdev.selectionguicrafting.registry.category.OutputType;
import io.enderdev.selectionguicrafting.registry.category.QueueType;
import io.enderdev.selectionguicrafting.registry.category.SoundType;
import io.enderdev.selectionguicrafting.registry.util.Particle;
import io.enderdev.selectionguicrafting.registry.util.Sound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

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
        // Default values
        this.frame = Assets.FRAME_DEFAULT.get();
        this.progressBar = Assets.BAR_DEFAULT.get();

        this.outputType = OutputType.INVENTORY;
        this.queueable = QueueType.YES;
        this.soundType = SoundType.RANDOM;
    }

    public ResourceLocation getFrame() {
        return frame;
    }

    public RecipeData setFrame(ResourceLocation frame) {
        this.frame = frame;
        return this;
    }

    public ResourceLocation getProgressBar() {
        return progressBar;
    }

    public RecipeData setProgressBar(ResourceLocation progressBar) {
        this.progressBar = progressBar;
        return this;
    }

    public OutputType getOutputType() {
        return outputType;
    }

    public RecipeData setOutputType(OutputType outputType) {
        this.outputType = outputType;
        return this;
    }

    public QueueType getQueueable() {
        return queueable;
    }

    public RecipeData setQueueable(QueueType queueable) {
        this.queueable = queueable;
        return this;
    }

    public SoundType getSoundType() {
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
