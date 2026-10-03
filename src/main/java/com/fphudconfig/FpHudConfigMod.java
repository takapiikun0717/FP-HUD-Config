package com.fphudconfig;

import com.fphudconfig.client.FpHudConfigClient;
import com.fphudconfig.config.HudConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(FpHudConfigMod.MODID)
public class FpHudConfigMod {
    public static final String MODID = "fphudconfig";

    public FpHudConfigMod() {
        // config/fphudconfig-client.toml
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, HudConfig.SPEC, "fphudconfig-client.toml");

        if (FMLEnvironment.dist == Dist.CLIENT) {
            FpHudConfigClient.init();
        }
    }
}
