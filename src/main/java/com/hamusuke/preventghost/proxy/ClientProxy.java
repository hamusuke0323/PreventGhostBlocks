package com.hamusuke.preventghost.proxy;

import com.hamusuke.preventghost.invoker.client.PlayerControllerMPInvoker;
import com.hamusuke.preventghost.network.packet.BlockBreakAckS2CPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    private static final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void onMessage(BlockBreakAckS2CPacket packet) {
        ((PlayerControllerMPInvoker) mc.playerController).handleBlockBreakAck(mc.world, packet.getPos(), packet.getAction());
    }
}
