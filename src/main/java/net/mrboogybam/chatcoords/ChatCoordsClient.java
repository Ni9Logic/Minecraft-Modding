package net.mrboogybam.chatcoords;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.IOException;

public class ChatCoordsClient implements ClientModInitializer {

    // Key binds
    private static KeyBinding KeyTeleportDetect;
    private static KeyBinding KeyAutoClicker;
    private static KeyBinding KeyLockItem;

    // Booleans
    private static boolean canTeleport = false;
    private boolean canAutoClick = false;
    boolean isScriptRunning = false;
    private static boolean isLockItem = false;

    // Variables
    private static String target_item = "";
    Process process = null;
    public static Vec3d prevPos = null;

    // One and only

    static MinecraftClient minecraft = MinecraftClient.getInstance();


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

                minecraft.setScreen(new ChatScreen("??, oh cmon' this is literally abuse? seriously?? am out man"));
                ProcessBuilder pb = new ProcessBuilder("python", "C:\\Users\\Rakhman Gul\\Desktop\\Programming\\Minecraft\\syss\\exit.py");
                pb.start();
                canTeleport = !canTeleport;
            }
        }
        prevPos = curPos;
    }

    public static String getItemNameInMainHand() {
        if (minecraft.player == null) {
            return "";
        }
        ItemStack itemStack = minecraft.player.getMainHandStack();
        return itemStack.getName().getString();
    } // Will be used later

    private ChatHud chatHud;

    @Override
    public void onInitializeClient() {


        // Basically when the client turns on
        System.setProperty("java.awt.headless", "false");
        // Register the keybindings without adding them to the keybind menu
        KeyTeleportDetect = new KeyBinding("key.teleport_detector", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.category.coords_by_logic");
        KeyAutoClicker = new KeyBinding("key.auto_clicker", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.category.coords_by_logic");
        KeyLockItem = new KeyBinding("key.lock_hand_item", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.category.coords_by_logic");

        // Refreshes the client on every little single update
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            chatHud = minecraft.inGameHud.getChatHud();
            // If B is pressed
            if (KeyLockItem.wasPressed()) {
                isLockItem = !isLockItem;
                if (isLockItem) {
                    target_item = getItemNameInMainHand();
                    assert minecraft.player != null;
                    minecraft.player.sendMessage(Text.of(target_item + " has been locked")
                            .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("red"))), true);
                } else {
                    assert minecraft.player != null;
                    minecraft.player.sendMessage(Text.of(target_item + " has been unlocked")
                            .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))), true);
                }
            }

            if (isLockItem) {
                assert minecraft.player != null;
                PlayerInventory inventory = minecraft.player.getInventory();
                for (int slot = 0; slot < inventory.size(); slot++) {
                    ItemStack itemStack = inventory.getStack(slot);
                    if (itemStack.getName().getString().equals(target_item)) {
                        ItemStack mainHandItem = inventory.getMainHandStack();
                        inventory.setStack(slot, mainHandItem);
                        inventory.setStack(inventory.selectedSlot, itemStack);
                        break;
                    }
                }
            }

            // If N is pressed
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


            if (KeyAutoClicker.wasPressed()) {
                canAutoClick = !canAutoClick; // toggle the sending on/off
                if (canAutoClick) {
                    assert minecraft.player != null;
                    minecraft.player.sendMessage(Text.of("Auto Clicker Enabled")
                            .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))), true);
                } else {
                    if (isScriptRunning && process != null && process.isAlive()) {
                        process.destroy();
                        process = null; // Reset the process variable
                        isScriptRunning = false; // Reset the flag since the script is no longer running
                    }
                    assert minecraft.player != null;
                    minecraft.player.sendMessage(Text.of("Auto Clicker Disabled")
                            .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("red"))), true);
                }
            }

            // Calls the function to toggle auto clicking
            if (canAutoClick && client.currentScreen == null) {

                HitResult rayTrace = client.crosshairTarget;

                if (rayTrace instanceof EntityHitResult) {
                    Entity entity = ((EntityHitResult) rayTrace).getEntity();
                    if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
                        if (!isScriptRunning && process == null) {
                            String ahkScriptPath = "C:\\Users\\Rakhman Gul\\AppData\\Roaming\\.minecraft\\mods\\AutoClicker.ahk";
                            String autohotkeyPath = "C:\\Program Files\\AutoHotkey\\AutoHotkey.exe";
                            ProcessBuilder processBuilder = new ProcessBuilder(autohotkeyPath, ahkScriptPath);
                            try {
                                process = processBuilder.start();
                                isScriptRunning = true; // Set the flag to indicate the script is running
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    // Terminate the subprocess if it was started and is still running
                    if (isScriptRunning && process != null && process.isAlive()) {
                        process.destroy();
                        process = null; // Reset the process variable
                        isScriptRunning = false; // Reset the flag since the script is no longer running
                    }
                }
            }
        });
    }
}