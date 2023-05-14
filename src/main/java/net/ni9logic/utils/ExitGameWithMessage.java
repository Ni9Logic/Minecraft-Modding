package net.ni9logic.utils;

import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ExitGameWithMessage {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void exit_game_with_message(String Message) {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        MyScreens.sendMyChatMessage(Message, 2000);

        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);

        for (int i = 0; i < 8; i++) {
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(300);
        }

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }
}
