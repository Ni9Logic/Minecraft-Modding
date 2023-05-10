package net.mrboogybam.chatcoords;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ChatCoordsClient implements ClientModInitializer {

    public static final String LOG_PATH = "C:\\Users\\Rakhman Gul\\AppData\\Roaming\\.minecraft\\logs\\latest.log";

    private static KeyBinding Keybinding1;
    private static KeyBinding Keybinding2;
    private boolean canSendCoords = false;
    public static Vec3d prevPos = null;

    public static String logFile() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void clearChatLog() {
        try {
            FileWriter fw = new FileWriter(LOG_PATH);
            fw.write("");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void is_Teleported() throws AWTException, InterruptedException {
        // We are checking this because after exiting it's so fast that it still manages to call this function and the game crashes right there
        if (MinecraftClient.getInstance().player == null) {
            return;
        }
        Vec3d curPos = MinecraftClient.getInstance().player.getPos();
        if (prevPos != null && !prevPos.equals(curPos)) {
            // Checking difference
            double dx = curPos.getX() - prevPos.getX();
            double dy = curPos.getY() - prevPos.getY();
            double dz = curPos.getZ() - prevPos.getZ();
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (dist > 0.1) {
                Robot robot = new Robot();

                robot.keyPress(KeyEvent.VK_ESCAPE);
                robot.keyRelease(KeyEvent.VK_ESCAPE);

                for (int i = 0; i < 8; i++) {
                    robot.keyPress(KeyEvent.VK_TAB);
                    robot.keyRelease(KeyEvent.VK_TAB);
                }

                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        }
        prevPos = curPos;
    }

    public static void is_HandItem() {
        // TODO We have to check that if the current item in our hand is not a sword, we just need to leave or exit the game.
    }

    @Override
    public void onInitializeClient() {

        // Basically when the client turns on
        System.setProperty("java.awt.headless", "false");
        Keybinding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Teleport Detector",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "Coords by LOGIC"
        ));
        Keybinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Load Math",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "Coords by LOGIC"
        ));

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, isDedicated) -> {
        });


        // Refreshes the client on every little single update
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // If N is pressed
            if (Keybinding1.wasPressed()) {
                canSendCoords = !canSendCoords; // toggle the sending on/off
                if (canSendCoords) {
                    prevPos = null;
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(Text.of("Don't move teleport detect enabled")
                            .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("red"))));
                } else {
                    prevPos = null;
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(Text.of("Free to move teleport detect disabled")
                            .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))));
                }
            }

            // Calls the function to update co-ordinates
            if (canSendCoords) {
                try {
                    is_Teleported();
                } catch (AWTException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            if (Keybinding2.wasPressed()) {
                String line = "---------------------------------------------";
                String msg = "MATH » 2 + 3 + 100 * 5 = ?";

                assert MinecraftClient.getInstance().player != null;
                MinecraftClient.getInstance().player.sendMessage(Text.of(line));
                MinecraftClient.getInstance().player.sendMessage(Text.of(msg));
                MinecraftClient.getInstance().player.sendMessage(Text.of(line));
            }
        });

    }
}