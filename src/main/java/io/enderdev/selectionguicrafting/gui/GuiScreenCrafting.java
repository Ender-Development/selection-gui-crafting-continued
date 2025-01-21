/*
WARNING

COGNITOHAZARD

This class contains some of the worst code I have ever written.
*/

package io.enderdev.selectionguicrafting.gui;

import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.config.SelectionConfig;
import io.enderdev.selectionguicrafting.network.SelectionMessageProcessRecipe;
import io.enderdev.selectionguicrafting.network.SelectionPacketHandler;
import io.enderdev.selectionguicrafting.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GuiScreenCrafting extends GuiScreenDynamic {
    private GuiButton buttonClose;
    private GuiLabel label;

    public static final int ICON_DISTANCE = 24;

    // Close button position
    private final int CLOSE_BUTTON_WIDTH = 70;
    private final int CLOSE_BUTTON_HEIGHT = 20;
    private final int CLOSE_BUTTON_OFFSET = 12;

    private final GsCategory recipeCategory;
    private float timeMultiplier;
    private float totalCraftingTime;
    private final EntityPlayer player;

    private ItemStack toolTipToRenderItem = null;
    private int toolTipToRenderX = 32000;
    private int toolTipToRenderY = 32000;

    private final ArrayList<Integer> slotCoordinates = new ArrayList<>();
    private final ArrayList<Integer> queue = new ArrayList<>();
    private final ArrayList<GsRecipe> validRecipes;

    private float craftingProgress = 0.0f;
    private int recipeSelectedIndex = -1;
    private int lineCoordX = 32000, lineCoordY = 32000;
    private int recipeTime = -1;
    private long timeQueueStart = -1;
    private long timeRecipeStart = -1;

    private int rows;
    private int cols;

    private int final_width_offset;

    private GsRecipe hoveredRecipe;
    private GsRecipe selectedRecipe;
    private final Random random = new Random();

    private boolean wrongInput = false;
    private boolean wrongAmount = false;
    private boolean wrongDurability = false;
    private boolean correctAmount = false;

    public GuiScreenCrafting(GsCategory recipeCategory, EntityPlayer player, World world) {
        super();

        this.recipeCategory = recipeCategory;
        this.player = player;

        this.validRecipes = GsRegistry.getValidRecipes(recipeCategory, player.getHeldItemMainhand());
        calculateRowsCols();
    }

    private void calculateRowsCols() {
        int n = this.validRecipes.size();
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        double aspectRatio = scaledResolution.getScaledWidth_double() / scaledResolution.getScaledHeight_double();

        int rows = 1, cols = n; // Start with 1 row and all items in a single row
        double bestDiff = Double.MAX_VALUE;

        // Find rows and columns such that cols / rows is closest to 16:9
        for (int r = 1; r <= Math.sqrt((double) (n * 16) / 9); r++) {
            int c = (int) Math.ceil((double) n / r);
            double currentRatio = (double) c / r;
            double diff = Math.abs(currentRatio - aspectRatio);

            if (diff < bestDiff) {
                bestDiff = diff;
                rows = r;
                cols = c;
            }
        }
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        super.drawScreen(mouseX, mouseY, partialTicks); // Background

        drawRecipes(mouseX, mouseY); // Items

        super.drawLabels(mouseX, mouseY); // Labels/buttons

        if (toolTipToRenderItem != null) {
            this.renderToolTip(toolTipToRenderItem, toolTipToRenderX, toolTipToRenderY);
        }

        if (!queue.isEmpty() && recipeSelectedIndex == -1) {
            recipeSelectedIndex = queue.get(0);
            timeRecipeStart = Minecraft.getSystemTime();
            selectedRecipe = validRecipes.get(recipeSelectedIndex);
            GsTool tool = selectedRecipe.getTool(player.getHeldItemMainhand());
            timeMultiplier = tool != null ? tool.getTimeMultiplier() : 1.0f;
            recipeTime = selectedRecipe.getTime();
        }

        if (craftingProgress >= 1.0f) {
            SelectionPacketHandler.SELECTION_NETWORK_WRAPPER.sendToServer(new SelectionMessageProcessRecipe(validRecipes.get(0).getCategory(), recipeSelectedIndex, player.getName()));

            List<GsSound> sounds = selectedRecipe.getSounds() == null || selectedRecipe.getSounds().isEmpty() ? recipeCategory.getSounds() : selectedRecipe.getSounds();
            GsEnum.SoundType soundType = selectedRecipe.getSoundType() != null ? selectedRecipe.getSoundType() : recipeCategory.getSoundType();
            if (soundType == GsEnum.SoundType.RANDOM) {
                GsSound sound = sounds.get(random.nextInt(sounds.size()));
                player.playSound(new SoundEvent(sound.getSound()), sound.getVolume(), sound.getPitch());
            } else {
                sounds.forEach(sound -> player.playSound(new SoundEvent(sound.getSound()), sound.getVolume(), sound.getPitch()));
            }

            // Close GUI
            queue.remove(0);
            if (queue.isEmpty()) {
                timeQueueStart = -1;
                mc.player.closeScreen();
                if (mc.currentScreen == null) {
                    mc.setIngameFocus();
                }
            }

            selectedRecipe = null;
            craftingProgress = 0.0f;
            recipeSelectedIndex = -1;
            lineCoordX = 32000;
            lineCoordY = 32000;
        }
    }

    @Override
    public void drawHoveringText(@NotNull List<String> textLines, int x, int y, @NotNull FontRenderer font) {
        if (wrongInput) {
            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".wrong_input", hoveredRecipe.getInput().get(0).getMatchingStacks()[0].getDisplayName()));
            wrongInput = false;
        }
        if (wrongAmount) {
            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".wrong_amount", hoveredRecipe.getInputStackSize(player.getHeldItemOffhand()), player.getHeldItemOffhand().getCount()));
            wrongAmount = false;
        }
        if (correctAmount) {
            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".correct_amount", hoveredRecipe.getInputStackSize(player.getHeldItemOffhand())));
            correctAmount = false;
        }
        if (wrongDurability) {
            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".wrong_durability", player.getHeldItemMainhand().getDisplayName()));
            wrongDurability = false;
        }
        super.drawHoveringText(textLines, x, y, font);
    }

    private void drawRecipes(int mouseX, int mouseY) {

        toolTipToRenderItem = null;
        toolTipToRenderX = 32000;
        toolTipToRenderY = 32000;

        slotCoordinates.clear();
        int i = 0;

        int OFFSET = (final_width_offset + 8 - (cols * ICON_DISTANCE)) / 2;

        for (GsRecipe recipe : this.validRecipes) {

            int rowNumber = i / cols;

            int xPos = left + OFFSET + (i % cols) * ICON_DISTANCE;
            int yPos = top + 24 + (rowNumber * ICON_DISTANCE);

            slotCoordinates.add((i * 4), xPos);
            slotCoordinates.add((i * 4) + 1, yPos);
            slotCoordinates.add((i * 4) + 2, xPos + 16);
            slotCoordinates.add((i * 4) + 3, yPos + 16);

            ResourceLocation itemBackground = recipe.getFrame() == null ? GsRegistry.getCategory(recipe.getCategory()).getFrame() : recipe.getFrame();
            mc.getTextureManager().bindTexture(itemBackground);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            drawScaledCustomSizeModalRect(xPos - 8, yPos - 8, 0, 0, 32, 32, 32, 32, 32, 32);


            ItemStack recipeItem = recipe.getOutput().get(0).getItemStack();
            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemIntoGUI(recipeItem, xPos, yPos);
            itemRender.renderItemOverlayIntoGUI(this.fontRenderer, recipeItem, xPos, yPos, null);

            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            GlStateManager.colorMask(true, true, true, false);
            mc.getTextureManager().bindTexture(new ResourceLocation(Tags.MOD_ID, "textures/gui/icon.png"));
            GlStateManager.pushMatrix();
            boolean isHovered = isMouseOverSlot(mouseX, mouseY, xPos, xPos + 16, yPos, yPos + 16);
            if (recipe.getInput().stream().map(Ingredient::getMatchingStacks).noneMatch(itemStacks -> Arrays.stream(itemStacks).anyMatch(stack -> stack.isItemEqual(player.getHeldItemOffhand())))) {
                drawScaledCustomSizeModalRect(xPos, yPos + 8, 16, 0, 16, 16, 8, 8, 32, 32);
                drawScaledCustomSizeModalRect(xPos + 8, yPos + 8, 0, 16, 16, 16, 8, 8, 32, 32);
                if (isHovered) {
                    wrongInput = true;
                }
            } else if (!recipe.isToolValid(player.getHeldItemMainhand())) {
                drawScaledCustomSizeModalRect(xPos, yPos + 8, 16, 16, 16, 16, 8, 8, 32, 32);
                drawScaledCustomSizeModalRect(xPos + 8, yPos + 8, 0, 16, 16, 16, 8, 8, 32, 32);
                if (isHovered) {
                    wrongDurability = true;
                }
            } else if (!recipe.isInputValid(player.getHeldItemOffhand())) {
                drawScaledCustomSizeModalRect(xPos, yPos + 8, 0, 0, 16, 16, 8, 8, 32, 32);
                drawScaledCustomSizeModalRect(xPos + 8, yPos + 8, 0, 16, 16, 16, 8, 8, 32, 32);
                if (isHovered) {
                    wrongAmount = true;
                }
            } else if (isHovered) {
                drawGradientRect(xPos, yPos, xPos + 16, yPos + 16, -2130706433, -2130706433);
                correctAmount = true;
            }
            GlStateManager.popMatrix();

            if (isHovered) {
                hoveredRecipe = recipe;
                toolTipToRenderItem = recipeItem;
                toolTipToRenderX = mouseX;
                toolTipToRenderY = mouseY;
            }

            if (queue.contains(i)) {
                int finalI = i;
                renderRecipeText(String.valueOf(queue.stream().filter(j -> j == finalI).count()), xPos + 16, yPos, Color.green.getRGB());
            }

            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();

            RenderHelper.disableStandardItemLighting();

            if (selectedRecipe != null && i == recipeSelectedIndex) {
                lineCoordX = xPos - 1;
                lineCoordY = yPos + 14;

                craftingProgress = selectedRecipe.isInputValid(player.getHeldItemOffhand()) && selectedRecipe.isToolValid(player.getHeldItemMainhand()) ? (float) (Minecraft.getSystemTime() - timeRecipeStart) / (recipeTime / timeMultiplier * 50) : 1.0f;

                float remainingCraftingTime = totalCraftingTime - (Minecraft.getSystemTime() - timeQueueStart);
                if (remainingCraftingTime < 0) {
                    remainingCraftingTime = 0;
                }
                String renderTime = String.format("%.1f", (remainingCraftingTime) / 1000);
                fontRenderer.drawStringWithShadow(renderTime, right - fontRenderer.getStringWidth(renderTime) - 10, top + 10, Color.white.getRGB());

                GlStateManager.pushMatrix();
                ResourceLocation progressBar = recipe.getProgressBar() == null ? GsRegistry.getCategory(recipe.getCategory()).getProgressBar() : recipe.getProgressBar();
                mc.getTextureManager().bindTexture(progressBar);
                GlStateManager.scale(0.5, 0.5, 1);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableDepth();
                drawScaledCustomSizeModalRect(lineCoordX * 2, lineCoordY * 2, 0, 0, (int) (32 * craftingProgress), 4, (int) (32 * craftingProgress + 4), 4, 32, 32);
                drawScaledCustomSizeModalRect(lineCoordX * 2, lineCoordY * 2 - 1, 0, 4, 32, 6, 36, 6, 32, 32);
                GlStateManager.popMatrix();
            }
            i++;
        }
    }

    private void renderRecipeText(String text, int x, int y, int foreground) {
        float size = text.startsWith("§") ? fontRenderer.getStringWidth(text.substring(2)) : fontRenderer.getStringWidth(text);
        fontRenderer.drawStringWithShadow(text, x - size / 2, y, foreground);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int i = 0;
        while (i / 4 < slotCoordinates.size() / 4) {
            int slotXmin = slotCoordinates.get(i);
            int slotYmin = slotCoordinates.get(i + 1);
            int slotXmax = slotCoordinates.get(i + 2);
            int slotYmax = slotCoordinates.get(i + 3);
            if (isMouseOverSlot(mouseX, mouseY, slotXmin, slotXmax, slotYmin, slotYmax)) {
                if (validRecipes.get(i / 4).isInputValid(player.getHeldItemOffhand()) && validRecipes.get(i / 4).isToolValid(player.getHeldItemMainhand())) {
                    queue.add(i / 4);
                    timeQueueStart = timeQueueStart == -1 ? Minecraft.getSystemTime() : timeQueueStart;
                    totalCraftingTime += (validRecipes.get(i / 4).getTime() / Objects.requireNonNull(validRecipes.get(i / 4).getTool(player.getHeldItemMainhand())).getTimeMultiplier()) * 50;
                    break;
                }
            }
            i += 4;
        }
    }

    // Called when GUI is opened or resized
    @Override
    public void initGui() {
        int width1 = validRecipes.isEmpty() ? ICON_DISTANCE : ((cols * ICON_DISTANCE) + 16);
        int width2 = (int) (fontRenderer.getStringWidth(recipeCategory.getDisplayName()) * 1.5) + 16;

        int guiBorder = 8;
        int guiHeight = 56 - (SelectionConfig.CLIENT.disableCloseGUIbutton ? CLOSE_BUTTON_HEIGHT + 4 : 0);
        int final_height = guiHeight + rows * ICON_DISTANCE;
        int final_height_offset = (final_height - guiBorder * 2) % 16 == 0 ? final_height : final_height + (final_height - guiBorder * 2) % 16;
        int final_width = Math.max(width1, width2);
        final_width_offset = (final_width - guiBorder * 2) % 16 == 0 ? final_width : final_width + (final_width - guiBorder * 2) % 16;

        // Update dynamic GUI size
        super.updateContainerSize(final_width_offset, final_height_offset, recipeCategory);

        if (!SelectionConfig.CLIENT.disableCloseGUIbutton) {
            // Add Close button
            int buttonX = (width / 2) - (CLOSE_BUTTON_WIDTH / 2);
            int buttonY = bottom - CLOSE_BUTTON_HEIGHT - CLOSE_BUTTON_OFFSET;
            String buttonCloseText = I18n.format("gui." + Tags.MOD_ID + ".close");
            buttonList.add(buttonClose = new GuiButton(0, buttonX, buttonY, CLOSE_BUTTON_WIDTH, CLOSE_BUTTON_HEIGHT, buttonCloseText));
        }

        // Draw labels
        redrawLabels();
    }

    // Called when needing to propagate the window with new information
    public void redrawLabels() {

        // Clear existing labels
        labelList.clear();

        // Title
        labelList.add(label = new GuiLabel(fontRenderer, 0, (width / 2) - (fontRenderer.getStringWidth(recipeCategory.getDisplayName()) / 2), top + 16, 0, 0, 0xffffffff));
        label.addLine(recipeCategory.getDisplayName());
    }

    @Override
    protected void actionPerformed(@NotNull GuiButton button) {
        if (button == buttonClose) {
            mc.player.closeScreen();
            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        // If escape key (1), or player inventory key (E) is pressed
        if (keyCode == 1 || keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
            mc.player.closeScreen();
            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        redrawLabels();
    }

    private boolean isMouseOverSlot(int mouseX, int mouseY, int lowerX, int upperX, int lowerY, int upperY) {
        return mouseX >= lowerX && mouseX <= upperX && mouseY >= lowerY && mouseY <= upperY;
    }
}