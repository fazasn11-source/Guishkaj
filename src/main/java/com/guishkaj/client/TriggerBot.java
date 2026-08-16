package com.guishkaj.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

@Environment(EnvType.CLIENT)
public class TriggerBot {
    public static void register() {
        // placeholder for future registration
    }

    public static void onTick(MinecraftClient client) {
        // nothing here; using BindManager to call trigger when key pressed
    }

    public static void trigger(MinecraftClient client) {
        try {
            if (client.player == null) return;
            HitResult hr = client.crosshairTarget;
            if (hr == null) return;
            if (hr.getType() == HitResult.Type.ENTITY) {
                EntityHitResult ehr = (EntityHitResult) hr;
                Entity target = ehr.getEntity();
                // attack on client side - try interaction manager
                if (client.interactionManager != null) {
                    client.interactionManager.attackEntity(client.player, target);
                    client.player.swingHand(client.player.getActiveHand());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
