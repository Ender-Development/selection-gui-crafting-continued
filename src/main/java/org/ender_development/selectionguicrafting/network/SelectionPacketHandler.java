/*
 * Copyright (c) 2023-2026 Ender-Development.org
 * SPDX-License-Identifier: MIT
 */

package org.ender_development.selectionguicrafting.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.ender_development.selectionguicrafting.Tags;

public class SelectionPacketHandler {
    public static final SimpleNetworkWrapper SELECTION_NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MOD_ID);
}
