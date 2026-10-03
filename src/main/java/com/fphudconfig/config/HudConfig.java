package com.fphudconfig.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class HudConfig {
    public static final ForgeConfigSpec SPEC;

    /** Fracture Point: ホットバー横のプレートアイコン */
    public static final Element PLATE_ICON;
    /** Fracture Point: プレート(装甲)耐久バー (前/後ろ2本をひとまとめで移動) */
    public static final Element PLATE_BARS;
    /** TACZ Arm Stamina: 腕スタミナバー */
    public static final Element STAMINA_BAR;
    /** TACZ Arm Stamina: スタミナバーの表示タイミング */
    public static final ForgeConfigSpec.EnumValue<StaminaVisibility> STAMINA_VISIBILITY;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.comment("Fracture Point HUD / Fracture Point の HUD").push("fracture_point");
        PLATE_ICON = new Element(b, "plate_icon",
                "Plate icon next to the hotbar (18x18). Default position: left of the hotbar (shifted left when the offhand is used).\n"
                        + "ホットバー横のプレートアイコン(18x18)。標準位置はホットバー左(オフハンド使用時はさらに左)。");
        PLATE_BARS = new Element(b, "plate_bars",
                "Plate durability bars (front+back as one 80x2 group). Default position: above the health bar.\n"
                        + "プレート耐久バー(前+後ろをまとめた80x2のグループ)。標準位置は体力バーの上。");
        b.pop();

        b.comment("TACZ Arm Stamina HUD / TACZ Arm Stamina の HUD").push("tacz_arm_stamina");
        STAMINA_VISIBILITY = b.comment(
                "When to show the stamina bar. DEFAULT = original behavior (only while aiming/recovering), ALWAYS = always visible,\n"
                        + "HOLDING_GUN = always visible while holding a gun (original behavior otherwise).\n"
                        + "Note: TACZ Arm Stamina's own show-stamina-bar option still applies.\n"
                        + "スタミナバーの表示タイミング。DEFAULT=元の挙動(構え/回復中のみ)、ALWAYS=常時表示、HOLDING_GUN=銃を持っている間は常時表示。\n"
                        + "※TACZ Arm Stamina 側のバー表示設定がオフなら表示されません。")
                .defineEnum("visibility", StaminaVisibility.DEFAULT);
        STAMINA_BAR = new Element(b, "stamina_bar",
                "Arm stamina bar (81x4 including its border). Default position: above the hearts row.\n"
                        + "腕スタミナバー(枠込みで81x4)。標準位置は体力バーの上。");
        b.pop();

        SPEC = b.build();
    }

    private HudConfig() {}

    public static final class Element {
        public final ForgeConfigSpec.BooleanValue enabled;
        public final ForgeConfigSpec.EnumValue<Anchor> anchor;
        public final ForgeConfigSpec.IntValue offsetX;
        public final ForgeConfigSpec.IntValue offsetY;

        Element(ForgeConfigSpec.Builder b, String name, String comment) {
            b.comment(comment).push(name);
            enabled = b.comment("Show this element. / 表示する").define("enabled", true);
            anchor = b.comment(
                    "DEFAULT = original mod's position (+offset). Others = align to that screen position (+offset).\n"
                            + "DEFAULT = 元modの標準位置(+オフセット)。それ以外 = 画面のその位置に揃える(+オフセット)。\n"
                            + "Values: DEFAULT, TOP_LEFT, TOP_CENTER, TOP_RIGHT, MIDDLE_LEFT, CENTER, MIDDLE_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT")
                    .defineEnum("anchor", Anchor.DEFAULT);
            offsetX = b.comment("Horizontal offset in GUI-scaled pixels (+ = right). / 横オフセット(GUIスケール後のpx, +で右)")
                    .defineInRange("offsetX", 0, -4096, 4096);
            offsetY = b.comment("Vertical offset in GUI-scaled pixels (+ = down). / 縦オフセット(GUIスケール後のpx, +で下)")
                    .defineInRange("offsetY", 0, -4096, 4096);
            b.pop();
        }
    }
}
