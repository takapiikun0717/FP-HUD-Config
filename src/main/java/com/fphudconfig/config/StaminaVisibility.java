package com.fphudconfig.config;

/** スタミナバーの表示タイミング */
public enum StaminaVisibility {
    /** TACZ Arm Stamina 本来の挙動 (構え/消費時だけ表示してフェードアウト) */
    DEFAULT,
    /** 常に表示 */
    ALWAYS,
    /** 銃を持っている間は常に表示 (持っていない時は本来の挙動) */
    HOLDING_GUN
}
