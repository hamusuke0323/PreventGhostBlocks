package com.hamusuke.preventghost.mixin;

import com.google.common.collect.Maps;
import com.hamusuke.preventghost.invoker.PlayerInteractionManagerInvoker;
import com.hamusuke.preventghost.network.Network;
import com.hamusuke.preventghost.network.packet.BlockBreakAckS2CPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PlayerInteractionManager.class)
public class PlayerInteractionManagerMixin implements PlayerInteractionManagerInvoker {
    @Shadow
    public EntityPlayerMP player;

    private final Map<BlockPos, CPacketPlayerDigging.Action> map = Maps.newHashMap();

    @Inject(method = "onBlockClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetHandlerPlayServer;sendPacket(Lnet/minecraft/network/Packet;)V", shift = At.Shift.BEFORE))
    private void onBlockClicked(BlockPos pos, EnumFacing side, CallbackInfo ci) {
        CPacketPlayerDigging.Action action = this.map.remove(pos);
        if (action != null) {
            Network.sendToClient(new BlockBreakAckS2CPacket(pos, action), this.player);
        }
    }

    @Inject(method = "onBlockClicked", at = @At("TAIL"))
    private void onBlockClicked$TAIL(BlockPos pos, EnumFacing side, CallbackInfo ci) {
        this.map.clear();
    }

    @Inject(method = "tryHarvestBlock", at = @At("RETURN"))
    private void tryHarvestBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        CPacketPlayerDigging.Action action = this.map.remove(pos);
        action = action == null ? CPacketPlayerDigging.Action.START_DESTROY_BLOCK : action;
        Network.sendToClient(new BlockBreakAckS2CPacket(pos, action), this.player);
    }

    @Override
    public void registerAction(BlockPos pos, CPacketPlayerDigging.Action action) {
        this.map.put(pos, action);
    }
}
