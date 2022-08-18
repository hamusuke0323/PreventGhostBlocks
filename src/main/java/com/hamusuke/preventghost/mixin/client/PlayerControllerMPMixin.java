package com.hamusuke.preventghost.mixin.client;

import com.hamusuke.preventghost.invoker.client.PlayerControllerMPInvoker;
import com.hamusuke.preventghost.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@SideOnly(Side.CLIENT)
@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin implements PlayerControllerMPInvoker {
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    @Final
    private NetHandlerPlayClient connection;
    private final Object2ObjectLinkedOpenHashMap<Pair<BlockPos, CPacketPlayerDigging.Action>, Vec3d> unAcknowledgedActions = new Object2ObjectLinkedOpenHashMap<>();

    @ModifyArgs(method = "clickBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/play/client/CPacketPlayerDigging;<init>(Lnet/minecraft/network/play/client/CPacketPlayerDigging$Action;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)V"))
    private void clickBlock(Args args) {
        CPacketPlayerDigging.Action action = args.get(0);
        if (action == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            this.unAcknowledgedActions.put(Pair.of(args.get(1), action), this.mc.player.getPositionVector());
        }
    }

    @ModifyArgs(method = "onPlayerDamageBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/play/client/CPacketPlayerDigging;<init>(Lnet/minecraft/network/play/client/CPacketPlayerDigging$Action;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)V"))
    private void onPlayerDamageBlock(Args args) {
        this.unAcknowledgedActions.put(Pair.of(args.get(1), args.get(0)), this.mc.player.getPositionVector());
    }

    @Override
    public void handleBlockBreakAck(WorldClient worldClient, BlockPos pos, CPacketPlayerDigging.Action action) {
        this.unAcknowledgedActions.remove(Pair.of(pos, action));
        this.unAcknowledgedActions.forEach((blockPosActionPair, vec3d) -> {
            this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, blockPosActionPair.getFirst(), EnumFacing.DOWN));
        });
        this.unAcknowledgedActions.clear();
    }
}
