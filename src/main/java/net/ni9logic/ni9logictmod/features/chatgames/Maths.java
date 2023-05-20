package net.ni9logic.ni9logictmod.features.chatgames;

import com.sun.jna.platform.KeyboardUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.ni9logic.utils.ChatMessagess;
import net.ni9logic.utils.MathEval;
import net.ni9logic.utils.StopWatch;
import org.lwjgl.glfw.GLFW;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Maths {
    public static boolean isMathGameActive = false;
    static MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void playMath() {
        assert minecraft.player != null;

        if (isMathGameActive) {
            if (ChatMessagess.recentMessage.contains("MATH » ")) {
                Pattern pattern = Pattern.compile("MATH » (.*) = \\?");
                Matcher matcher = pattern.matcher(ChatMessagess.recentMessage);
                if (matcher.find()) {
                    String mathExpression = matcher.group(1); // This will contain something like "2 + 3 / 6 * 6 - 100"

                    long startTime = System.currentTimeMillis();

                    while (true) {
                        assert MinecraftClient.getInstance().player != null;


                        Style timer_up = Style.EMPTY
                                .withColor(Formatting.DARK_RED)
                                .withBold(true);

                        StopWatch.myTimer("TIMER", startTime);


                        if (StopWatch.elapsedSeconds >= 10) {
                            MinecraftClient.getInstance().player.sendMessage(Text.of("Time up!").copy().setStyle(timer_up), true);
                            break;
                        }

                        if (KeyboardUtils.isPressed(GLFW.GLFW_KEY_P)) {
                            minecraft.player.networkHandler.sendChatMessage(String.valueOf(getRes(mathExpression)));
                            ChatMessagess.recentMessage = "";
                            break;
                        }
                    }

                    // Sends the result in the chat
                    ChatMessagess.recentMessage = "";
                }
            }
        }

    }

    public static int getRes(String expr) {
        MathEval solver = new MathEval();
        return Math.toIntExact(Math.round(solver.evaluate(expr)));
    }
}
