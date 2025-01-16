package io.enderdev.selectionguicrafting.network;

import io.enderdev.selectionguicrafting.proxy.CommonProxy;
import io.enderdev.selectionguicrafting.recipe.GuiSelectionRecipeCategory;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.Charset;

import static io.enderdev.selectionguicrafting.proxy.CommonProxy.*;

public class SelectionMessageProcessRecipe implements IMessage {

    public SelectionMessageProcessRecipe(){}

    private String recipeCategory;
    private int recipeIndex;
    private String playerName;

    public SelectionMessageProcessRecipe(String recipeCategory, int recipeIndex, String playerName) {
        this.recipeCategory = recipeCategory;
        this.recipeIndex = recipeIndex;
        this.playerName = playerName;
    }

    @Override public void toBytes(ByteBuf buf) {
        buf.writeInt(recipeCategory.length());
        buf.writeCharSequence(recipeCategory, Charset.defaultCharset());

        buf.writeInt(recipeIndex);

        buf.writeInt(playerName.length());
        buf.writeCharSequence(playerName, Charset.defaultCharset());
    }

    @Override public void fromBytes(ByteBuf buf) {
        int recipeCategoryLength = buf.readInt();
        recipeCategory = (String) buf.readCharSequence(recipeCategoryLength, Charset.defaultCharset());

        recipeIndex = buf.readInt();

        int playerNameLength = buf.readInt();
        playerName = (String) buf.readCharSequence(playerNameLength, Charset.defaultCharset());
    }



    public static class SelectionMessageProcessRecipeHandler implements IMessageHandler<SelectionMessageProcessRecipe, IMessage> {

        @Override
        public IMessage onMessage(SelectionMessageProcessRecipe message, MessageContext ctx) {

            GuiSelectionRecipeCategory category = CommonProxy.recipeCategories.get(message.recipeCategory);

            if(category == null)
                return null;
            if(message.recipeIndex >= category.recipes.size())
                return null;

            EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(message.playerName);
            if(player == null)
                return null;

            ItemStack itemMainhand = player.getHeldItemMainhand();
            ItemStack stackOffhand = player.getHeldItemOffhand();

            int toolIndex = 0;
            for (ItemStack itemTool : getToolFromCategory(message.recipeCategory)) {
                if (ItemStack.areItemStacksEqual(new ItemStack(itemTool.getItem(), 1, 0, itemTool.getTagCompound()), new ItemStack(itemMainhand.getItem(), 1, 0, itemMainhand.getTagCompound()))) {
                    boolean validMeta = false;
                    if (getPairFromCategory(message.recipeCategory).durabilityMultipliers.get(toolIndex) == Float.MAX_VALUE) {
                        if (itemTool.getMetadata() == itemMainhand.getMetadata()) {
                            validMeta = true;
                        }
                    } else {
                        validMeta = true;
                    }
                    if (validMeta) {
                        for (ItemStack itemInput : getItemFromCategory(message.recipeCategory)) {
                            boolean valid = false;
                            if (itemInput.getMetadata() == Short.MAX_VALUE) {
                                if (ItemStack.areItemStacksEqual(new ItemStack(itemInput.getItem(), 1, Short.MAX_VALUE, itemInput.getTagCompound()), (new ItemStack(stackOffhand.getItem(), 1, Short.MAX_VALUE, stackOffhand.getTagCompound())))) {
                                    valid = true;
                                }
                            } else {
                                if (ItemStack.areItemStacksEqual(new ItemStack(itemInput.getItem(), 1, itemInput.getMetadata(), itemInput.getTagCompound()), new ItemStack(stackOffhand.getItem(), 1, stackOffhand.getMetadata(), stackOffhand.getTagCompound()))) {
                                    valid = true;
                                }
                            }

                            if (valid) {
                                if (category.recipes.get(message.recipeIndex).inputQuantity <= player.getHeldItemOffhand().getCount()) {

                                    // If all requirements are met, do the actual recipe
                                    for (ItemStack itemStack : category.recipes.get(message.recipeIndex).outputs) {
                                        //FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(message.playerName).addItemStackToInventory(itemStack.copy());
                                        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(message.playerName).dropItem(itemStack.copy(), false, true);
                                    }
                                    stackOffhand.shrink(category.recipes.get(message.recipeIndex).inputQuantity);

                                    float toolDurabilityMultiplier = getPairFromCategory(message.recipeCategory).durabilityMultipliers.get(toolIndex);
                                    if (toolDurabilityMultiplier == Float.MAX_VALUE) {
                                        player.getHeldItemMainhand().shrink(1);
                                    } else {
                                        player.getHeldItemMainhand().damageItem((int) (category.recipes.get(message.recipeIndex).durabilityUsage * toolDurabilityMultiplier), player);
                                    }

                                    return null;
                                }
                            }
                        }
                    }
                }
                toolIndex++;
            }

            return null;
        }
    }
}