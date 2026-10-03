package com.fphudconfig.config;

/**
 * DEFAULT = 元modの標準位置 (offsetX/Y はそこからのズレ)。
 * それ以外 = 画面のその位置に要素を揃える (offsetX/Y は そこからのズレ)。
 * fx/fy は 0=左/上, 0.5=中央, 1=右/下。
 */
public enum Anchor {
    DEFAULT(0f, 0f),
    TOP_LEFT(0f, 0f),
    TOP_CENTER(0.5f, 0f),
    TOP_RIGHT(1f, 0f),
    MIDDLE_LEFT(0f, 0.5f),
    CENTER(0.5f, 0.5f),
    MIDDLE_RIGHT(1f, 0.5f),
    BOTTOM_LEFT(0f, 1f),
    BOTTOM_CENTER(0.5f, 1f),
    BOTTOM_RIGHT(1f, 1f);

    public final float fx;
    public final float fy;

    Anchor(float fx, float fy) {
        this.fx = fx;
        this.fy = fy;
    }

    public boolean isDefault() {
        return this == DEFAULT;
    }

    /** 画面幅 screen の中で、幅 size の要素をこのアンカーに揃えたときの左端X */
    public int originX(int screen, int size) {
        return Math.round((screen - size) * fx);
    }

    /** 画面高 screen の中で、高さ size の要素をこのアンカーに揃えたときの上端Y */
    public int originY(int screen, int size) {
        return Math.round((screen - size) * fy);
    }
}
