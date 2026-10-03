package com.fphudconfig.client;

import com.fphudconfig.config.Anchor;
import com.fphudconfig.config.HudConfig;

public final class HudPlacement {
    public record Pos(int x, int y) {}

    private HudPlacement() {}

    /**
     * @param sw,sh     GUIスケール後の画面サイズ
     * @param ew,eh     要素サイズ (アンカー揃え用)
     * @param defX,defY 元modが描く標準位置 (要素の左上)
     */
    public static Pos resolve(HudConfig.Element cfg, int sw, int sh, int ew, int eh, int defX, int defY) {
        Anchor a = cfg.anchor.get();
        int ox = cfg.offsetX.get();
        int oy = cfg.offsetY.get();
        if (a.isDefault()) {
            return new Pos(defX + ox, defY + oy);
        }
        return new Pos(a.originX(sw, ew) + ox, a.originY(sh, eh) + oy);
    }
}
