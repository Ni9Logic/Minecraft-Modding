package net.ni9logic.ni9logictmod.features.chatgames;

import net.ni9logic.utils.sendAnswer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scramble {
    public static boolean isScrambleGameActive = true;
    private static final Map<String, String> UnscrambleScramble = new HashMap<>();

    private static String Unscramble;


    public static void playScramble(String message) {
        if (isScrambleGameActive) {
            Pattern pattern = Pattern.compile("SCRAMBLE » Unscramble the word (\\w+) to win");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                Unscramble = matcher.group(1);
                if (UnscrambleScramble.containsKey(Unscramble)) {
                    sendAnswer.inputAnswer(Unscramble);
                }
            }

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Pattern answerPattern = Pattern.compile("SCRAMBLE » The answer was (\\w+)");
            matcher = pattern.matcher(message);
            if (matcher.find() && !UnscrambleScramble.containsKey(Unscramble)) {
                String Scramble = matcher.group(1);
                addUnscrambleScramble(Unscramble, Scramble);
            }
        }
    }

    public static void readyMap() {
        // Path to our data
        Path path = Paths.get("src/net/ni9logic/ni9logictmod/features/chatgames/utils/scrmable-data.txt");

        try {
            // Reading all the lines from the file
            List<String> lines = Files.readAllLines(path);

            // For each line splitting it into question and answers
            for (String line : lines) {
                String[] parts = line.split(":");
                String Unscramble = parts[0];
                String Scramble = parts[1];

                // Inserting all the question and answers into the hash map for faster future get
                UnscrambleScramble.put(Unscramble, Scramble);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addUnscrambleScramble(String Unscramble, String Scramble) {
        Path path = Paths.get("src/net/ni9logic/ni9logictmod/features/chatgames/utils/trivia-data.txt");

        // Appending the new question and answer to the file
        try {
            Files.write(path, (Unscramble + ":" + Scramble + "\n").getBytes(), StandardOpenOption.APPEND);
            // Updating the map as well with the new question and answer
            UnscrambleScramble.put(Unscramble, Scramble);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
