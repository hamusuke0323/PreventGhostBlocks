package com.hamusuke.preventghost.mixin;

import com.hamusuke.preventghost.invoker.PlayerInteractionManagerInvoker;
import com.hamusuke.preventghost.network.Network;
import com.hamusuke.preventghost.network.packet.BlockBreakAckS2CPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayServer.class)
public abstract class NetHandlerPlayServerMixin {
    @Shadow
    public EntityPlayerMP player;

    @Inject(method = "processPlayerDigging", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerInteractionManager;onBlockClicked(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)V", shift = At.Shift.BEFORE))
    private void processPlayerDigging$onBlockClicked(CPacketPlayerDigging packetIn, CallbackInfo ci) {
        ((PlayerInteractionManagerInvoker) this.player.interactionManager).registerAction(packetIn.getPosition(), packetIn.getAction());
    }

    @Inject(method = "processPlayerDigging", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetHandlerPlayServer;sendPacket(Lnet/minecraft/network/Packet;)V", shift = At.Shift.BEFORE))
    private void processPlayerDigging$onBlockClickedElse(CPacketPlayerDigging packetIn, CallbackInfo ci) {
        if (packetIn.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            Network.sendToClient(new BlockBreakAckS2CPacket(packetIn.getPosition(), CPacketPlayerDigging.Action.START_DESTROY_BLOCK), this.player);
        }
    }

    @Inject(method = "processPlayerDigging", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerInteractionManager;blockRemoving(Lnet/minecraft/util/math/BlockPos;)V", shift = At.Shift.BEFORE))
    private void processPlayerDigging$blockRemoving(CPacketPlayerDigging packetIn, CallbackInfo ci) {
        ((PlayerInteractionManagerInvoker) this.player.interactionManager).registerAction(packetIn.getPosition(), packetIn.getAction());
    }
}
