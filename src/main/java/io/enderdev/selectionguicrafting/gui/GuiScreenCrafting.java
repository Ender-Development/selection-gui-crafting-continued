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
import io.enderdev.selectionguicrafting.registry.category.AbstractTrigger;
import io.enderdev.selectionguicrafting.registry.category.Category;
import io.enderdev.selectionguicrafting.registry.category.SoundType;
import io.enderdev.selectionguicrafting.registry.recipe.Recipe;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeHelper;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeInput;
import io.enderdev.selectionguicrafting.registry.util.Particle;
import io.enderdev.selectionguicrafting.registry.util.Sound;
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

    private final Category recipeCategory;
    private double totalCraftingTime;
    private final EntityPlayer player;
    private final World world;

    private ItemStack toolTipToRenderItem = null;
    private int toolTipToRenderX = 32000;
    private int toolTipToRenderY = 32000;

    private final ArrayList<Integer> slotCoordinates = new ArrayList<>();
    private final ArrayList<Integer> queue = new ArrayList<>();
    private final ArrayList<Recipe> validRecipes;

    private double craftingProgress = 0.0;
    private int recipeSelectedIndex = -1;
    private int lineCoordX = 32000, lineCoordY = 32000;
    private int recipeTime = -1;
    private long timeQueueStart = -1;
    private long timeRecipeStart = -1;

    private int rows;
    private int cols;

    private int final_width_offset;

    private Recipe hoveredRecipe;
    private RecipeHelper hoveredRecipeHelper;
    private Recipe selectedRecipe;
    private RecipeHelper selectedRecipeHelper;
    private final Random random = new Random();

    private final double damageMultiplier;
    private final double timeMultiplier;
    private final double xpMultiplier;

    private boolean wrongInput = false;
    private boolean wrongAmount = false;
    private boolean wrongDurability = false;
    private boolean wrongCatalyst = false;
    private boolean correctAmount = false;
    private boolean noQueue = false;

    public GuiScreenCrafting(Category recipeCategory, AbstractTrigger trigger, EntityPlayer player, World world) {
        super();

        this.recipeCategory = recipeCategory;
        this.player = player;
        this.world = world;

        this.damageMultiplier = trigger.getDamageMultiplier();
        this.timeMultiplier = trigger.getTimeMultiplier();
        this.xpMultiplier = trigger.getXpMultiplier();

        this.validRecipes = Register.getRecipesByCategory(recipeCategory);
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
            selectedRecipeHelper = new RecipeHelper(selectedRecipe);
            recipeTime = selectedRecipe.getTime();
        }

        if (craftingProgress >= 1.0f) {
            SelectionPacketHandler.SELECTION_NETWORK_WRAPPER.sendToServer(new SelectionMessageProcessRecipe(validRecipes.get(0).getCategory(), recipeSelectedIndex, player.getName(), selectedRecipeHelper.getAbsoluteXP(xpMultiplier), damageMultiplier));

            List<Sound> sounds = selectedRecipeHelper.getSounds();
            SoundType soundType = selectedRecipeHelper.getSoundType();
            if (soundType == SoundType.RANDOM) {
                Sound sound = sounds.get(random.nextInt(sounds.size()));
                player.playSound(new SoundEvent(sound.getSound()), sound.getVolume(), sound.getPitch());
            } else {
                sounds.forEach(sound -> player.playSound(new SoundEvent(sound.getSound()), sound.getVolume(), sound.getPitch()));
            }

            List<Particle> particles = selectedRecipeHelper.getParticles();
            particles.forEach(particle -> {
                for (int i = 0; i < particle.getCount(); i++) {
                    double offsetX = random.nextGaussian();
                    double offsetY = random.nextGaussian();
                    double offsetZ = random.nextGaussian();
                    world.spawnParticle(particle.getType(), player.posX + offsetX, player.posY + offsetY, player.posZ + offsetZ, particle.getSpeed(), particle.getSpeed(), particle.getSpeed());
                }
            });

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
        ItemStack mainHand = player.getHeldItemMainhand();
        ItemStack offHand = player.getHeldItemOffhand();
        if (wrongInput) {
            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".wrong_input", hoveredRecipe.getInputs().get(0).getIngredient().getMatchingStacks()[0].getDisplayName()));
            wrongInput = false;
        }
//        if (wrongAmount) {
//            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".wrong_amount", hoveredRecipe.getInputStackSize(offHand), offHand.getDisplayName(), offHand.getCount()));
//            wrongAmount = false;
//        }
//        if (wrongCatalyst && hoveredRecipe.getCatalyst() != null) {
//            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".wrong_catalyst", hoveredRecipe.getCatalyst().getIngredient().getMatchingStacks()[0].getDisplayName()));
//            wrongCatalyst = false;
//        }
//        if (correctAmount) {
//            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".correct_amount", hoveredRecipe.getInputStackSize(offHand)));
//            correctAmount = false;
//        }
        if (noQueue) {
            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".no_queue"));
            noQueue = false;
        }
//        if (wrongDurability) {
//            if (player.getHeldItemMainhand().isItemStackDamageable()) {
//                textLines.add(I18n.format("gui." + Tags.MOD_ID + ".wrong_durability", mainHand.getDisplayName()));
//            } else {
//                textLines.add(I18n.format("gui." + Tags.MOD_ID + ".wrong_amount", Objects.requireNonNull(hoveredRecipe.getTool(mainHand)).getItemStack().getCount(), mainHand.getDisplayName(), mainHand.getCount()));
//            }
//            wrongDurability = false;
//        }
        super.drawHoveringText(textLines, x, y, font);
    }

    private void drawRecipes(int mouseX, int mouseY) {

        toolTipToRenderItem = null;
        toolTipToRenderX = 32000;
        toolTipToRenderY = 32000;

        slotCoordinates.clear();
        int i = 0;

        int OFFSET = (final_width_offset + 8 - (cols * ICON_DISTANCE)) / 2;

        for (Recipe recipe : this.validRecipes) {
            RecipeHelper recipeHelper = new RecipeHelper(recipe);

            int rowNumber = i / cols;

            int xPos = left + OFFSET + (i % cols) * ICON_DISTANCE;
            int yPos = top + 24 + (rowNumber * ICON_DISTANCE);

            slotCoordinates.add((i * 4), xPos);
            slotCoordinates.add((i * 4) + 1, yPos);
            slotCoordinates.add((i * 4) + 2, xPos + 16);
            slotCoordinates.add((i * 4) + 3, yPos + 16);

            ResourceLocation itemBackground = recipeHelper.getFrame();
            mc.getTextureManager().bindTexture(itemBackground);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            drawScaledCustomSizeModalRect(xPos - 8, yPos - 8, 0, 0, 32, 32, 32, 32, 32, 32);


            ItemStack recipeItem = recipe.getOutputs().get(0).getItemStack();
            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemIntoGUI(recipeItem, xPos, yPos);
            itemRender.renderItemOverlayIntoGUI(this.fontRenderer, recipeItem, xPos, yPos, null);

            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            GlStateManager.colorMask(true, true, true, false);
            mc.getTextureManager().bindTexture(Assets.ICON.get());
            GlStateManager.pushMatrix();
            boolean isHovered = isMouseOverSlot(mouseX, mouseY, xPos, xPos + 16, yPos, yPos + 16);
            boolean isQueueable = recipeHelper.getQueueable().isQueueable();
            int iconX = xPos + 8;
            int iconY = yPos + 8;
            if (!isQueueable && !queue.isEmpty()) {
                drawScaledCustomSizeModalRect(xPos, yPos, 0, 16, 16, 16, 16, 16, 32, 32);
                if (isHovered) {
                    noQueue = true;
                }
            } else if (recipe.getInputs().stream().map(RecipeInput::getIngredient).map(Ingredient::getMatchingStacks).noneMatch(itemStacks -> Arrays.stream(itemStacks).anyMatch(stack -> stack.isItemEqual(player.getHeldItemOffhand())))) {
                drawScaledCustomSizeModalRect(iconX, iconY, 0, 16, 16, 16, 8, 8, 32, 32); // Red X
                if (isHovered) {
                    wrongInput = true;
                }
//            } else if (!recipe.isToolValid(player.getHeldItemMainhand())) {
//                if (player.getHeldItemMainhand().isItemStackDamageable()) {
//                    drawScaledCustomSizeModalRect(iconX, iconY, 16, 16, 16, 16, 8, 8, 32, 32); // Anvil
//                } else {
//                    drawScaledCustomSizeModalRect(iconX, iconY, 0, 0, 16, 16, 8, 8, 32, 32); // Pouch
//                }
//                if (isHovered) {
//                    wrongDurability = true;
//                }
//            } else if (!recipe.isInputValid(player.getHeldItemOffhand())) {
//                drawScaledCustomSizeModalRect(iconX, iconY, 0, 0, 16, 16, 8, 8, 32, 32); // Pouch
//                if (isHovered) {
//                    wrongAmount = true;
//                }
//            } else if (!recipe.isCatalystValid(player)) {
//                drawScaledCustomSizeModalRect(iconX, iconY, 16, 0, 16, 16, 8, 8, 32, 32); // Magnifying glass
//                if (isHovered) {
//                    wrongCatalyst = true;
//                }
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

            if (queue.contains(i) && isQueueable) {
                int finalI = i;
                renderRecipeText(String.valueOf(queue.stream().filter(j -> j == finalI).count()), xPos + 6, yPos + 4, Color.green.getRGB());
            }

            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();

            RenderHelper.disableStandardItemLighting();

            if (selectedRecipe != null && i == recipeSelectedIndex) {
                lineCoordX = xPos - 1;
                lineCoordY = yPos + 14;

                craftingProgress = selectedRecipeHelper.canCraft(player) ? (Minecraft.getSystemTime() - timeRecipeStart) / (recipeTime / timeMultiplier * 50) : 1.0;

                double remainingCraftingTime = totalCraftingTime - (Minecraft.getSystemTime() - timeQueueStart);
                if (remainingCraftingTime < 0) {
                    remainingCraftingTime = 0;
                }

                String renderTime = String.format("%.1f", (remainingCraftingTime) / 1000);
                fontRenderer.drawStringWithShadow(I18n.format("gui.selectionguicrafting.crafting_time", renderTime), right - fontRenderer.getStringWidth(renderTime) - 10, top + 10, Color.white.getRGB());

                GlStateManager.pushMatrix();
                ResourceLocation progressBar = recipeHelper.getProgressBar();
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

            Recipe recipe = validRecipes.get(i / 4);
            RecipeHelper recipeHelper = new RecipeHelper(recipe);
            boolean isQueueable = recipeHelper.getQueueable().isQueueable();

            if (isMouseOverSlot(mouseX, mouseY, slotXmin, slotXmax, slotYmin, slotYmax)) {
                if (!isQueueable && !queue.isEmpty()) {
                    break;
                }
                if (recipeHelper.canCraft(player)) {
                    queue.add(i / 4);
                    timeQueueStart = timeQueueStart == -1 ? Minecraft.getSystemTime() : timeQueueStart;
                    totalCraftingTime += (recipe.getTime() / timeMultiplier) * 50;
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
        int font_width = fontRenderer.getStringWidth(recipeCategory.getName());
        int width2 = font_width % 16 == 0 ? font_width : font_width + (16 - font_width % 16);

        int guiBorder = 8;
        int guiHeight = 56 - (SelectionConfig.CLIENT.disableCloseGUIbutton ? CLOSE_BUTTON_HEIGHT + 4 : 0);
        int final_height = guiHeight + rows * ICON_DISTANCE;
        int final_height_offset = (final_height - guiBorder * 2) % 16 == 0 ? final_height : final_height + (final_height - guiBorder * 2) % 16;
        int final_width = Math.max(width1, width2 + 64);
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
        labelList.add(label = new GuiLabel(fontRenderer, 0, (width / 2) - (fontRenderer.getStringWidth(recipeCategory.getName()) / 2), top + 16, 0, 0, 0xffffffff));
        label.addLine(recipeCategory.getName());
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