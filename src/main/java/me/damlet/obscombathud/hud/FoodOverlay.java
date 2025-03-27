package me.damlet.obscombathud.hud;

import me.damlet.obscombathud.config.CombatHudConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class FoodOverlay {
    public static void renderFood(DrawContext context, PlayerEntity player, int top, int right) {
        if (!CombatHudConfig.SATURATION_HUD) {
            return;
        }

        float saturation = player.getHungerManager().getSaturationLevel();
        int startSaturationBar = 0;
        int endSaturationBar = (int) Math.ceil(saturation / 2.0F);

        int iconSize = 9;

        for (int i = startSaturationBar; i < endSaturationBar; i++) {

            int x = right - (i * 8) - 9;

            int v = 0;
            int u = 0;

            float effectiveSaturationOfBar = (saturation / 2.0F) - i;

            if (effectiveSaturationOfBar >= 1)
                u = 3 * iconSize;
            else if (effectiveSaturationOfBar > .5)
                u = 2 * iconSize;
            else if (effectiveSaturationOfBar > .25)
                u = 1 * iconSize;

            context.drawTexture(Identifier.of("obscombathud", "textures/gui/sprites/icons.png"), x, top, u, v, iconSize, iconSize);
        }
    }
}
