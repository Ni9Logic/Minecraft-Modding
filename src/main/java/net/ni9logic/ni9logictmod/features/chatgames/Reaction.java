package net.ni9logic.ni9logictmod.features.chatgames;

import com.sun.jna.platform.KeyboardUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.ni9logic.utils.StopWatch;
import org.lwjgl.glfw.GLFW;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reaction {
    public static boolean isReactionGameActive = false;

    public static void playReaction(String message) {
        if (isReactionGameActive) {
            Pattern pattern = Pattern.compile("REACTION » First to type (\\w+) in the chat wins");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String word = matcher.group(1);
                // Sends the words as soon as it gets it
                assert MinecraftClient.getInstance().player != null;

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
                        MinecraftClient.getInstance().player.networkHandler.sendChatMessage(word);
                        break;
                    }
                }
            }
        }
    }
}
