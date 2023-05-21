package net.ni9logic.ni9logictmod.features.chatgames;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scramble {
    public static boolean isScrambleGameActive = true;

    public static void playScramble(String message) {
        if (isScrambleGameActive) {
            Pattern pattern = Pattern.compile("SCRAMBLE » Unscramble the word (\\w+) to win");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                String word = matcher.group(1);
                System.out.println("Word to unscramble is: " + word);
            }
        }
    }
}
