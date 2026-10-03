package com.fphudconfig.mixin;

import com.fphudconfig.client.HudPlacement;
import com.fphudconfig.config.HudConfig;
import de.taczstamina.platform.forge.v1_20_1.client.Forge1201StaminaHudRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * TACZ Arm Stamina の Forge1201StaminaHudRenderer.render(Post, LocalPlayer, float stamina, float alpha) を
 * 置き換えて、位置とオンオフをconfigで変えられるようにする。バー本体の描画は元の drawXpStyleBar をそのまま使う。
 */
@Mixin(value = Forge1201StaminaHudRenderer.class, remap = false)
public abstract class StaminaHudRendererMixin {

    // 元mod側の定数 (BAR_WIDTH=79, BAR_X_OFFSET=-90)。黒フチ込みの見た目サイズは 81x4、描画原点から(-1,-1)
    // NOTE: Mixinクラスに非private staticフィールドは置けないので private static final にしている
    private static final int FPHC_BAR_WIDTH = 79;
    private static final int FPHC_BOX_W = 81;
    private static final int FPHC_BOX_H = 4;

    @Shadow(remap = false)
    private static int calculateBarY(int screenHeight, LocalPlayer player) {
        throw new AssertionError();
    }

    @Shadow(remap = false)
    private static void drawXpStyleBar(GuiGraphics graphics, int x, int y, int fillWidth, float alpha) {
        throw new AssertionError();
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true, remap = false)
    private static void fphudconfig$render(RenderGuiOverlayEvent.Post event, LocalPlayer player,
                                           float stamina, float alpha, CallbackInfo ci) {
        ci.cancel();

        HudConfig.Element cfg = HudConfig.STAMINA_BAR;
        if (!cfg.enabled.get()) {
            return;
        }

        int sw = event.getWindow().getGuiScaledWidth();
        int sh = event.getWindow().getGuiScaledHeight();

        int defX = sw / 2 - 90;
        int defY = calculateBarY(sh, player);

        // 枠の左上(=描画原点-1)を基準に配置してから +1 して描画原点に戻す
        HudPlacement.Pos p = HudPlacement.resolve(cfg, sw, sh, FPHC_BOX_W, FPHC_BOX_H, defX - 1, defY - 1);
        int fill = Math.round(FPHC_BAR_WIDTH * stamina);
        drawXpStyleBar(event.getGuiGraphics(), p.x() + 1, p.y() + 1, fill, alpha);
    }
}
