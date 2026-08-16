package com.guishkaj;

import com.guishkaj.client.BindManager;
import com.guishkaj.client.CommandBindHandler;
import com.guishkaj.client.GuiScreenModern;
import com.guishkaj.client.TriggerBot;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientChatEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class GuishkajMod implements ClientModInitializer {
    public static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        // Register Right Shift to open GUI
        openGuiKey = new KeyBinding("key.guishkaj.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.guishkaj");
        KeyBindingHelper.registerKeyBinding(openGuiKey);

        BindManager.loadBinds();
        CommandBindHandler.register();
        TriggerBot.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                if (openGuiKey.wasPressed()) {
                    client.execute(() -> client.setScreen(new GuiScreenModern()));
                }
                BindManager.onTick(client);
                TriggerBot.onTick(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Intercept chat for .bind commands
        ClientChatEvents.CHAT_MESSAGE.register(message -> {
            if (message.startsWith(".bind ")) {
                return BindManager.handleChatCommand(message);
            }
            return false; // not handled
        });
    }
}
