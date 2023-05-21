package net.ni9logic.utils;

import com.sun.jna.platform.KeyboardUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.ni9logic.ni9logictmod.ni9logic;
import org.lwjgl.glfw.GLFW;

public class sendAnswer {
    public static void inputAnswer(String answer) {
        ni9logic.LOGGER.info("Input method started");
        long startTime = System.currentTimeMillis();

        while (true) {
            assert MinecraftClient.getInstance().player != null;


            Style timer_up = Style.EMPTY
                    .withColor(Formatting.DARK_RED)
                    .withBold(true);

            StopWatch.myTimer("TIMER", startTime);


            if (StopWatch.elapsedSeconds >= 10) {
                ni9logic.LOGGER.info("INPUT ANSWER - Time was up the user didn't press P");
                MinecraftClient.getInstance().player.sendMessage(Text.of("TIME UP!").copy().setStyle(timer_up), true);
                break;
            }

            if (KeyboardUtils.isPressed(GLFW.GLFW_KEY_P)) {
                ni9logic.LOGGER.info("INPUT ANSWER - The user pressed P");
                MinecraftClient.getInstance().player.networkHandler.sendChatMessage(answer);

                // Sleeps for 1 second to avoid spam
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }
}
