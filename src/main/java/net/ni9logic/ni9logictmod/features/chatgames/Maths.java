package net.ni9logic.ni9logictmod.features.chatgames;

import net.minecraft.client.MinecraftClient;
import net.ni9logic.ni9logictmod.ni9logic;
import net.ni9logic.utils.MathEval;
import net.ni9logic.utils.sendAnswer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.ni9logic.ni9logictmod.features.OptionsScreen.chatGames;

public class Maths {
    static MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void playMath(String message) {
        assert minecraft.player != null;

        if (chatGames) {
            if (message.contains("MATH » ") && !message.contains("MATH » The answer ") && !message.contains("won the game")) {
                Pattern pattern = Pattern.compile("MATH » (.*) = \\?");
                Matcher matcher = pattern.matcher(message);
                if (matcher.find()) {
                    String mathAnswer = String.valueOf(getRes(matcher.group(1))); // This directly extracts our answer from the expression

                    // Sending Answer
                    ni9logic.LOGGER.warn("[MATH-GAME] » Expression detected, calling sendAnswer.inputAnswer function");
                    sendAnswer.inputAnswer(mathAnswer);
                }
            }
        }
    }


    public static int getRes(String expr) {
        MathEval solver = new MathEval();
        return Math.toIntExact(Math.round(solver.evaluate(expr)));
    }
}
