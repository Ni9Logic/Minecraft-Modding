package net.ni9logic.ni9logictmod.features.chatgames;

import net.ni9logic.utils.sendAnswer;

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

                // Sends the words as soon as user presses p
                sendAnswer.inputAnswer(word);
            }
        }
    }
}
