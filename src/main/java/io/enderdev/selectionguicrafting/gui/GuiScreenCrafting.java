/*
WARNING

COGNITOHAZARD

This class contains some of the worst code I have ever written.
*/

package io.enderdev.selectionguicrafting.gui;

import io.enderdev.selectionguicrafting.Tags;
import io.enderdev.selectionguicrafting.network.SelectionMessageProcessRecipe;
import io.enderdev.selectionguicrafting.network.SelectionPacketHandler;
import io.enderdev.selectionguicrafting.registry.GsRecipe;
import io.enderdev.selectionguicrafting.registry.GsCategory;
import io.enderdev.selectionguicrafting.registry.GsRegistry;
import io.enderdev.selectionguicrafting.registry.GsTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static io.enderdev.selectionguicrafting.config.SelectionConfig.disableCloseGUIbutton;

public class GuiScreenCrafting extends GuiScreenDynamic {
    private GuiButton buttonClose;
    private GuiLabel label;
    private final ResourceLocation guiBackground;

    public static final int ICON_DISTANCE = 24;

    // Close button position
    private final int CLOSE_BUTTON_WIDTH = 70;
    private final int CLOSE_BUTTON_HEIGHT = 20;
    private final int CLOSE_BUTTON_OFFSET = 6;

    private final String recipeCategory;
    private final float timeMultiplier;
    private final EntityPlayer player;
    private World world;

    private ItemStack toolTipToRenderItem = null;
    private int toolTipToRenderX = 32000;
    private int toolTipToRenderY = 32000;

    private final ArrayList<Integer> slotCoordinates = new ArrayList<>();
    private final ArrayList<GsRecipe> validRecipes;

    private float craftingProgress = 0.0f;
    private boolean recipeSelected = false;
    private int recipeSelectedIndex = -1;
    private int lineCoordX = 32000, lineCoordY = 32000, lineHeight = 32000;
    private int recipeTime = -1;
    private long startTime = -1;

    private int rows;
    private int cols;

    private int final_width_offset;

    private GsRecipe hoveredRecipe;

    private boolean wrongInput = false;
    private boolean wrongAmount = false;

    public GuiScreenCrafting(GsCategory recipeCategory, float timeMultiplier, EntityPlayer player, World world) {
        super();

        this.recipeCategory = recipeCategory.getDisplayName();
        this.guiBackground = recipeCategory.getBackground();
        this.timeMultiplier = timeMultiplier;
        this.player = player;
        this.world = world;

        this.validRecipes = GsRegistry.getRecipesForCategory(recipeCategory).stream().filter(recipe -> recipe.getTools().stream().map(GsTool::getItemStack).anyMatch(itemStack -> itemStack.isItemEqualIgnoreDurability(player.getHeldItemMainhand()))).collect(Collectors.toCollection(ArrayList::new));
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

        if (craftingProgress >= 1.0f) {

            //SelectionPacketHandler.SELECTION_NETWORK_WRAPPER.sendToServer(new SelectionMessageGiveItem(recipeCategory.recipes[recipeSelectedIndex].outputs, player.getName()));

            SelectionPacketHandler.SELECTION_NETWORK_WRAPPER.sendToServer(new SelectionMessageProcessRecipe(validRecipes.get(0).getCategory(), recipeSelectedIndex, player.getName()));

            craftingProgress = 0.0f;
            recipeSelected = false;
            recipeSelectedIndex = -1;
            lineCoordX = 32000;
            lineCoordY = 32000;
            lineHeight = 32000;
            recipeTime = -1;
            startTime = -1;

            // Close GUI
            mc.player.closeScreen();
            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
        }
    }

    @Override
    public void drawHoveringText(@NotNull List<String> textLines, int x, int y, @NotNull FontRenderer font) {
        if (wrongInput) {
            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".wrong_input", hoveredRecipe.getInputs().get(0).getMatchingStacks()[0].getDisplayName()));
            wrongInput = false;
        }
        if (wrongAmount) {
            textLines.add(I18n.format("gui." + Tags.MOD_ID + ".wrong_amount", hoveredRecipe.getAmount(), player.getHeldItemOffhand().getCount()));
            wrongAmount = false;
        }
        super.drawHoveringText(textLines, x, y, font);
    }


    private void giveItemToPlayer(ItemStack itemstack) {
        boolean flag = player.inventory.addItemStackToInventory(itemstack);

        if (flag) {
            player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryContainer.detectAndSendChanges();
        }

        if (flag && itemstack.isEmpty()) {
            itemstack.setCount(1);
            EntityItem entityitem1 = player.dropItem(itemstack, false);

            if (entityitem1 != null) {
                entityitem1.makeFakeItem();
            }
        } else {
            EntityItem entityitem = player.dropItem(itemstack, false);

            if (entityitem != null) {
                entityitem.setNoPickupDelay();
                entityitem.setOwner(player.getName());
            }
        }
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

            ResourceLocation itemBackground = new ResourceLocation(Tags.MOD_ID, "gui/itembackground");
            GlStateManager.color(1F, 1F, 1F);
            TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(itemBackground.toString());
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            drawTexturedModalRect(xPos - 3, yPos - 3, sprite, 32, 32);


            ItemStack recipeItem = recipe.getOutputs().get(0).getItemStack();
            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemIntoGUI(recipeItem, xPos, yPos);
            itemRender.renderItemOverlayIntoGUI(this.fontRenderer, recipeItem, xPos, yPos, null);

            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            GlStateManager.colorMask(true, true, true, false);
            boolean isHovered = (mouseX >= xPos) && (mouseX <= xPos + 16) && (mouseY >= yPos) && (mouseY <= yPos + 16);
            if (recipe.getInputs().stream().map(Ingredient::getMatchingStacks).noneMatch(itemStacks -> Arrays.stream(itemStacks).anyMatch(stack -> stack.isItemEqual(player.getHeldItemOffhand())))) {
                GlStateManager.scale(0.5, 0.5, 1);
                itemRender.renderItemIntoGUI(new ItemStack(Item.getItemFromBlock(Blocks.BARRIER)), xPos * 2, yPos * 2);
                itemRender.renderItemIntoGUI(new ItemStack(Item.getItemFromBlock(Blocks.CRAFTING_TABLE)), xPos * 2, yPos * 2 + 16);
                GlStateManager.scale(2, 2, 1);
                if (isHovered) {
                    wrongInput = true;
                }

            } else if (player.getHeldItemOffhand().getCount() < recipe.getAmount()) {
                GlStateManager.scale(0.5, 0.5, 1);
                itemRender.renderItemIntoGUI(new ItemStack(Item.getItemFromBlock(Blocks.BARRIER)), xPos * 2, yPos * 2);
                itemRender.renderItemIntoGUI(new ItemStack(Items.CHEST_MINECART), xPos * 2, yPos * 2 + 16);
                GlStateManager.scale(2, 2, 1);
                if (isHovered) {
                    wrongAmount = true;
                }
            } else if (isHovered) {
                // Highlight item if the mouse is over
                this.drawGradientRect(xPos, yPos, xPos + 16, yPos + 16, -2130706433, -2130706433);
            }

            if (isHovered) {
                hoveredRecipe = recipe;
                toolTipToRenderItem = recipeItem;
                toolTipToRenderX = mouseX;
                toolTipToRenderY = mouseY;
            }

            if (!(player.getHeldItemOffhand().getCount() < recipe.getAmount())) {
                if (recipe.getAmount() > 1) {
                    renderRecipeText(String.valueOf(recipe.getAmount()), xPos, yPos, Color.BLACK.getRGB(), Color.green.getRGB());
                }
            }

            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();

            RenderHelper.disableStandardItemLighting();

            if (recipeSelected && i == recipeSelectedIndex) {
                lineCoordX = xPos - 1;
                lineCoordY = yPos + 14;
                lineHeight = yPos + 17;

                recipeTime = recipe.getTime();


                craftingProgress = (float) (Minecraft.getSystemTime() - startTime) / ((recipeTime / timeMultiplier) * 50f);

                int recipeProgress = (int) (18 * craftingProgress);

                Color color = new Color(0, Math.min(craftingProgress * 0.8f + 0.2f, 1.0f), 0);
                drawGradientRectHorizontal(lineCoordX, lineCoordY, lineCoordX + recipeProgress, lineHeight, Color.BLACK.getRGB(), /*endColorG*/ color.getRGB());
            }

            i++;
        }
    }

    private void renderRecipeText(String text, int x, int y, int background, int foreground) {
        int size = text.startsWith("§") ? fontRenderer.getStringWidth(text.substring(2)) / 2 : fontRenderer.getStringWidth(text) / 2;
        fontRenderer.drawStringWithShadow(text, x - size, y, foreground);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int i = 0;
        if (!recipeSelected) {
            while (i / 4 < slotCoordinates.size() / 4) {
                int slotXmin = slotCoordinates.get(i);
                int slotYmin = slotCoordinates.get(i + 1);
                int slotXmax = slotCoordinates.get(i + 2);
                int slotYmax = slotCoordinates.get(i + 3);
                if ((mouseX >= slotXmin) && (mouseX <= slotXmax) && (mouseY >= slotYmin) && (mouseY <= slotYmax)) {
                    if (player.getHeldItemOffhand().getCount() >= validRecipes.get(i / 4).getAmount()) {
                        recipeSelected = true;
                        recipeSelectedIndex = i / 4;
                        startTime = Minecraft.getSystemTime();

                        System.out.println("Selected recipe: " + validRecipes.get(i / 4).getOutputs().get(0).getItemStack().getDisplayName());
                        System.out.println("Start time: " + Minecraft.getSystemTime());
                        break;
                    }
                }
                i += 4;
            }
        }
    }

    // Called when GUI is opened or resized
    @Override
    public void initGui() {
        String part1 = I18n.format("gui." + Tags.MOD_ID + ".title.select");
        String part3 = I18n.format("gui." + Tags.MOD_ID + ".title.recipe");
        String selectionguiTitle = part1 + " " + recipeCategory + " " + part3;

        int width1 = validRecipes.isEmpty() ? ICON_DISTANCE : ((cols * ICON_DISTANCE) + 16);
        int width2 = ((fontRenderer.getStringWidth(selectionguiTitle) / 16) * 16) + 16;

        int guiHeight = 56 - (disableCloseGUIbutton ? CLOSE_BUTTON_HEIGHT : 0);
        int final_height = guiHeight + rows * ICON_DISTANCE;
        int final_height_offset = (final_height - 8) % 16 == 0 ? final_height : final_height + (final_height - 8) % 16;
        int final_width = Math.max(width1, width2);
        final_width_offset = (final_width - 8) % 16 == 0 ? final_width : final_width + (final_width - 8) % 16;

        // Update dynamic GUI size
        super.updateContainerSize(final_width_offset, final_height_offset, guiBackground);

        if (!disableCloseGUIbutton) {
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

        // Draw title
        String part1 = I18n.format("gui." + Tags.MOD_ID + ".title.select");
        String part3 = I18n.format("gui." + Tags.MOD_ID + ".title.recipe");
        String selectionguiTitle = part1 + " " + recipeCategory + " " + part3;

        // Title
        labelList.add(label = new GuiLabel(fontRenderer, 0, (width / 2) - (fontRenderer.getStringWidth(selectionguiTitle) / 2), top + 12, 0, 0, 0xffffffff));
        label.addLine(selectionguiTitle);
    }

    // Called when button/element is clicked
    @Override
    protected void actionPerformed(@NotNull GuiButton button) {
        if (button == buttonClose) {
            // Close GUI
            mc.player.closeScreen();
            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
        }
    }

    // Close GUI if inventory key is hit again
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        // If escape key (1), or player inventory key (E) is pressed
        if (keyCode == 1 || keyCode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
            // Close GUI
            mc.player.closeScreen();
            if (mc.currentScreen == null) {
                mc.setIngameFocus();
            }
        }
    }

    // Opening Nutrition menu doesn't pause game
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


    @Override
    public void updateScreen() {
        redrawLabels();
    }


    private void drawGradientRectHorizontal(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) right, (double) top, (double) this.zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) left, (double) top, (double) this.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) bottom, (double) this.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, (double) this.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }


    // Get Key by value
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}