package gliese832c.selectionGuiCrafting.network;

import gliese832c.SelectionGuiCrafting;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class SelectionPacketHandler {
    public static final SimpleNetworkWrapper SELECTION_NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(SelectionGuiCrafting.MOD_ID);
}