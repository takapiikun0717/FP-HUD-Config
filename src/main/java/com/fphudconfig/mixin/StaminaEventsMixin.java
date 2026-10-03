package com.fphudconfig.mixin;

import com.fphudconfig.config.HudConfig;
import com.fphudconfig.config.StaminaVisibility;
import de.taczstamina.config.ArmStaminaConfig;
import de.taczstamina.platform.forge.v1_20_1.client.Forge1201ClientStaminaEvents;
import de.taczstamina.platform.forge.v1_20_1.client.Forge1201StaminaHudRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 元の onRenderOverlay は「barAlpha が 0 以下なら描画しない」ため、常時表示にするには
 * ここで割り込んで、フェード状態に関係なく render(...) を呼ぶ。
 */
@Mixin(value = Forge1201ClientStaminaEvents.class, remap = false)
public abstract class StaminaEventsMixin {

    @Shadow(remap = false)
    private float stamina;

    @Shadow(remap = false)
    private boolean holdingGun;

    @Inject(method = "onRenderOverlay", at = @At("HEAD"), cancellable = true, remap = false)
    private void fphudconfig$forceVisible(RenderGuiOverlayEvent.Post event, CallbackInfo ci) {
        StaminaVisibility mode = HudConfig.STAMINA_VISIBILITY.get();
        if (mode == StaminaVisibility.DEFAULT) {
            return;
        }
        if (mode == StaminaVisibility.HOLDING_GUN && !this.holdingGun) {
            return; // 銃を持っていない間は元の挙動(フェード)
        }

        ci.cancel();
        if (!ArmStaminaConfig.showStaminaBar()) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        Forge1201StaminaHudRenderer.render(event, player, this.stamina, 1.0F);
    }
}
