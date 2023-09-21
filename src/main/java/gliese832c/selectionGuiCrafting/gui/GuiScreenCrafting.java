package gliese832c.selectionGuiCrafting.gui;

import gliese832c.SelectionGuiCrafting;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipe;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiScreenCrafting extends GuiScreenDynamic {
    private GuiButton buttonClose;
    private GuiLabel label;

    ///////////////////
    // Magic Numbers //
    ///////////////////

    // Gui Container
    private final int GUI_BASE_WIDTH = 12;
    private final int GUI_BASE_HEIGHT = 48;
    private final int ICON_DISTANCE = 24;
    private final int MAX_ITEMS_PER_ROW = 10;

    // Title
    private final int TITLE_VERTICAL_OFFSET = 12;

    // Close button position
    private final int CLOSE_BUTTON_WIDTH = 70;
    private final int CLOSE_BUTTON_HEIGHT = 20;
    private final int CLOSE_BUTTON_OFFSET = 4;

    private EntityPlayer player;

    private GuiSelectionRecipeCategory recipeCategory;

    public GuiScreenCrafting(GuiSelectionRecipeCategory recipeCategory) {
        super();

        this.recipeCategory = recipeCategory;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks); // Background
        drawRecipes(); // Nutrition bars
        super.drawLabels(mouseX, mouseY); // Labels/buttons
    }

    private void drawRecipes() {
        int i = 0;

        GuiSelectionRecipe[] recipes = recipeCategory.recipes;
        for (GuiSelectionRecipe recipe : recipes) {

            ItemStack recipeItem = recipe.outputs[0];

            int rowNumber = i / MAX_ITEMS_PER_ROW;

            int xPos = left + 10 + (i % MAX_ITEMS_PER_ROW) * ICON_DISTANCE;
            int yPos = top + 24 + (rowNumber * ICON_DISTANCE);

            ResourceLocation itemBackground = new ResourceLocation(SelectionGuiCrafting.MOD_ID, "gui/itembackground");
            GlStateManager.color(1F, 1F, 1F);
            TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(itemBackground.toString());
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            drawTexturedModalRect(xPos - 3, yPos - 3, sprite, 32, 32);

            itemRender.renderItemIntoGUI(recipeItem, xPos, yPos);

            i++;
        }
    }

    // Called when GUI is opened or resized
    @Override
    public void initGui() {
        String part1 = I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".title.select");
        String part2 = recipeCategory.displayName;
        String part3 = I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".title.recipe");
        String selectionguiTitle = part1 + " " + part2 + " " + part3;

        GuiSelectionRecipe[] recipes = recipeCategory.recipes;

        int width1 = recipes.length == 0 ? ICON_DISTANCE : recipes.length <= MAX_ITEMS_PER_ROW ? ((recipes.length % MAX_ITEMS_PER_ROW) * ICON_DISTANCE) : MAX_ITEMS_PER_ROW * ICON_DISTANCE;
        int width2 = ((fontRenderer.getStringWidth(selectionguiTitle)/ 4) * 4) + 8;

        // Update dynamic GUI size
        super.updateContainerSize(GUI_BASE_WIDTH + Math.max(width1, width2), GUI_BASE_HEIGHT + (((recipes.length / MAX_ITEMS_PER_ROW) + 1) * ICON_DISTANCE));

        // Add Close button
        buttonList.add(buttonClose = new GuiButton(
                0,
                (width / 2) - (CLOSE_BUTTON_WIDTH / 2),
                bottom - CLOSE_BUTTON_HEIGHT - CLOSE_BUTTON_OFFSET,
                CLOSE_BUTTON_WIDTH,
                CLOSE_BUTTON_HEIGHT,
                I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".close")
        ));

        // Draw labels
        redrawLabels();
    }

    // Called when needing to propagate the window with new information
    public void redrawLabels() {

        // Clear existing labels
        labelList.clear();

        // Draw title
        String part1 = I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".title.select");
        String part2 = recipeCategory.displayName;
        String part3 = I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".title.recipe");
        String selectionguiTitle = part1 + " " + part2 + " " + part3;

        labelList.add(label = new GuiLabel(fontRenderer, 0, (width / 2) - (fontRenderer.getStringWidth(selectionguiTitle) / 2), top + TITLE_VERTICAL_OFFSET, 0, 0, 0xffffffff));
        label.addLine(selectionguiTitle);

        // Nutrients names and values
        /* int i = 0;
        for (StatusTracker statusTracker : StatusTrackers.statusTrackers) {
            // Create labels for each nutrient type name
            labelList.add(label = new GuiLabel(fontRenderer, 0, left + LABEL_NAME_HORIZONTAL_OFFSET, top + LABEL_VERTICAL_OFFSET + (i * NUTRITION_DISTANCE), 0, 0, 0xffffffff));
            label.addLine(I18n.format(statusTracker.chatInfoMessage)); // Add name from localization file

            // Create percent value labels for each nutrient value
            labelList.add(label = new GuiLabel(fontRenderer, 0, left + LABEL_VALUE_HORIZONTAL_OFFSET + labelCharacterPadding, top + LABEL_VERTICAL_OFFSET + (i * NUTRITION_DISTANCE), 0, 0, 0xffffffff));

            String value = data != null ? getValueColorizedPercentage(data.getDouble(statusTracker.key)) : "Couldn't Fetch Value";
            label.labels.add(value);
            //label.addLine(getValueColorizedPercentage(data.getDouble(statusTracker.key)));
            //fontRenderer.drawString();
            i++;
        }*/
    }

    // Called when button/element is clicked
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == buttonClose) {
            // Close GUI
            mc.player.closeScreen();
            if (mc.currentScreen == null)
                mc.setIngameFocus();
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
            if (mc.currentScreen == null)
                mc.setIngameFocus();
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
}