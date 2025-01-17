package io.enderdev.selectionguicrafting.recipe;

import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GSCategory {
    private final ArrayList<ResourceLocation> sounds = new ArrayList<>();

    private String id;
    private String displayName;
    private ResourceLocation background;
    private GSEnum.OutputType outputType;
    private GSEnum.QueueType queueable;

    public GSCategory() {
    }

    /**
     * Set the id of the category
     * @param id The id
     * @return The category
     */
    public GSCategory setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    /**
     * Set the display name of the category
     * @param displayName The display name
     * @return The category
     */
    public GSCategory setDisplayName(@NotNull String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Add a sound to the category
     * @param sound The sound to add
     * @return The category
     */
    public GSCategory addSound(@NotNull ResourceLocation sound) {
        sounds.add(sound);
        return this;
    }

    /**
     * Set the background of the category
     * @param background The background
     * @return The category
     */
    public GSCategory setBackground(@NotNull ResourceLocation background) {
        this.background = background;
        return this;
    }

    /**
     * Set the output type of the category
     * @param outputType The output type
     * @return The category
     */
    public GSCategory setOutputType(@NotNull GSEnum.OutputType outputType) {
        this.outputType = outputType;
        return this;
    }

    /**
     * Set the category to be queueable
     * @param queueable The queueable type
     * @return The category
     */
    public GSCategory setQueueable(@NotNull GSEnum.QueueType queueable) {
        this.queueable = queueable;
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
        return background == null ? new ResourceLocation("selectionguicrafting", "textures/gui/gui.png") : background;
    }

    @NotNull
    public ArrayList<ResourceLocation> getSounds() {
        if (sounds.isEmpty()) {
            sounds.add(new ResourceLocation("minecraft", "ui.button.click"));
        }
        return sounds;
    }

    @NotNull
    public GSEnum.OutputType getOutputType() {
        return outputType == null ? GSEnum.OutputType.DROP : outputType;
    }

    @NotNull
    public GSEnum.QueueType getQueueable() {
        return queueable == null ? GSEnum.QueueType.YES : queueable;
    }

}
