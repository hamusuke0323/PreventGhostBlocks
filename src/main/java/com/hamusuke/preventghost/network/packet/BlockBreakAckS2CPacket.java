package com.hamusuke.preventghost.network.packet;

import com.hamusuke.preventghost.PreventGhostBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BlockBreakAckS2CPacket implements IMessage, IMessageHandler<BlockBreakAckS2CPacket, IMessage> {
    private BlockPos pos;
    private CPacketPlayerDigging.Action action;

    public BlockBreakAckS2CPacket() {
    }

    public BlockBreakAckS2CPacket(BlockPos pos, CPacketPlayerDigging.Action action) {
        this.pos = pos.toImmutable();
        this.action = action;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        this.pos = buffer.readBlockPos();
        this.action = buffer.readEnumValue(CPacketPlayerDigging.Action.class);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeBlockPos(this.pos);
        buffer.writeEnumValue(this.action);
    }

    @Override
    public IMessage onMessage(BlockBreakAckS2CPacket message, MessageContext ctx) {
        PreventGhostBlocks.PROXY.onMessage(message);
        return null;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public CPacketPlayerDigging.Action getAction() {
        return this.action;
    }
}
