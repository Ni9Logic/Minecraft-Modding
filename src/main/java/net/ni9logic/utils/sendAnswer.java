package net.ni9logic.utils;

import com.sun.jna.platform.KeyboardUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class sendAnswer {
    public static void inputAnswer(String answer) {
        long startTime = System.currentTimeMillis();

        while (true) {
            assert MinecraftClient.getInstance().player != null;


            Style timer_up = Style.EMPTY
                    .withColor(Formatting.DARK_RED)
                    .withBold(true);

            StopWatch.myTimer("TIMER", startTime);


            if (StopWatch.elapsedSeconds >= 10) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("TIME UP!").copy().setStyle(timer_up), true);
                break;
            }

            if (KeyboardUtils.isPressed(GLFW.GLFW_KEY_P)) {
                MinecraftClient.getInstance().player.networkHandler.sendChatMessage(answer);
                break;
            }
        }
    }
}
