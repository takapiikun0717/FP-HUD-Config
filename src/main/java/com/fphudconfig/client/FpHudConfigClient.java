package com.fphudconfig.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

/** クライアント専用の初期化。Fracture Point が無い環境で PlateHudRenderer をロードしないための分離 */
public final class FpHudConfigClient {
    private FpHudConfigClient() {}

    public static void init() {
        if (ModList.get().isLoaded("fracturepoint")) {
            MinecraftForge.EVENT_BUS.register(new PlateHudRenderer());
        }
    }
}
