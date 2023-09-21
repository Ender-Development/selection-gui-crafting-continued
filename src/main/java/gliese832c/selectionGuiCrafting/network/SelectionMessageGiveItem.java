package gliese832c.selectionGuiCrafting.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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

public class SelectionMessageGiveItem implements IMessage {

    public SelectionMessageGiveItem(){}

    private ItemStack[] outputs;
    private String playerName;

    public SelectionMessageGiveItem(ItemStack[] outputs, String playerName) {
        this.outputs = outputs;
        this.playerName = playerName;
    }

    @Override public void toBytes(ByteBuf buf) {
        StringBuilder itemNames = new StringBuilder();
        StringBuilder itemAmounts = new StringBuilder();
        StringBuilder itemMetas = new StringBuilder();
        StringBuilder itemNBTs = new StringBuilder();

        for (ItemStack itemStack : outputs) {
            itemNames.append(itemStack.getItem().getRegistryName());
            itemNames.append("|");

            itemAmounts.append(itemStack.getCount());
            itemAmounts.append("|");

            itemMetas.append(itemStack.getMetadata());
            itemMetas.append("|");

            if (itemStack.getTagCompound() != null) {
                itemNBTs.append(itemStack.getTagCompound().toString());
            } else {
                itemNBTs.append(" ");
            }
            itemNBTs.append("|");
        }
        itemNames = new StringBuilder(itemNames.substring(0, itemNames.length() - 1));
        itemAmounts = new StringBuilder(itemAmounts.substring(0, itemAmounts.length() - 1));
        itemMetas = new StringBuilder(itemMetas.substring(0, itemMetas.length() - 1));
        itemNBTs = new StringBuilder(itemNBTs.substring(0, itemNBTs.length() - 1));

        buf.writeInt(itemNames.length());
        buf.writeInt(itemAmounts.length());
        buf.writeInt(itemMetas.length());
        buf.writeInt(itemNBTs.length());

        buf.writeCharSequence(itemNames.toString(), Charset.defaultCharset());
        buf.writeCharSequence(itemAmounts.toString(), Charset.defaultCharset());
        buf.writeCharSequence(itemMetas.toString(), Charset.defaultCharset());
        buf.writeCharSequence(itemNBTs.toString(), Charset.defaultCharset());

        buf.writeInt(playerName.length());
        buf.writeCharSequence(playerName, Charset.defaultCharset());
    }

    @Override public void fromBytes(ByteBuf buf) {
        int itemNamesLength = buf.readInt();
        int itemAmountsLength = buf.readInt();
        int itemMetasLength = buf.readInt();
        int itemNBTsLength = buf.readInt();

        String itemNames = (String) buf.readCharSequence(itemNamesLength, Charset.defaultCharset());
        String itemAmounts = (String) buf.readCharSequence(itemAmountsLength, Charset.defaultCharset());
        String itemMetas = (String) buf.readCharSequence(itemMetasLength, Charset.defaultCharset());
        String itemNBTs = (String) buf.readCharSequence(itemNBTsLength, Charset.defaultCharset());

        ItemStack[] itemStacks = new ItemStack[itemNames.split("\\|").length];
        for (int i = 0; i < itemNames.split("\\|").length; i++) {
            String[] itemNamesSplit = itemNames.split("\\|");
            String[] itemAmountsSplit = itemAmounts.split("\\|");
            String[] itemMetasSplit = itemMetas.split("\\|");
            String[] itemNBTsSplit = itemNBTs.split("\\|");

            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            try {
                nbtTagCompound = JsonToNBT.getTagFromJson(itemNBTsSplit[i]);
            } catch (NBTException e) {
                e.printStackTrace();
            }

            itemStacks[i] = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(itemNamesSplit[i])), Integer.parseInt(itemAmountsSplit[i]), Integer.parseInt(itemMetasSplit[i]), nbtTagCompound);
        }

        outputs = itemStacks;

        int playerNameLength = buf.readInt();
        playerName = (String) buf.readCharSequence(playerNameLength, Charset.defaultCharset());
    }



    public static class SelectionMessageGiveItemHandler implements IMessageHandler<SelectionMessageGiveItem, IMessage> {

        @Override
        public IMessage onMessage(SelectionMessageGiveItem message, MessageContext ctx) {

            System.out.println("Message received!");

            for (ItemStack itemStack : message.outputs) {
                FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(message.playerName).addItemStackToInventory(itemStack);
            }

            return null;
        }
    }
}