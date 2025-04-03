package io.enderdev.selectionguicrafting.network;

import io.enderdev.selectionguicrafting.registry.*;
import io.enderdev.selectionguicrafting.registry.recipe.RecipeHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.Charset;

public class SelectionMessageProcessRecipe implements IMessage {

    public SelectionMessageProcessRecipe() {
    }

    private String recipeCategory;
    private int recipeIndex;
    private String playerName;
    private int xp;
    private double durabilityMultiplier;

    public SelectionMessageProcessRecipe(String recipeCategory, int recipeIndex, String playerName, int xp, double durabilityMultiplier) {
        this.recipeCategory = recipeCategory;
        this.recipeIndex = recipeIndex;
        this.playerName = playerName;
        this.xp = xp;
        this.durabilityMultiplier = durabilityMultiplier;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(recipeCategory.length());
        buf.writeCharSequence(recipeCategory, Charset.defaultCharset());

        buf.writeInt(recipeIndex);

        buf.writeInt(playerName.length());
        buf.writeCharSequence(playerName, Charset.defaultCharset());

        buf.writeInt(xp);
        buf.writeDouble(durabilityMultiplier);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int recipeCategoryLength = buf.readInt();
        recipeCategory = (String) buf.readCharSequence(recipeCategoryLength, Charset.defaultCharset());

        recipeIndex = buf.readInt();

        int playerNameLength = buf.readInt();
        playerName = (String) buf.readCharSequence(playerNameLength, Charset.defaultCharset());

        xp = buf.readInt();
        durabilityMultiplier = buf.readDouble();
    }


    public static class SelectionMessageProcessRecipeHandler implements IMessageHandler<SelectionMessageProcessRecipe, IMessage> {

        @Override
        public IMessage onMessage(SelectionMessageProcessRecipe message, MessageContext ctx) {
            EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(message.playerName);
            RecipeHandler recipeHandler = new RecipeHandler(player, Register.getCategoryByID(message.recipeCategory), message.recipeIndex, message.durabilityMultiplier, message.xp);
            recipeHandler.craft();
            return null;
        }
    }
}