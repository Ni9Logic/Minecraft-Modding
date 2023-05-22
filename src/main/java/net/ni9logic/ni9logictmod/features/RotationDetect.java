package net.ni9logic.ni9logictmod.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.ni9logic.utils.TypeitMessage;

public class RotationDetect {
    public static KeyBinding KeyRotationDetect;
    private static boolean isRotationDetect = false;
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();
    private static final float THRESHOLD = 5.0F; // Change this to the threshold you want
    private static float lastYaw = 0;
    private static float currentYaw = 0;
    private static boolean once_rotated = false;

    public static void detectRotation() {
        if (KeyRotationDetect.wasPressed()) {
            isRotationDetect = !isRotationDetect; // toggle the sending on/off
            assert minecraft.player != null;
            Style activateStyle = Style.EMPTY
                    .withColor(Formatting.DARK_RED)
                    .withBold(true);
            Style deactivateStyle = Style.EMPTY
                    .withColor(Formatting.DARK_GREEN)
                    .withBold(true);
            MutableText toggle = Text.of(isRotationDetect ? " [ENABLED]" : " [DISABLED]").copy()
                    .setStyle(isRotationDetect ? activateStyle : deactivateStyle);

            minecraft.player.sendMessage(Text.of("Rotation Detect").copy().append(toggle), true);
            if (isRotationDetect) {
                lastYaw = minecraft.player.getYaw();
            }
        }
        if (!isRotationDetect) {
            once_rotated = false;
        }
        if (isRotationDetect) {
            if (minecraft.player == null) {
                return;
            }

            currentYaw = minecraft.player.getYaw();

            // Normalize the yaw values to be within 0 to 360 degree
            currentYaw = MathHelper.wrapDegrees(currentYaw);
            lastYaw = MathHelper.wrapDegrees(lastYaw);

            float changeInYaw = Math.abs(currentYaw - lastYaw);

            // If the change in yaw is larger than the threshold, assume the player has been rotated
            if (changeInYaw > THRESHOLD) {
                try {
                    Thread.sleep(1500);
                    TypeitMessage.exit_game("Nice abuse man, so professional of you. Am out and done here");
                    Thread.sleep(1000);

                    isRotationDetect = false;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            lastYaw = currentYaw;
        }
    }
}