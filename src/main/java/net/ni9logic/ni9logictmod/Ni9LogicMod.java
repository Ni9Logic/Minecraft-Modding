package net.ni9logic.ni9logictmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.ni9logic.ni9logictmod.features.*;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Ni9LogicMod implements ClientModInitializer {
    public static final MinecraftClient minecraft = MinecraftClient.getInstance();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    @Override
    public void onInitializeClient() {
        // Shuts down the threads to avoid resource leakage
        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdownNow));

        // Basically when the client turns on
        System.setProperty("java.awt.headless", "false");

        // Register the keybindings without adding them to the key bind menu of minecraft
        TeleportDetect.KeyTeleportDetect = new KeyBinding("key.teleport_detector", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.category.coords_by_logic");
        AutoClicker.KeyAutoClicker = new KeyBinding("key.auto_clicker", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.category.coords_by_logic");
        LockItemInHand.KeyLockItem = new KeyBinding("key.lock_hand_item", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.category.coords_by_logic");
        RotationDetect.KeyRotationDetect = new KeyBinding("key.detect_rotation", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.category.coords_by_logic");
        OptionsScreen.GUI_KEY = new KeyBinding("key.GUI", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.category.coords_by_logic");

        // Refreshes the client on every little single update
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            executor.submit(this::handleLockHandItem);
            executor.submit(this::handleTeleportDetect);
            executor.submit(this::handleAutoClicker);
            executor.submit(this::handleRotationDetect);
            handleGUI();
        });
    }

    public void handleLockHandItem() {
        LockItemInHand.handleLIH();
    }

    public void handleTeleportDetect() {
        TeleportDetect.detectTp();
    }

    public void handleAutoClicker() {
        AutoClicker.handleAC();
    }

    public void handleRotationDetect() {
        RotationDetect.detectRotation();
    }

    public void handleGUI() {
        while (OptionsScreen.GUI_KEY.wasPressed()) {
            minecraft.execute(() -> minecraft.setScreen(new OptionsScreen()));
        }
    }

}