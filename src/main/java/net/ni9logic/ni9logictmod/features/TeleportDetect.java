package net.ni9logic.ni9logictmod.features;


import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.ni9logic.ni9logictmod.ni9logic;
import net.ni9logic.utils.TypeitMessage;

import java.awt.*;
import java.io.IOException;

import static net.ni9logic.ni9logictmod.Ni9LogicMod.minecraft;

public class TeleportDetect {
    public static KeyBinding KeyTeleportDetect;
    private static boolean canTeleport = false;
    private static Vec3d prevPos = null;

    public static void detectTp() {
        if (KeyTeleportDetect.wasPressed()) {
            canTeleport = !canTeleport; // toggle the sending on/off
            prevPos = null;
            assert minecraft.player != null;
            Style activateStyle = Style.EMPTY
                    .withColor(Formatting.DARK_RED)
                    .withBold(true);
            Style deactivateStyle = Style.EMPTY
                    .withColor(Formatting.DARK_GREEN)
                    .withBold(true);
            MutableText toggle = Text.of(canTeleport ? " [ENABLED]" : " [DISABLED]").copy()
                    .setStyle(canTeleport ? activateStyle : deactivateStyle);

            minecraft.player.sendMessage(Text.of("Teleport Detect").copy().append(toggle), true);
        }

        // Calls the function to detect teleportation
        if (canTeleport) {
            try {
                is_Teleported();
            } catch (AWTException | InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void is_Teleported() throws AWTException, InterruptedException, IOException {
        // We are checking this because after exiting it's so fast that it still manages to call this function and the game crashes right there
        if (minecraft.player == null) {
            return;
        }
        Vec3d curPos = minecraft.player.getPos();
        if (prevPos != null && !prevPos.equals(curPos)) {
            // Checking difference
            double dx = curPos.getX() - prevPos.getX();
            double dy = curPos.getY() - prevPos.getY();
            double dz = curPos.getZ() - prevPos.getZ();
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist > 0.1) {
                ni9logic.LOGGER.info("You were teleported to " + curPos);

                Thread.sleep(1500);
                // Type a message to exit the game
                TypeitMessage.exit_game("Why would you do that man, clear abuse. Am out this is shit");
                Thread.sleep(1000);
                // Exits the game without message
                TypeitMessage.exit_game();

                canTeleport = !canTeleport;
            }
        }
        prevPos = curPos;
    }
}