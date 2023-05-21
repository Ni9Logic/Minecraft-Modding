package net.ni9logic.ni9logictmod.features.chatgames;

import net.minecraft.client.MinecraftClient;
import net.ni9logic.ni9logictmod.ni9logic;
import net.ni9logic.utils.MathEval;
import net.ni9logic.utils.sendAnswer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Maths {
    public static boolean isMathGameActive = false;
    static MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void playMath(String message) {
        assert minecraft.player != null;

        if (isMathGameActive) {
            Pattern pattern = Pattern.compile("MATH » (.*) = \\?");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                ni9logic.LOGGER.info("MATH-GAME - Math game found in the cat... heading towards input method");
                String mathAnswer = String.valueOf(getRes(matcher.group(1))); // This directly extracts our answer from the expression

                // Sending Answer
                sendAnswer.inputAnswer(mathAnswer);
            }
        }
    }


    public static int getRes(String expr) {
        MathEval solver = new MathEval();
        return Math.toIntExact(Math.round(solver.evaluate(expr)));
    }
}
