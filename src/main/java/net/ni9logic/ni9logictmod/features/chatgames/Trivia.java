package net.ni9logic.ni9logictmod.features.chatgames;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trivia {
    public static boolean isTriviaGameActive = true;

    public static void playTrivia(String message) {
        if (isTriviaGameActive) {
            Pattern pattern = Pattern.compile("TRIVIA » (.*)");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                String question = matcher.group(1);
                System.out.println("Question is: " + question);
            }
        }
    }
}
