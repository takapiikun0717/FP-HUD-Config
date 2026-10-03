package com.fphudconfig.mixin;

import com.raiiiden.warborn.client.gui.PlateHudOverlay;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fracture Point 本体のプレートHUD描画を無効化する。
 * 代わりに com.fphudconfig.client.PlateHudRenderer が同じ内容をconfig付きで描く。
 */
@Mixin(value = PlateHudOverlay.class, remap = false)
public abstract class PlateHudOverlayMixin {

    @Inject(method = "onRenderOverlay", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fphudconfig$disableOriginal(RenderGuiOverlayEvent.Post event, CallbackInfo ci) {
        ci.cancel();
    }
}
