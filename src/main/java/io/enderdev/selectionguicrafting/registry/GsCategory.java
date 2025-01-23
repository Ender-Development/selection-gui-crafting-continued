package io.enderdev.selectionguicrafting.registry;

import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.gui.Assets;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@SuppressWarnings("UnusedReturnValue")
public class GsCategory {
    private final ArrayList<GsSound> sounds = new ArrayList<>();
    private final ArrayList<GsParticle> particles = new ArrayList<>();

    private String id;
    private String displayName;
    private ResourceLocation background;
    private ResourceLocation border;
    private ResourceLocation decoration;
    private ResourceLocation frame;
    private ResourceLocation progressBar;
    private GsEnum.BackgroundType backgroundType;
    private GsEnum.OutputType outputType;
    private GsEnum.QueueType queueable;
    private GsEnum.SoundType soundType;

    public GsCategory() {
    }

    /**
     * Set the id of the category
     * @param id The id
     * @return The category
     */
    public GsCategory setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    /**
     * Set the display name of the category
     * @param displayName The display name
     * @return The category
     */
    public GsCategory setDisplayName(@NotNull String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Add a sound to the category
     * @param sound The sound to add
     * @return The category
     */
    public GsCategory addSound(@NotNull ResourceLocation sound, float volume, float pitch) {
        sounds.add(new GsSound(sound, volume, pitch));
        return this;
    }

    /**
     * Add a particle to the category
     * @param particle The particle to add
     * @return The category
     */
    public GsCategory addParticle(@NotNull EnumParticleTypes particle, int count, float speed) {
        particles.add(new GsParticle(particle, count, speed));
        return this;
    }

    /**
     * Set the background of the category
     * @param background The background
     * @return The category
     */
    public GsCategory setBackground(@NotNull ResourceLocation background) {
        this.background = background;
        return this;
    }

    /**
     * Set the border of the category
     * @param border The border
     * @return The category
     */
    public GsCategory setBorder(@NotNull ResourceLocation border) {
        this.border = border;
        return this;
    }

    /**
     * Set the decoration of the category
     * @param decoration The decoration
     * @return The category
     */
    public GsCategory setDecoration(@NotNull ResourceLocation decoration) {
        this.decoration = decoration;
        return this;
    }

    /**
     * Set the frame of the category
     * @param frame The frame
     * @return The category
     */
    public GsCategory setFrame(@NotNull ResourceLocation frame) {
        this.frame = frame;
        return this;
    }

    /**
     * Set the progress bar of the category
     * @param progressBar The progress bar
     * @return The category
     */
    public GsCategory setProgressBar(@NotNull ResourceLocation progressBar) {
        this.progressBar = progressBar;
        return this;
    }

    /**
     * Set the output type of the category
     * @param outputType The output type
     * @return The category
     */
    public GsCategory setOutputType(@NotNull GsEnum.OutputType outputType) {
        this.outputType = outputType;
        return this;
    }

    /**
     * Set the category to be queueable
     * @param queueable The queueable type
     * @return The category
     */
    public GsCategory setQueueable(@NotNull GsEnum.QueueType queueable) {
        this.queueable = queueable;
        return this;
    }

    /**
     * Set the background type of the category
     * @param backgroundType The background type
     * @return The category
     */
    public GsCategory setBackgroundType(@NotNull GsEnum.BackgroundType backgroundType) {
        this.backgroundType = backgroundType;
        return this;
    }

    /**
     * Set the sound type of the category
     * @param soundType The sound type
     * @return The category
     */
    public GsCategory setSoundType(@NotNull GsEnum.SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    public ResourceLocation getBackground() {
        return background == null ? Assets.BG_DEFAULT.get() : background;
    }

    @NotNull
    public ResourceLocation getBorder() {
        return border == null ? Assets.BG_DEFAULT.get() : border;
    }

    @NotNull
    public ResourceLocation getDecoration() {
        return decoration == null ? Assets.DECOR_DEFAULT.get() : decoration;
    }

    @NotNull
    public ResourceLocation getFrame() {
        return frame == null ? Assets.FRAME_DEFAULT.get() : frame;
    }

    @NotNull
    public ResourceLocation getProgressBar() {
        return progressBar == null ? Assets.BAR_DEFAULT.get() : progressBar;
    }

    @NotNull
    public ArrayList<GsSound> getSounds() {
        if (sounds.isEmpty()) {
            sounds.add(new GsSound(new ResourceLocation("minecraft", "block.anvil.place"), 0.1f, 1));
        }
        return sounds;
    }

    @NotNull
    public ArrayList<GsParticle> getParticles() {
        if (particles.isEmpty()) {
            particles.add(new GsParticle(EnumParticleTypes.VILLAGER_HAPPY, 10, 0.5f));
        }
        return particles;
    }

    @NotNull
    public GsEnum.OutputType getOutputType() {
        return outputType == null ? GsEnum.OutputType.DROP : outputType;
    }

    @NotNull
    public GsEnum.QueueType getQueueable() {
        return queueable == null ? GsEnum.QueueType.YES : queueable;
    }

    @NotNull
    public GsEnum.BackgroundType getBackgroundType() {
        return backgroundType == null ? GsEnum.BackgroundType.TILE : backgroundType;
    }

    @NotNull
    public GsEnum.SoundType getSoundType() {
        return soundType == null ? GsEnum.SoundType.RANDOM : soundType;
    }

}
