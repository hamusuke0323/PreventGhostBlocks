package com.hamusuke.preventghost.invoker.client;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface PlayerControllerMPInvoker {
    default void handleBlockBreakAck(WorldClient worldClient, BlockPos pos, CPacketPlayerDigging.Action action) {
    }
}
