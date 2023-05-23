package net.ni9logic.ni9logictmod.features;

import net.ni9logic.ni9logictmod.ni9logic;
import net.ni9logic.utils.TypeitMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoReply {

    public static void playAutoReply(String message) {
        if (OptionsScreen.chatGames) {
            if (message.contains(" -> me]")) {
                ni9logic.LOGGER.info("AUTO-REPLY - PM FOUND!");
                Pattern pattern = Pattern.compile("\\[([^\\]]+?) -> [^\\]]+]");
                Matcher matcher = pattern.matcher(message);

                if (matcher.find()) {
                    String personName = matcher.group(1);
                    ni9logic.LOGGER.info("AUTO-REPLY - PM FOUND! Data Extracted & Person name is: " + personName);
                    TypeitMessage.exit_game("/r " + personName + ", ??");
                } else {
                    ni9logic.LOGGER.info("AUTO-REPLY - PM FOUND! but data couldn't be extracted");
                }
            }
        }
    }
}
