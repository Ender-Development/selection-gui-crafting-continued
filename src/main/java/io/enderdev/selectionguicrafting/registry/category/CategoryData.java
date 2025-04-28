package io.enderdev.selectionguicrafting.registry.category;

import io.enderdev.selectionguicrafting.gui.Assets;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeData;
import net.minecraft.util.ResourceLocation;

public class CategoryData extends RecipeData {
    // set by category, can't be overridden by recipe
    private ResourceLocation background;
    private ResourceLocation border;
    private ResourceLocation decoration;
    private BackgroundType backgroundType;

    public CategoryData() {
        // get default values from RecipeData
        super();
        // Default values
        this.background = Assets.BG_DEFAULT.get();
        this.border = Assets.BG_DEFAULT.get();
        this.decoration = Assets.DECOR_DEFAULT.get();

        this.setProgressBar(Assets.BAR_DEFAULT.get());
        this.setFrame(Assets.FRAME_DEFAULT.get());

        this.setQueueable(QueueType.YES);
        this.setOutputType(OutputType.INVENTORY);
        this.setSoundType(SoundType.RANDOM);

        this.backgroundType = BackgroundType.TILE;
    }

    public ResourceLocation getBackground() {
        return background;
    }

    public CategoryData setBackground(ResourceLocation background) {
        this.background = background;
        return this;
    }

    public ResourceLocation getBorder() {
        return border;
    }

    public CategoryData setBorder(ResourceLocation border) {
        this.border = border;
        return this;
    }

    public ResourceLocation getDecoration() {
        return decoration;
    }

    public CategoryData setDecoration(ResourceLocation decoration) {
        this.decoration = decoration;
        return this;
    }

    public BackgroundType getBackgroundType() {
        return backgroundType;
    }

    public CategoryData setBackgroundType(BackgroundType backgroundType) {
        this.backgroundType = backgroundType;
        return this;
    }
}
