package io.enderdev.selectionguicrafting.gui;

import io.enderdev.selectionguicrafting.registry.GsCategory;
import io.enderdev.selectionguicrafting.registry.GsEnum;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public abstract class GuiScreenDynamic extends GuiScreen {

    // Container size
    private int guiWidth;
    private int guiHeight;

    // Offsets
    public int top;
    public int left;
    public int right;
    public int bottom;

    // Texture
    private ResourceLocation backgroundTexture;
    private ResourceLocation borderTexture;
    private ResourceLocation decorationTexture;
    private GsEnum.BackgroundType backgroundType;

    // Must be increment of 16!
    void updateContainerSize(int guiWidth, int guiHeight, GsCategory category) {
        // Update container size
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.backgroundTexture = category.getBackground();
        this.borderTexture = category.getBorder();
        this.decorationTexture = category.getDecoration();
        this.backgroundType = category.getBackgroundType();

        // Calculate offsets
        top = (height / 2) - (guiHeight / 2);
        left = (width / 2) - (guiWidth / 2);
        right = (width / 2) + (guiWidth / 2);
        bottom = (height / 2) + (guiHeight / 2);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground();
    }

    private void drawBackground() {
        // Init
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
        mc.getTextureManager().bindTexture(borderTexture); // Fetch texture
        int textureWidth  = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int textureHeight = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        int border = (textureHeight - 16) / 2;

        GlStateManager.pushMatrix();


        // Top left corner
        drawScaledCustomSizeModalRect(left, top, 0, 0, border, border, border, border, textureWidth, textureHeight);

        // Bottom left corner
        drawScaledCustomSizeModalRect(left, bottom - border, 0, 24, border, border, border, border, textureWidth, textureHeight);

        // Top right corner
        drawScaledCustomSizeModalRect(right - border, top, 24, 0, border, border, border, border, textureWidth, textureHeight);

        // Bottom right corner
        drawScaledCustomSizeModalRect(right - border, bottom - border, 24, 24, border, border, border, border, textureWidth, textureHeight);

        // Left side
        for (int i = 0; i < guiHeight - border*2; i += 16)
            drawScaledCustomSizeModalRect(left, top + 8 + i, 0, 8, border, 16, border, 16, textureWidth, textureHeight);

        // Top side
        for (int i = 0; i < guiWidth - border*2; i += 16)
            drawScaledCustomSizeModalRect(left + 8 + i, top, 8, 0, 16, border, 16, border, textureWidth, textureHeight);

        // Right side
        for (int i = 0; i < guiHeight - border*2; i += 16)
            drawScaledCustomSizeModalRect(right - border, top + 8 + i, 24, 8, border, 16, border, 16, textureWidth, textureHeight);

        // Bottom side
        for (int i = 0; i < guiWidth - border*2; i += 16)
            drawScaledCustomSizeModalRect(left + 8 + i, bottom - border, 8, 24, 16, border, 16, border, textureWidth, textureHeight);

        GlStateManager.popMatrix();
        mc.getTextureManager().bindTexture(backgroundTexture); // Fetch texture
        textureWidth  = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        textureHeight = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        border = (textureHeight - 16) / 2;
        GlStateManager.pushMatrix();

        if (backgroundType == GsEnum.BackgroundType.TILE) {
            // Draw center tiles
            for (int i = 0; i < guiWidth - border*2; i += 16) {
                for (int j = 0; j < guiHeight - border*2; j += 16) {
                    drawScaledCustomSizeModalRect(left + border + i, top + border + j, border, border, 16, 16, 16, 16, textureWidth, textureHeight);
                }
            }
        }
        if (backgroundType == GsEnum.BackgroundType.SINGLE_STRETCH) {
            drawScaledCustomSizeModalRect(left + 8, top + 8, 0, 0, textureWidth, textureHeight, guiWidth - 16, guiHeight - 16, textureWidth, textureHeight);
        }
        if (backgroundType == GsEnum.BackgroundType.SINGLE_CUT) {
            // TODO: Implement
        }
        GlStateManager.popMatrix();

        mc.getTextureManager().bindTexture(decorationTexture);
        textureWidth  = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        textureHeight = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

        // Draw decoration
        int posX = left + (guiWidth / 2) - (textureWidth / 2);
        GlStateManager.pushMatrix();
        drawScaledCustomSizeModalRect(posX, top, 0, 0, textureWidth, textureHeight/2, textureWidth, textureHeight/2, textureWidth, textureHeight);
        drawScaledCustomSizeModalRect(posX, bottom - textureHeight/2, 0, textureHeight/2, textureWidth, textureHeight/2, textureWidth, textureHeight/2, textureWidth, textureHeight);
        GlStateManager.popMatrix();
    }

    // Draw labels and buttons (replacing super.drawScreen() call)
    // Can be called after background drawing, for proper layering
    public void drawLabels(int mouseX, int mouseY) {
        // Labels
        for (GuiButton aButtonList : this.buttonList)
            aButtonList.drawButton(this.mc, mouseX, mouseY, 0);

        // Buttons
        for (GuiLabel aLabelList : this.labelList)
            aLabelList.drawLabel(this.mc, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void drawHoveringText(@NotNull List<String> textLines, int x, int y, @NotNull FontRenderer font) {
        super.drawHoveringText(textLines, x, y, font);
    }
}
