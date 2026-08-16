package com.guishkaj.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class BindManager {
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("guishkaj_binds.json");
    private static final Gson GSON = new Gson();
    private static Map<String, String> binds = new HashMap<>();
    private static Map<String, KeyBinding> keyBindings = new HashMap<>();

    public static void loadBinds() {
        try {
            if (Files.exists(CONFIG)) {
                String json = Files.readString(CONFIG);
                Type type = new TypeToken<Map<String, String>>(){}.getType();
                binds = GSON.fromJson(json, type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // register keybindings
        binds.forEach((name, key) -> registerBind(name, key));
    }

    public static void saveBinds() {
        try {
            String json = GSON.toJson(binds);
            Files.createDirectories(CONFIG.getParent());
            Files.writeString(CONFIG, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean handleChatCommand(String msg) {
        // .bind add <name> <key>
        String[] parts = msg.split("\\s+");
        if (parts.length >= 4 && parts[0].equals(".bind") && parts[1].equals("add")) {
            String name = parts[2];
            String key = parts[3];
            binds.put(name, key);
            registerBind(name, key);
            saveBinds();
            MinecraftClient.getInstance().player.sendMessage(
                    TextUtil.literal("[Guishkaj] bound " + name + " -> " + key), false);
            return true; // cancel chat
        }
        return false;
    }

    private static int keyFromString(String s) {
        // simple mapping for A-Z, 0-9
        if (s.length() == 1) {
            char c = Character.toUpperCase(s.charAt(0));
            if (c >= 'A' && c <= 'Z') return GLFW.GLFW_KEY_A + (c - 'A');
            if (c >= '0' && c <= '9') return GLFW.GLFW_KEY_0 + (c - '0');
        }
        // try parse as integer
        try { return Integer.parseInt(s); } catch (Exception e) {}
        // default
        return GLFW.GLFW_KEY_UNKNOWN;
    }

    private static void registerBind(String name, String keyString) {
        int key = keyFromString(keyString);
        KeyBinding kb = new KeyBinding("key.guishkaj.bind." + name,
                InputUtil.Type.KEYSYM,
                key,
                "category.guishkaj");
        KeyBindingHelper.registerKeyBinding(kb);
        keyBindings.put(name, kb);
    }

    public static void onTick(MinecraftClient client) {
        // Check binds and trigger functions
        keyBindings.forEach((name, kb) -> {
            if (kb.isPressed()) {
                if (name.equalsIgnoreCase("TriggerBot")) {
                    TriggerBot.trigger(client);
                }
            }
        });
    }
}
