package com.guishkaj.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class GuiScreenModern extends Screen {
    private float alpha = 0f;
    private boolean opening = true;

    public GuiScreenModern() {
        super(Text.literal("Guishkaj Menu"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // simple pseudo-blur: draw several translucent layers with increasing alpha
        int w = this.width;
        int h = this.height;

        for (int i = 0; i < 6; i++) {
            fill(matrices, 0, 0, w, h, (int) (alpha * 30) << 24 | 0x202020);
        }

        // central panel
        int pw = 400; int ph = 240;
        int px = (w - pw) / 2; int py = (h - ph) / 2;
        fill(matrices, px, py, px+pw, py+ph, ((int)(alpha*200) << 24) | 0x0F172A);

        // header
        drawCenteredText(matrices, this.textRenderer, this.title.getString(), w/2, py + 12, 0xFFFFFF);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        if (opening) {
            alpha += 0.06f;
            if (alpha >= 1f) { alpha = 1f; opening = false; }
        }
    }

    @Override
    public boolean shouldPause() { return false; }
}
