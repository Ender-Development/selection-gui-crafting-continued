package io.enderdev.selectionguicrafting.gui;

import io.enderdev.selectionguicrafting.registry.GsCategory;
import io.enderdev.selectionguicrafting.registry.GsEnum;
import io.enderdev.selectionguicrafting.registry.GsRegistry;
import net.minecraft.client.Minecraft;
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

    // Stencil value
    private int stencilValue;

    // Must be increment of 16!
    void updateContainerSize(int newGuiWidth, int newGuiHeight, GsCategory category) {
        // Update container size
        guiWidth = newGuiWidth;
        guiHeight = newGuiHeight;
        backgroundTexture = category.getBackground();
        borderTexture = category.getBorder();
        decorationTexture = category.getDecoration();
        backgroundType = category.getBackgroundType();
        stencilValue = GsRegistry.getCategories().indexOf(category) + 1;

        // Calculate offsets
        top = (height / 2) - (guiHeight / 2);
        left = (width / 2) - (guiWidth / 2);
        right = (width / 2) + (guiWidth / 2);
        bottom = (height / 2) + (guiHeight / 2);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
        drawDynamicBackground();
        drawDynamicBorder();
        drawDynamicDecoration();
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

    private void drawDynamicBorder() {
        GlStateManager.pushMatrix();
        mc.getTextureManager().bindTexture(borderTexture);
        int textureWidth = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int textureHeight = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        int border = (textureHeight - 16) / 2;

        // Top left corner
        drawScaledCustomSizeModalRect(left, top, 0, 0, border, border, border, border, textureWidth, textureHeight);

        // Bottom left corner
        drawScaledCustomSizeModalRect(left, bottom - border, 0, 24, border, border, border, border, textureWidth, textureHeight);

        // Top right corner
        drawScaledCustomSizeModalRect(right - border, top, 24, 0, border, border, border, border, textureWidth, textureHeight);

        // Bottom right corner
        drawScaledCustomSizeModalRect(right - border, bottom - border, 24, 24, border, border, border, border, textureWidth, textureHeight);

        // Left side
        for (int i = 0; i < guiHeight - border * 2; i += 16)
            drawScaledCustomSizeModalRect(left, top + 8 + i, 0, 8, border, 16, border, 16, textureWidth, textureHeight);

        // Top side
        for (int i = 0; i < guiWidth - border * 2; i += 16)
            drawScaledCustomSizeModalRect(left + 8 + i, top, 8, 0, 16, border, 16, border, textureWidth, textureHeight);

        // Right side
        for (int i = 0; i < guiHeight - border * 2; i += 16)
            drawScaledCustomSizeModalRect(right - border, top + 8 + i, 24, 8, border, 16, border, 16, textureWidth, textureHeight);

        // Bottom side
        for (int i = 0; i < guiWidth - border * 2; i += 16)
            drawScaledCustomSizeModalRect(left + 8 + i, bottom - border, 8, 24, 16, border, 16, border, textureWidth, textureHeight);

        GlStateManager.popMatrix();
    }

    private void drawDynamicBackground() {
        mc.getTextureManager().bindTexture(backgroundTexture); // Fetch texture
        int textureWidth = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int textureHeight = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        int border = (textureHeight - 16) / 2;
        GlStateManager.pushMatrix();

        switch (backgroundType) {
            case TILE:
                for (int i = 0; i < guiWidth - border * 2; i += 16) {
                    for (int j = 0; j < guiHeight - border * 2; j += 16) {
                        drawScaledCustomSizeModalRect(left + border + i, top + border + j, border, border, 16, 16, 16, 16, textureWidth, textureHeight);
                    }
                }
                break;
            case SINGLE_STRETCH:
                drawScaledCustomSizeModalRect(left + 8, top + 8, 0, 0, textureWidth, textureHeight, guiWidth - 16, guiHeight - 16, textureWidth, textureHeight);
                break;
            case SINGLE_CUT:
                float textureAspectRatio = (float) textureWidth / textureHeight;
                float screenAspectRatio = (float) guiWidth / guiHeight;
                float finalWidth, finalHeight;

                int inner_top = (height / 2) - (guiHeight / 2) + 8;
                int inner_left = (width / 2) - (guiWidth / 2) + 8;
                int inner_right = (width / 2) + (guiWidth / 2) - 8;
                int inner_bottom = (height / 2) + (guiHeight / 2) - 8;

                if (textureAspectRatio > screenAspectRatio) {
                    // Fit height to screen, width will exceed
                    finalHeight = guiHeight - 16;
                    finalWidth = (guiHeight - 16) * textureAspectRatio;
                } else {
                    // Fit width to screen, height will exceed
                    finalWidth = guiWidth - 16;
                    finalHeight = (guiWidth - 16) / textureAspectRatio;
                }

                // I have no idea how any of the GL11 stuff works, but thanks to the RenderBook by tttsaurus for the stencil code
                // I thought I have to tweak the stencil code to make it work, but all that was needed was to tweak the QUADS vertices
                // Reference: https://github.com/tttsaurus/Mc122RenderBook/blob/main/articles/Stencil.md
                if (!Minecraft.getMinecraft().getFramebuffer().isStencilEnabled()) {
                    Minecraft.getMinecraft().getFramebuffer().enableStencil();
                }

                GlStateManager.disableTexture2D();
                GlStateManager.disableCull();

                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_STENCIL_TEST);

                GL11.glClearStencil(0);
                GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);

                GlStateManager.depthMask(false);
                GlStateManager.colorMask(false, false, false, false);
                GL11.glStencilFunc(GL11.GL_ALWAYS, stencilValue, 0xFF);
                GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);

                GL11.glStencilMask(0xFF);

                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2f(inner_left, inner_top);
                GL11.glVertex2f(inner_right, inner_top);
                GL11.glVertex2f(inner_right, inner_bottom);
                GL11.glVertex2f(inner_left, inner_bottom);
                GL11.glEnd();

                GL11.glStencilMask(0x00);

                GlStateManager.depthMask(true);
                GlStateManager.colorMask(true, true, true, true);

                GL11.glStencilFunc(GL11.GL_EQUAL, stencilValue, 0xFF);
                GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);

                GlStateManager.enableCull();
                GlStateManager.enableTexture2D();
                int posX = (int) (left + ((float) guiWidth / 2) - (finalWidth / 2));
                int posY = (int) (top + ((float) guiHeight / 2) - (finalHeight / 2));
                drawScaledCustomSizeModalRect(posX, posY, 0, 0, textureWidth, textureHeight, (int) finalWidth, (int) finalHeight, textureWidth, textureHeight);

                GL11.glDisable(GL11.GL_STENCIL_TEST);
                break;
        }
        GlStateManager.popMatrix();
    }

    private void drawDynamicDecoration() {
        mc.getTextureManager().bindTexture(decorationTexture);
        int textureWidth = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int textureHeight = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

        // Draw decoration
        int posX = left + (guiWidth / 2) - (textureWidth / 2);
        GlStateManager.pushMatrix();
        drawScaledCustomSizeModalRect(posX, top, 0, 0, textureWidth, textureHeight / 2, textureWidth, textureHeight / 2, textureWidth, textureHeight);
        drawScaledCustomSizeModalRect(posX, bottom - textureHeight / 2, 0, textureHeight / 2, textureWidth, textureHeight / 2, textureWidth, textureHeight / 2, textureWidth, textureHeight);
        GlStateManager.popMatrix();
    }
}
