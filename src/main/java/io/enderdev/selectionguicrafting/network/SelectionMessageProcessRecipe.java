package io.enderdev.selectionguicrafting.network;

import io.enderdev.selectionguicrafting.registry.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

public class SelectionMessageProcessRecipe implements IMessage {

    public SelectionMessageProcessRecipe() {
    }

    private String recipeCategory;
    private int recipeIndex;
    private String playerName;

    public SelectionMessageProcessRecipe(String recipeCategory, int recipeIndex, String playerName) {
        this.recipeCategory = recipeCategory;
        this.recipeIndex = recipeIndex;
        this.playerName = playerName;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(recipeCategory.length());
        buf.writeCharSequence(recipeCategory, Charset.defaultCharset());

        buf.writeInt(recipeIndex);

        buf.writeInt(playerName.length());
        buf.writeCharSequence(playerName, Charset.defaultCharset());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int recipeCategoryLength = buf.readInt();
        recipeCategory = (String) buf.readCharSequence(recipeCategoryLength, Charset.defaultCharset());

        recipeIndex = buf.readInt();

        int playerNameLength = buf.readInt();
        playerName = (String) buf.readCharSequence(playerNameLength, Charset.defaultCharset());
    }


    public static class SelectionMessageProcessRecipeHandler implements IMessageHandler<SelectionMessageProcessRecipe, IMessage> {

        @Override
        public IMessage onMessage(SelectionMessageProcessRecipe message, MessageContext ctx) {
            EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(message.playerName);
            if (player == null) {
                return null;
            }

            ItemStack itemMainhand = player.getHeldItemMainhand();
            ItemStack stackOffhand = player.getHeldItemOffhand();
            if (itemMainhand.isEmpty() || stackOffhand.isEmpty()) {
                return null;
            }

            GsCategory category = GsRegistry.getCategory(message.recipeCategory);
            if (category == null) {
                return null;
            }

            GsRecipe recipe = GsRegistry.getValidRecipes(category, itemMainhand).get(message.recipeIndex);
            if (recipe == null) {
                return null;
            }

            if (!recipe.isInputValid(stackOffhand)) {
                return null;
            }

            if (!recipe.isToolValid(itemMainhand)) {
                return null;
            }

            GsTool tool = recipe.getTool(itemMainhand);
            if (tool == null) {
                return null;
            }

            // If we got here, the recipe is valid and can be processed
            Random random = new Random();
            recipe.getOutput().forEach(output -> {
                ItemStack stack = output.getItemStack().copy();
                GsEnum.OutputType outputType = recipe.getOutputType() != null ? recipe.getOutputType() : category.getOutputType();
                if (random.nextFloat() < output.getChance()) {
                    if (recipe.getOutputType() == GsEnum.OutputType.DROP) {
                        player.dropItem(stack, false, true);
                    } else {
                        if (!player.inventory.addItemStackToInventory(stack)) {
                            player.dropItem(stack, false, true);
                        }
                    }
                }
            });

            stackOffhand.shrink(recipe.getInputStackSize(stackOffhand));

            if (itemMainhand.isItemStackDamageable()) {
                int toolDamage = (int) (recipe.getDurability() * tool.getDamageMultiplier());
                itemMainhand.damageItem(toolDamage, player);
            } else {
                itemMainhand.shrink(tool.getItemStack().getCount());
            }

            player.addExperience(recipe.getXp());

            List<GsParticle> particles = recipe.getParticles() == null ? category.getParticles() : recipe.getParticles();
            particles.forEach(particle -> {
                double offsetX = random.nextGaussian();
                double offsetY = random.nextGaussian();
                double offsetZ = random.nextGaussian();
                ((WorldServer) player.getEntityWorld()).spawnParticle(particle.getType(), player.posX, player.posY, player.posZ, particle.getCount(), offsetX, offsetY, offsetZ, particle.getSpeed());
            });

            return null;
        }
    }
}