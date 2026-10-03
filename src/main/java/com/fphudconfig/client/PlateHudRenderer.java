package com.fphudconfig.client;

import com.fphudconfig.config.HudConfig;
import com.raiiiden.warborn.common.object.capability.PlateHolderCapability;
import com.raiiiden.warborn.common.object.capability.PlateHolderProvider;
import com.raiiiden.warborn.common.object.plate.Plate;
import com.raiiiden.warborn.common.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Fracture Point の PlateHudOverlay (元の描画は PlateHudOverlayMixin で無効化済み) の置き換え。
 * 既定設定では元と同じ位置・見た目になる。
 */
public final class PlateHudRenderer {
    private static final ResourceLocation PLATE_ICON =
            new ResourceLocation("fracturepoint", "textures/gui/plate_icon.png");
    private static final Color BAR_BACKGROUND = new Color(26, 26, 42);

    private static final int ICON_SIZE = 18;
    private static final int BAR_WIDTH = 36;
    private static final int BAR_HEIGHT = 2;
    /** 前バーの左端 -> 後ろバーの左端 (元: w/2-90 と w/2-46) */
    private static final int BAR_SPACING = 44;
    private static final int BARS_GROUP_WIDTH = BAR_SPACING + BAR_WIDTH;

    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!"minecraft:armor_level".equals(event.getOverlay().id().toString())) {
            return;
        }
        boolean iconOn = HudConfig.PLATE_ICON.enabled.get();
        boolean barsOn = HudConfig.PLATE_BARS.enabled.get();
        if (!iconOn && !barsOn) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || player.isCreative() || player.isSpectator()) {
            return;
        }
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.isEmpty()) {
            return;
        }

        GuiGraphics g = event.getGuiGraphics();
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        chest.getCapability(PlateHolderProvider.CAP)
                .ifPresent(cap -> draw(g, player, cap, sw, sh, iconOn, barsOn));
    }

    private static void draw(GuiGraphics g, LocalPlayer player, PlateHolderCapability cap,
                             int sw, int sh, boolean iconOn, boolean barsOn) {
        if (iconOn) {
            boolean offhand = !player.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty();
            int defX = sw / 2 - 110 - (offhand ? 30 : 0);
            int defY = sh - 20;
            HudPlacement.Pos p = HudPlacement.resolve(HudConfig.PLATE_ICON, sw, sh, ICON_SIZE, ICON_SIZE, defX, defY);
            g.blit(PLATE_ICON, p.x(), p.y(), 0.0F, 0.0F, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        }

        if (barsOn) {
            boolean hasAbsorption = player.getAbsorptionAmount() > 0.0F;
            boolean hasArmor = player.getArmorValue() > 0;
            int rows = 9 * ((hasAbsorption ? 1 : 0) + (hasArmor ? 1 : 0));
            int defX = sw / 2 - 90;
            int defY = sh - 45 - rows;
            HudPlacement.Pos p = HudPlacement.resolve(HudConfig.PLATE_BARS, sw, sh, BARS_GROUP_WIDTH, BAR_HEIGHT, defX, defY);

            if (cap.hasFrontPlate()) {
                drawPlateBar(g, p.x(), p.y(), cap.getFrontPlate());
            }
            if (cap.hasBackPlate()) {
                drawPlateBar(g, p.x() + BAR_SPACING, p.y(), cap.getBackPlate());
            }
        }
    }

    private static void drawPlateBar(GuiGraphics g, int x, int y, Plate plate) {
        float current = plate.getCurrentDurability();
        float max = plate.getMaxDurability();
        int filled = (int) (current / max * BAR_WIDTH);
        g.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, BAR_BACKGROUND.getARGB());
        g.fill(x, y, x + filled, y + BAR_HEIGHT, plate.getMaterial().getColor().getARGB());
    }
}
