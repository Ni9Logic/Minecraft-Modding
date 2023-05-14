package net.ni9logic.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MyScreens {

    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void sendMyChatMessage(String Message, int delay) {
        if (minecraft.currentScreen == null) {
            minecraft.execute(() -> minecraft.setScreen(new ChatScreen(Message)));
            Robot robot = null;
            try {
                robot = new Robot();
                robot.delay(delay);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
