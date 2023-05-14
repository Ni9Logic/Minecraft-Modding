package net.ni9logic.ni9logictmod.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class RotationDetect {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();
    private static final float THRESHOLD = 5.0F; // Change this to the threshold you want
    private static float lastYaw = 0;

    public static void detectRotation() {
        if (minecraft.player == null) {
            return;
        }

        float currentYaw = minecraft.player.getYaw();

        // Normalize the yaw values to be within 0 to 360 degree
        currentYaw = MathHelper.wrapDegrees(currentYaw);
        lastYaw = MathHelper.wrapDegrees(lastYaw);

        float changeInYaw = Math.abs(currentYaw - lastYaw);

        // If the change in yaw is larger than the threshold, assume the player has been rotated
        if (changeInYaw > THRESHOLD) {
            // Do what you want to do here when a rotation is detected
            System.out.println("Rotation detected!");
        }

        lastYaw = currentYaw;
    }
}
