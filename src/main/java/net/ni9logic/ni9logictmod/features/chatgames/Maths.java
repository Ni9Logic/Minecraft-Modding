package net.ni9logic.ni9logictmod.features.chatgames;

import net.ni9logic.utils.ChatMessagess;
import net.ni9logic.utils.ExpressionEvaluator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Maths {
    public static boolean isMathGameActive = false;

    public static void playMath() {
        if (isMathGameActive) {
            if (ChatMessagess.recentMessage.contains("MATH » ")) {
                Pattern pattern = Pattern.compile("MATH » (.*) = \\?");
                Matcher matcher = pattern.matcher(ChatMessagess.recentMessage);
                if (matcher.find()) {
                    String mathExpression = matcher.group(1); // This will contain something like "2 + 3 / 6 * 6 - 100"
                    double res = Math.round(ExpressionEvaluator.evaluateExpression(mathExpression));
                    System.out.println(res);

                    ChatMessagess.recentMessage = "";
                }
            }
        }
    }
}
