package net.ni9logic.ni9logictmod.features.chatgames;

import net.ni9logic.ni9logictmod.ni9logic;
import net.ni9logic.utils.sendAnswer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.ni9logic.ni9logictmod.features.OptionsScreen.chatGames;

public class Reaction {

    public static void playReaction(String message) {
        if (chatGames) {
            if (message.contains("REACTION » ") && !message.contains("REACTION » The answer ") && !message.contains("won the game")) {
                Pattern pattern = Pattern.compile("REACTION » First to type (\\w+) in the chat wins");
                Matcher matcher = pattern.matcher(message);
                if (matcher.find()) {
                    String word = matcher.group(1);

                    // Sends the words as soon as user presses p
                    ni9logic.LOGGER.warn("[REACTION-GAME] » Word detected, calling sendAnswer.inputAnswer function");
                    sendAnswer.inputAnswer(word);
                }
            }
        }
    }
}
