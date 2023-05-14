package net.mrboogybam.chatcoords;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChatCoordsClient implements ClientModInitializer {

    // Key binds
    private static KeyBinding KeyLockItem;

    // Booleans
    private static final boolean canTeleport = false;
    private static boolean isLockItem = false;

    // Variables
    public static String target_item = "";
    public static Vec3d prevPos = null;

    // One and only
    public static MinecraftClient minecraft = MinecraftClient.getInstance();

    // Executors
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public static String getItemNameInMainHand() {
        if (minecraft.player == null) {
            return "";
        }
        ItemStack itemStack = minecraft.player.getMainHandStack();
        return itemStack.getName().getString();
    }

    @Override
    public void onInitializeClient() {

        // Basically when the client turns on
        System.setProperty("java.awt.headless", "false");
        // Register the keybindings without adding them to the keybind menu
        TeleportDetect.KeyTeleportDetect = new KeyBinding("key.teleport_detector", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.category.coords_by_logic");
        AutoClicker.KeyAutoClicker = new KeyBinding("key.auto_clicker", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.category.coords_by_logic");
        KeyLockItem = new KeyBinding("key.lock_hand_item", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.category.coords_by_logic");

        // Refreshes the client on every little single update
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            executor.submit(this::handleLockHandItem);
            executor.submit(this::handleTeleportDetect);
            executor.submit(this::handleAutoClicker);
        });
    }

    public void handleLockHandItem() {
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
            assert MinecraftClient.getInstance().player != null;
            PlayerInventory inventory = MinecraftClient.getInstance().player.getInventory();
            if (!getItemNameInMainHand().equals(target_item)) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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
        }
    }

    public void handleTeleportDetect() {
        TeleportDetect.detectTp();
    }

    public void handleAutoClicker() {
        if (AutoClicker.KeyAutoClicker.wasPressed()) {
            AutoClicker.canAutoClick = !AutoClicker.canAutoClick; // toggle the sending on/off
            if (AutoClicker.canAutoClick) {
                AutoClicker.Send_Enable_Auto_Clicker_Text();
            } else {
                AutoClicker.Send_Disable_Auto_Clicker_Text();
            }
        }

        // Calls the function to toggle auto clicking
        if (AutoClicker.canAutoClick && minecraft.currentScreen == null) {
            AutoClicker.AutoClick();
        }
    }
}