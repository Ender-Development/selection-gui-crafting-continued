package io.enderdev.selectionguicrafting.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

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

    // Container info
    private ResourceLocation texture;

    // Update GUI size
    // Must be increment of 16!
    void updateContainerSize(int guiWidth, int guiHeight, ResourceLocation texture) {
        // Update container size
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.texture = texture;

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
        mc.getTextureManager().bindTexture(texture); // Fetch texture

        // Top left corner
        drawTexturedModalRect(left, top, 0, 0, 4, 4);

        // Bottom left corner
        drawTexturedModalRect(left, bottom - 4, 0, 20, 4, 4);

        // Top right corner
        drawTexturedModalRect(right - 4, top, 20, 0, 4, 4);

        // Bottom right corner
        drawTexturedModalRect(right - 4, bottom - 4, 20, 20, 4, 4);

        // Left side
        for (int i = 0; i < guiHeight - 8; i += 16)
            drawTexturedModalRect(left, top + 4 + i, 0, 4, 4, 16);

        // Top side
        for (int i = 0; i < guiWidth - 8; i += 16)
            drawTexturedModalRect(left + 4 + i, top, 4, 0, 16, 4);

        // Right side
        for (int i = 0; i < guiHeight - 8; i += 16)
            drawTexturedModalRect(right - 4, top + 4 + i, 20, 4, 4, 16);

        // Bottom side
        for (int i = 0; i < guiWidth - 8; i += 16)
            drawTexturedModalRect(left + 4 + i, bottom - 4, 4, 20, 16, 4);

        // Draw center tiles
        for (int i = 0; i < guiWidth - 8; i += 16)
            for (int j = 0; j < guiHeight - 8; j += 16)
                drawTexturedModalRect(left + 4 + i, top + 4 + j, 4, 4, 16, 16);
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
