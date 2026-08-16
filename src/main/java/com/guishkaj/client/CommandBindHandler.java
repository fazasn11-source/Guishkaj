package com.guishkaj.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.message.v1.ClientChatEvents;

@Environment(EnvType.CLIENT)
public class CommandBindHandler {
    public static void register() {
        // ClientChatEvents already used from main mod initializer; this class kept for expansion.
        // Left intentionally simple.
    }
}
