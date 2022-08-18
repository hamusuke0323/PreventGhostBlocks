package com.hamusuke.preventghost.network;

import com.hamusuke.preventghost.PreventGhostBlocks;
import com.hamusuke.preventghost.network.packet.BlockBreakAckS2CPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Network {
    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(PreventGhostBlocks.MOD_ID + ":main");
    private static int ID;

    static {
        INSTANCE.registerMessage(BlockBreakAckS2CPacket.class, BlockBreakAckS2CPacket.class, next(), Side.CLIENT);
    }

    public static void registerMsg() {
    }

    private static int next() {
        return ID++;
    }

    public static void sendToClient(IMessage message, EntityPlayerMP entityPlayerMP) {
        INSTANCE.sendTo(message, entityPlayerMP);
    }
}
