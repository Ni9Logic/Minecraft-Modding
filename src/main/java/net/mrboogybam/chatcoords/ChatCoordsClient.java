package net.mrboogybam.chatcoords;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ChatCoordsClient implements ClientModInitializer {

    public static final String LOG_PATH = "C:\\Users\\Rakhman Gul\\Desktop\\Chat-Coords-main\\run\\logs\\latest.log";

    private static KeyBinding Keybinding1;
    private static KeyBinding Keybinding2;
    private boolean canSendCordinates = false;
    private boolean canAutoClick = false;
    private double lastClickTime = 0;
    public static Vec3d prevPos = null;

    public static String logFile() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void clearChatLog() {
        try (FileWriter fw = new FileWriter(LOG_PATH)) {
            fw.write("");
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

    public static String getItemNameInMainHand() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.player == null) {
            return "";
        }
        ItemStack itemStack = minecraftClient.player.getMainHandStack();
        return itemStack.getName().getString();
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
                "Auto Clicker",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "Coords by LOGIC"
        ));

        // Refreshes the client on every little single update
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // If N is pressed
            if (Keybinding1.wasPressed()) {
                canSendCordinates = !canSendCordinates; // toggle the sending on/off
                if (canSendCordinates) {
                    prevPos = null;
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(Text.of("Don't move teleport detect enabled")
                            .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("red"))), true);
                } else {
                    prevPos = null;
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(Text.of("Free to move teleport detect disabled")
                            .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))), true);
                }
            }

            // Calls the function to detect teleportation
            if (canSendCordinates) {
                try {
                    is_Teleported();
                } catch (AWTException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


            if (Keybinding2.wasPressed()) {
                canAutoClick = !canAutoClick; // toggle the sending on/off
                if (canAutoClick) {
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(Text.of("Auto Clicker Enabled")
                            .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))), true);
                } else {
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(Text.of("Auto Clicker Disabled")
                            .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("red"))), true);
                }
            }

            // Calls the function to toggle auto clicking
            if (canAutoClick) {
                Random rand = new Random();
                long currentTime = System.currentTimeMillis();
                double clickInterval = 50 + rand.nextDouble() * 250; // Random interval between 50 ms and 300 ms

                if (currentTime - lastClickTime >= clickInterval) {
                    lastClickTime = currentTime;

                    if (client.currentScreen == null) {
                        HitResult rayTrace = client.crosshairTarget;

                        if (rayTrace instanceof EntityHitResult) {
                            Entity entity = ((EntityHitResult) rayTrace).getEntity();
                            if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
                                client.options.attackKey.setPressed(true);
                                client.options.attackKey.wasPressed();
                                client.interactionManager.attackEntity(client.player, entity);
                            }
                        }
                    }
                } else {
                    client.options.attackKey.setPressed(false);
                }


//                try {
//                    AutoClicker.auto_click();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
            }
        });
    }
}