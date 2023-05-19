package net.ni9logic.ni9logictmod.features.chatgames;

import net.minecraft.client.MinecraftClient;
import net.ni9logic.utils.ChatMessagess;
import net.ni9logic.utils.MathEval;

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


                    // Sends the result in the chat
                    minecraft.player.networkHandler.sendChatMessage(String.valueOf(getRes(mathExpression)));
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
