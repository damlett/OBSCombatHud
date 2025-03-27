package me.damlet.obscombathud.mixin.client;

import me.damlet.obscombathud.config.CombatHudConfig;
import me.damlet.obscombathud.hud.ArmorOverlay;
import me.damlet.obscombathud.hud.FoodOverlay;
import me.zziger.obsoverlay.OverlayRenderer;
import me.zziger.obsoverlay.registry.HUDOverlayComponent;
import me.zziger.obsoverlay.registry.OverlayComponentRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(at = @At("RETURN"), method = "renderHotbar")
    private void renderHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) {
        if (CombatHudConfig.armorOverlay == null) {
            CombatHudConfig.armorOverlay = new HUDOverlayComponent("armor_hud", true, true);
            OverlayComponentRegistry.registerComponent(CombatHudConfig.armorOverlay);
        }

        OverlayRenderer.beginDraw(CombatHudConfig.armorOverlay);
        ArmorOverlay.renderArmor(context, client);
        OverlayRenderer.endDraw(CombatHudConfig.armorOverlay);
    }

    @Inject(at = @At("RETURN"), method = "renderFood")
    private void renderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo info) {
        if (CombatHudConfig.saturationOverlay == null) {
            CombatHudConfig.saturationOverlay = new HUDOverlayComponent("saturation_hud", true, true);
            OverlayComponentRegistry.registerComponent(CombatHudConfig.saturationOverlay);
        }

        OverlayRenderer.beginDraw(CombatHudConfig.saturationOverlay);
        FoodOverlay.renderFood(context, player, top, right);
        OverlayRenderer.endDraw(CombatHudConfig.saturationOverlay);
    }

}
