package com.hamusuke.preventghost.invoker;

import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;

public interface PlayerInteractionManagerInvoker {
    default void registerAction(BlockPos pos, CPacketPlayerDigging.Action action) {
    }
}
