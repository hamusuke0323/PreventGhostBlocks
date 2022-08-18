package com.hamusuke.preventghost;

import com.hamusuke.preventghost.network.Network;
import com.hamusuke.preventghost.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = PreventGhostBlocks.MOD_ID, name = PreventGhostBlocks.MOD_NAME, version = PreventGhostBlocks.MOD_VERSION)
public class PreventGhostBlocks {
    public static final String MOD_ID = "preventghost";
    public static final String MOD_NAME = "Prevent Ghost Blocks";
    public static final String MOD_VERSION = "1.0.0";
    @SidedProxy(serverSide = "com.hamusuke." + MOD_ID + ".proxy.CommonProxy", clientSide = "com.hamusuke." + MOD_ID + ".proxy.ClientProxy")
    public static CommonProxy PROXY;

    public PreventGhostBlocks() {
        Network.registerMsg();
    }
}
