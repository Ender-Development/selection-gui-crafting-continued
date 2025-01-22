package io.enderdev.selectionguicrafting.gui;

import io.enderdev.selectionguicrafting.Tags;
import net.minecraft.util.ResourceLocation;

public enum Assets {

    LOGO("textures/logo.png"),
    ICON("textures/gui/icon.png"),
    BG_CLAY("textures/gui/background/clay.png"),
    BG_CRACK("textures/gui/background/crack.png"),
    BG_DEADLANDS("textures/gui/background/deadlands.png"),
    BG_DEFAULT("textures/gui/background/default.png"),
    BG_FOREST("textures/gui/background/forest.png"),
    BG_LAKE("textures/gui/background/lake.png"),
    BG_STONEBRICK("textures/gui/background/stonebrick.png"),
    BG_WOOD("textures/gui/background/wood.png"),
    BAR_DEFAULT("textures/gui/bar/default.png"),
    DECOR_CLAY("textures/gui/decor/clay.png"),
    DECOR_DEFAULT("textures/gui/decor/default.png"),
    DECOR_GOLD("textures/gui/decor/gold.png"),
    DECOR_PURPLE("textures/gui/decor/purple.png"),
    FRAME_CHEST("textures/gui/frame/chest.png"),
    FRAME_DEFAULT("textures/gui/frame/default.png"),
    FRAME_IRON("textures/gui/frame/iron.png"),
    JEI_LOCKED("textures/jei/locked.png"),
    JEI_SELECTION("textures/jei/selection.png");

    private final String path;

    Assets(String path) {
        this.path = path;
    }

    public ResourceLocation get() {
        return new ResourceLocation(Tags.MOD_ID, this.path);
    }
}
