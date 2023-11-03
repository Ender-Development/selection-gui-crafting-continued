package gliese832c.selectionGuiCrafting.network;

import gliese832c.selectionGuiCrafting.proxy.CommonProxy;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionItemPair;
import gliese832c.selectionGuiCrafting.recipe.GuiSelectionRecipeCategory;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.Charset;
import java.util.Objects;

import static gliese832c.selectionGuiCrafting.proxy.CommonProxy.*;

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

            Item itemMainhand = player.getHeldItemMainhand().getItem();
            ItemStack stackOffhand = player.getHeldItemOffhand();

            int toolIndex = 0;
            for (Item itemTool : getToolFromCategory(message.recipeCategory)) {
                if (ItemStack.areItemStacksEqual(new ItemStack(itemTool), new ItemStack(itemMainhand))) {
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
                toolIndex++;
            }

            return null;
        }
    }
}