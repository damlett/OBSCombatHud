package me.damlet.obscombathud.hud;

import me.damlet.obscombathud.config.CombatHudConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static java.lang.Math.min;

public class ArmorOverlay {

    public static void renderArmor(DrawContext context, MinecraftClient client) {
        if (!CombatHudConfig.ARMOR_HUD) {
            return;
        }

        assert client.player != null;
        int targetMend = getTargetDurability(client.player);
        final float x = context.getScaledWindowWidth() - CombatHudConfig.OFFSET_X;
        float y = context.getScaledWindowHeight() - CombatHudConfig.OFFSET_Y;

        for (ItemStack armor : client.player.getArmorItems()) {
            y -= CombatHudConfig.SPACING;
            renderArmor(context, x, y, client, armor, targetMend);
        }
    }

    public static void renderArmor(DrawContext context, float x, float y, MinecraftClient client, ItemStack stack, int targetMend) {
        if (stack.isEmpty()) return;
        int smallDurability = 70;

        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);

        int durability = stack.getMaxDamage() - stack.getDamage();
        int color = CombatHudConfig.COLOR;
        if (durability < smallDurability) {
            color = CombatHudConfig.LOW_COLOR;
        } else if (durability < targetMend) {
            color = CombatHudConfig.MEDIUM_COLOR;
        }

        context.drawItem(client.player, stack, 0, 0, 1);
        if (stack.isDamageable()) {
            context.drawText(
                    client.textRenderer,
                    String.valueOf(stack.getMaxDamage() - stack.getDamage()),
                    20, 4, color, CombatHudConfig.SHADOW);
        }

        context.drawItemInSlot(client.textRenderer, stack, 0,0);
        context.getMatrices().pop();
    }

    public static int getTargetDurability(PlayerEntity player) {
        int xpPerBottle = 10;
        int xpHealing = player.getInventory().count(Items.EXPERIENCE_BOTTLE) * xpPerBottle;
        int minMaxDurability = Integer.MAX_VALUE;
        int totalDurability = 0;
        int numArmor = 0;

        Collection<ItemStack> items = new ArrayList<>();
        items.addAll(player.getInventory().main);
        items.addAll(player.getInventory().armor);
        items.addAll(player.getInventory().offHand);

        for (ItemStack stack : items) {
            if (stack.getItem() instanceof Equipment && stack.isDamageable()) {
                int durability = stack.getMaxDamage() - stack.getDamage();
                totalDurability += durability;
                numArmor++;
                minMaxDurability = min(stack.getMaxDamage(), minMaxDurability);
            }
        }

        if (numArmor == 0) {
            return 0;
        }

        return min(minMaxDurability, (xpHealing + totalDurability) / numArmor);
    }

}
