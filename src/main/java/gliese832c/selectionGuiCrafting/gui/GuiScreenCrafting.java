/*
WARNING

COGNITOHAZARD

This class contains some of the worst code I have ever written.
*/

package gliese832c.selectionGuiCrafting.gui;

import gliese832c.SelectionGuiCrafting;
import gliese832c.selectionGuiCrafting.network.SelectionMessageProcessRecipe;
import gliese832c.selectionGuiCrafting.network.SelectionPacketHandler;
import gliese832c.selectionGuiCrafting.proxy.CommonProxy;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipe;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static gliese832c.selectionGuiCrafting.config.SelectionConfig.disableCloseGUIbutton;

public class GuiScreenCrafting extends GuiScreenDynamic {
    private GuiButton buttonClose;
    private GuiLabel label;

    ///////////////////
    // Magic Numbers //
    ///////////////////

    // Gui Container
    private final int GUI_BASE_WIDTH = 12;
    private final int GUI_BASE_HEIGHT = 48;
    public static final int ICON_DISTANCE = 24;
    public static final int MAX_ITEMS_PER_ROW = 10;

    // Title
    private final int TITLE_VERTICAL_OFFSET = 12;

    // Close button position
    private final int CLOSE_BUTTON_WIDTH = 70;
    private final int CLOSE_BUTTON_HEIGHT = 20;
    private final int CLOSE_BUTTON_OFFSET = 4;

    private GuiSelectionRecipeCategory recipeCategory;
    private float timeMultiplier;
    private EntityPlayer player;
    private World world;

    private ItemStack toolTipToRenderItem = null;
    private int toolTipToRenderX = 32000;
    private int toolTipToRenderY = 32000;

    private ArrayList<Integer> slotCoordinates = new ArrayList<>();

    private float craftingProgress = 0.0f;
    private boolean recipeSelected = false;
    private int recipeSelectedIndex = -1;
    private int lineCoordX = 32000, lineCoordY = 32000, lineHeight = 32000;
    private int recipeTime = -1;
    private long startTime = -1;

    public GuiScreenCrafting(GuiSelectionRecipeCategory recipeCategory, float timeMultiplier, EntityPlayer player, World world) {
        super();

        this.recipeCategory = recipeCategory;
        this.timeMultiplier = timeMultiplier;
        this.player = player;
        this.world = world;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        super.drawScreen(mouseX, mouseY, partialTicks); // Background

        drawRecipes(mouseX, mouseY); // Items

        /*if (recipeSelected) {
            craftingProgress = (float) (Minecraft.getSystemTime() - startTime) / (recipeTime * 50f);

            int recipeProgress = (int) (18 * craftingProgress);
            int endColorG = ((int) (255 * craftingProgress)) << 16;

            drawGradientRect(lineCoordX, lineCoordY, lineCoordX + recipeProgress, lineHeight, 0x00000000, endColorG);
        }*/

        super.drawLabels(mouseX, mouseY); // Labels/buttons

        if (toolTipToRenderItem != null) {
            this.renderToolTip(toolTipToRenderItem, toolTipToRenderX, toolTipToRenderY);
        }

        if (craftingProgress >= 1.0f) {

            //SelectionPacketHandler.SELECTION_NETWORK_WRAPPER.sendToServer(new SelectionMessageGiveItem(recipeCategory.recipes[recipeSelectedIndex].outputs, player.getName()));

            SelectionPacketHandler.SELECTION_NETWORK_WRAPPER.sendToServer(new SelectionMessageProcessRecipe(Objects.requireNonNull(getKeyByValue(CommonProxy.recipeCategories, recipeCategory)), recipeSelectedIndex, player.getName()));

            /*for (ItemStack itemstack : recipeCategory.recipes[recipeSelectedIndex].outputs) {
                //giveItemToPlayer(itemstack.copy());

                if (!world.isRemote) { // Make sure you are not on the client side
                    MinecraftServer server = world.getMinecraftServer();

                    if (server != null) {
                        ICommandManager commandManager = server.getCommandManager();
                        int result = 0;

                        result = commandManager.executeCommand(server, "/give " + player.getName() + itemstack.getItem().getRegistryName() + " " + itemstack.getCount() + " " + itemstack.getMetadata() + " " + itemstack.getTagCompound().toString());

                        // 'result' will contain the return value of the command execution.
                        // 0 means success, while non-zero values indicate an error.
                        if (result != 0) {
                            // Handle the error, if needed.
                            // You can print a message or take other actions based on the result.
                            // For example:
                            System.out.println("Command execution failed with error code: " + result);
                        }
                    }
                }
            }
            player.getHeldItemOffhand().shrink(recipeCategory.recipes[recipeSelectedIndex].inputQuantity);*/

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
            if (mc.currentScreen == null)
                mc.setIngameFocus();
        }
    }



    private void giveItemToPlayer(ItemStack itemstack) {
        boolean flag = player.inventory.addItemStackToInventory(itemstack);

        if (flag)
        {
            player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryContainer.detectAndSendChanges();
        }

        if (flag && itemstack.isEmpty())
        {
            itemstack.setCount(1);
            EntityItem entityitem1 = player.dropItem(itemstack, false);

            if (entityitem1 != null)
            {
                entityitem1.makeFakeItem();
            }
        }
        else
        {
            EntityItem entityitem = player.dropItem(itemstack, false);

            if (entityitem != null)
            {
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
        ArrayList<GuiSelectionRecipe> recipes = recipeCategory.recipes;
        for (GuiSelectionRecipe recipe : recipes) {

            int rowNumber = i / MAX_ITEMS_PER_ROW;

            int xPos = left + 10 + (i % MAX_ITEMS_PER_ROW) * ICON_DISTANCE;
            int yPos = top + 24 + (rowNumber * ICON_DISTANCE);

            slotCoordinates.add((i*4) + 0, xPos + 0);
            slotCoordinates.add((i*4) + 1, yPos + 0);
            slotCoordinates.add((i*4) + 2, xPos + 16);
            slotCoordinates.add((i*4) + 3, yPos + 16);

            ResourceLocation itemBackground = new ResourceLocation(SelectionGuiCrafting.MOD_ID, "gui/itembackground");
            GlStateManager.color(1F, 1F, 1F);
            TextureAtlasSprite sprite = mc.getTextureMapBlocks().getAtlasSprite(itemBackground.toString());
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            drawTexturedModalRect(xPos - 3, yPos - 3, sprite, 32, 32);

            /*RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);*/

            ItemStack recipeItem = recipe.outputs[0];
            //itemRender.renderItemIntoGUI(recipeItem, xPos, yPos);
            itemRender.renderItemIntoGUI(recipeItem, xPos, yPos);
            itemRender.renderItemOverlayIntoGUI(this.fontRenderer, recipeItem, xPos, yPos, null);

            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            GlStateManager.colorMask(true, true, true, false);
            //if (!recipeSelected && player.getHeldItemOffhand().getCount() < recipe.inputQuantity) {
            if (player.getHeldItemOffhand().getCount() < recipe.inputQuantity) {
                assert Blocks.BARRIER != null;
                itemRender.renderItemIntoGUI(new ItemStack(Item.getItemFromBlock(Blocks.BARRIER)), xPos, yPos);
                fontRenderer.drawString("§l" + String.valueOf(recipe.inputQuantity), (xPos + 13) - (fontRenderer.getStringWidth(String.valueOf(recipe.inputQuantity)) / 2), yPos - 2, Color.BLACK.getRGB());
                fontRenderer.drawString("§l" + String.valueOf(recipe.inputQuantity), (xPos + 15) - (fontRenderer.getStringWidth(String.valueOf(recipe.inputQuantity)) / 2), yPos + 0, Color.BLACK.getRGB());
                fontRenderer.drawString("§l" + String.valueOf(recipe.inputQuantity), (xPos + 14) - (fontRenderer.getStringWidth(String.valueOf(recipe.inputQuantity)) / 2), yPos - 1, Color.RED.getRGB());
                //fontRenderer.drawString(">=" + recipe.inputQuantity, (xPos + 9) - (fontRenderer.getStringWidth(">=" + recipe.inputQuantity) / 2), yPos + 4, Color.green.getRGB());
                //fontRenderer.drawString(I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".needed"), (xPos + 9) - (fontRenderer.getStringWidth(I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".needed")) / 2), yPos + 10, Color.black.getRGB());
                //fontRenderer.drawString(">=" + recipe.inputQuantity, (xPos + 8) - (fontRenderer.getStringWidth(">=" + recipe.inputQuantity) / 2), yPos + 3, Color.white.getRGB());
                //fontRenderer.drawString(I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".needed"), (xPos + 8) - (fontRenderer.getStringWidth(I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".needed")) / 2), yPos + 9, Color.white.getRGB());
                //this.drawGradientRect(xPos, yPos, xPos + 16, yPos + 16, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            } else if ((mouseX >= xPos) && (mouseX <= xPos + 16) && (mouseY >= yPos) && (mouseY <= yPos + 16)) {
                // Highlight item if the mouse is over
                this.drawGradientRect(xPos, yPos, xPos + 16, yPos + 16, -2130706433, -2130706433);

                toolTipToRenderItem = recipeItem;
                toolTipToRenderX = mouseX;
                toolTipToRenderY = mouseY;
            }

            if (!(player.getHeldItemOffhand().getCount() < recipe.inputQuantity)) {
                if (recipe.inputQuantity > 1) {
                    fontRenderer.drawString("§l" + String.valueOf(recipe.inputQuantity), (xPos + 13) - (fontRenderer.getStringWidth(String.valueOf(recipe.inputQuantity)) / 2), yPos - 2, Color.BLACK.getRGB());
                    fontRenderer.drawString("§l" + String.valueOf(recipe.inputQuantity), (xPos + 15) - (fontRenderer.getStringWidth(String.valueOf(recipe.inputQuantity)) / 2), yPos + 0, Color.BLACK.getRGB());
                    fontRenderer.drawString(String.valueOf(recipe.inputQuantity), (xPos + 14) - (fontRenderer.getStringWidth(String.valueOf(recipe.inputQuantity)) / 2), yPos - 1, Color.green.getRGB());
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

                recipeTime = recipe.time;


                craftingProgress = (float) (Minecraft.getSystemTime() - startTime) / ((recipeTime / timeMultiplier) * 50f);

                int recipeProgress = (int) (18 * craftingProgress);

                Color color = new Color(0, Math.min(craftingProgress * 0.8f + 0.2f, 1.0f), 0);
                drawGradientRectHorizontal(lineCoordX, lineCoordY, lineCoordX + recipeProgress, lineHeight, Color.BLACK.getRGB(), /*endColorG*/ color.getRGB());
            }

            i++;
        }
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        //System.out.println("Mouse has been clicked!");

        int i = 0;
        if (!recipeSelected) {
            while (i / 4 < slotCoordinates.size() / 4) {
                if ((mouseX >= slotCoordinates.get(i + 0)) && (mouseX <= slotCoordinates.get(i + 2)) && (mouseY >= slotCoordinates.get(i + 1)) && (mouseY <= slotCoordinates.get(i + 3))) {

                    /*System.out.println("Recipes length: " + recipeCategory.recipes.length);
                    System.out.println("slotCoordinates.size() / 4: " + slotCoordinates.size() / 4);
                    System.out.println("i: " + i);
                    System.out.println("i / 4: " + i / 4);*/

                    if (player.getHeldItemOffhand().getCount() >= recipeCategory.recipes.get(i / 4).inputQuantity) {
                        recipeSelected = true;
                        recipeSelectedIndex = i / 4;
                        startTime = Minecraft.getSystemTime();

                        /*System.out.println("Selected recipe index: " + i / 4);
                        System.out.println("Start time: " + Minecraft.getSystemTime());*/
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
        String part1 = I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".title.select");
        String part2 = recipeCategory.displayName;
        String part3 = I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".title.recipe");
        String selectionguiTitle = part1 + " " + part2 + " " + part3;

        ArrayList<GuiSelectionRecipe> recipes = recipeCategory.recipes;

        int width1 = recipes.size() == 0 ? ICON_DISTANCE : recipes.size() <= MAX_ITEMS_PER_ROW ? ((recipes.size() % MAX_ITEMS_PER_ROW) * ICON_DISTANCE) : MAX_ITEMS_PER_ROW * ICON_DISTANCE;
        int width2 = ((fontRenderer.getStringWidth(selectionguiTitle)/ 4) * 4) + 8;

        int guiHeight;
        if (disableCloseGUIbutton) {
            guiHeight = GUI_BASE_HEIGHT - CLOSE_BUTTON_HEIGHT;
        } else {
            guiHeight = GUI_BASE_HEIGHT;
        }

        // Update dynamic GUI size
        super.updateContainerSize(GUI_BASE_WIDTH + Math.max(width1, width2), guiHeight + (((recipes.size() / MAX_ITEMS_PER_ROW) + 1) * ICON_DISTANCE));

        if (!disableCloseGUIbutton) {
            // Add Close button
            buttonList.add(buttonClose = new GuiButton(
                    0,
                    (width / 2) - (CLOSE_BUTTON_WIDTH / 2),
                    bottom - CLOSE_BUTTON_HEIGHT - CLOSE_BUTTON_OFFSET,
                    CLOSE_BUTTON_WIDTH,
                    CLOSE_BUTTON_HEIGHT,
                    I18n.format("gui." + SelectionGuiCrafting.MOD_ID + ".close")
            ));
        }

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



    private void drawGradientRectHorizontal(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, (double)this.zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double)left, (double)top, (double)this.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, (double)this.zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, (double)this.zLevel).color(f5, f6, f7, f4).endVertex();
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