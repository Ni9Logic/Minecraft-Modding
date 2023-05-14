package net.mrboogybam.chatcoords;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

import static net.mrboogybam.chatcoords.ChatCoordsClient.minecraft;

public class TeleportDetect {
    public static KeyBinding KeyTeleportDetect;
    private static boolean canTeleport = false;
    private static Vec3d prevPos = null;

    public static void detectTp() {
        if (KeyTeleportDetect.wasPressed()) {
            canTeleport = !canTeleport; // toggle the sending on/off
            if (canTeleport) {
                prevPos = null;
                assert minecraft.player != null;
                minecraft.player.sendMessage(Text.of("Don't move teleport detect enabled")
                        .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("red"))), true);
            } else {
                prevPos = null;
                assert minecraft.player != null;
                minecraft.player.sendMessage(Text.of("Free to move teleport detect disabled")
                        .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))), true);
            }
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
        if (MinecraftClient.getInstance().player == null) {
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
                minecraft.execute(() -> minecraft.setScreen(new ChatScreen("??, oh cmon' this is literally abuse? seriously?? am out man")));
                Robot robot = new Robot();
                robot.delay(1500);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

                robot.keyPress(KeyEvent.VK_ESCAPE);
                robot.keyRelease(KeyEvent.VK_ESCAPE);

                for (int i = 0; i < 8; i++) {
                    robot.keyPress(KeyEvent.VK_TAB);
                    robot.keyRelease(KeyEvent.VK_TAB);
                    robot.delay(300);
                }

                canTeleport = !canTeleport;
            }
        }
        prevPos = curPos;
    }


}
