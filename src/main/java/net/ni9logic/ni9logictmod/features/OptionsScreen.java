package net.ni9logic.ni9logictmod.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

import java.util.HashMap;

public class OptionsScreen extends Screen {
    public static KeyBinding GUI_KEY;
    private final HashMap<ButtonWidget, String> buttonTooltips = new HashMap<>();

    public OptionsScreen() {
        super(Text.empty());
    }

    @Override
    protected void init() {
        int x = this.width / 2, y = this.height / 2;

        this.buttonTooltips.put(this.addDrawableChild(
                ButtonWidget.builder(
                                Text.of("Activate Chat Games"), (button) -> {
                                    AutoClicker.setActive(!AutoClicker.isActive());
                                    button.setMessage(Text.of("Deactivated Chat Games"));
                                })
                        .dimensions(x - 200, y - 44, 130, 20)
                        .build()
        ), "Active Chat Games");
    }

    private void renderHelpingTip(MatrixStack stack, Text text) {
        int x = this.width / 2, y = this.height / 2;

        this.renderOrderedTooltip(stack,
                MinecraftClient.getInstance().textRenderer.wrapLines(StringVisitable.plain(text.getString()), 270),
                x - 140,
                y + 100);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);

        this.textRenderer.drawWithShadow(
                matrices,
                "Attack",
                this.width / 2f - 200,
                this.height / 2f - 56,
                0xFFFFFF);


        for (ButtonWidget button : buttonTooltips.keySet()) {
            if (button.isHovered()) {
                this.renderHelpingTip(matrices, Text.translatable(this.buttonTooltips.get(button)));
            }
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GUI_KEY.getDefaultKey().getCode()) {
            this.close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
