package io.enderdev.selectionguicrafting.network;

import io.enderdev.selectionguicrafting.Tags;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class SelectionPacketHandler {
    public static final SimpleNetworkWrapper SELECTION_NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MOD_ID);
}