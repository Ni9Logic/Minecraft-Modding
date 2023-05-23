package net.ni9logic.ni9logictmod.features.chatgames;

import net.ni9logic.ni9logictmod.ni9logic;
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

import static net.ni9logic.ni9logictmod.features.OptionsScreen.chatGames;

public class Scramble {
    private static final Map<String, String> UnscrambleScramble = new HashMap<>();
    private static final String PATHH = "C:\\Users\\Rakhman Gul\\Desktop\\Chat-Coords-main\\src\\main\\java\\net\\ni9logic\\ni9logictmod\\features\\chatgames\\utils\\scramble-data.txt";

    private static String Unscramble;


    public static void playScramble(String message) {
        if (chatGames) {
            if (message.contains("SCRAMBLE » ") && !message.contains("SCRAMBLE » The answer ") && !message.contains("won the game")) {
                Pattern pattern = Pattern.compile("SCRAMBLE » Unscramble the word (\\w+) to win");
                Matcher matcher = pattern.matcher(message);

                if (matcher.find()) {
                    ni9logic.LOGGER.warn("[SCRAMBLE-GAME] » Word detected, checking the word inside text file");
                    Unscramble = matcher.group(1);
                    if (UnscrambleScramble.containsKey(Unscramble)) {
                        ni9logic.LOGGER.warn("[SCRAMBLE-GAME] » Word found inside text file");
                        sendAnswer.inputAnswer(Unscramble);
                    }
                }
            } else if (message.contains("SCRAMBLE » The answer ")) {
                ni9logic.LOGGER.warn("[SCRAMBLE-GAME] » Word not found inside text file, hence adding the answer and the word in text file");
                addUnscrambleScramble(message);
            }
        }
    }

    public static void addUnscrambleScramble(String message) {
        Pattern answerPattern = Pattern.compile("SCRAMBLE » The answer was (\\w+)");
        Matcher matcher = answerPattern.matcher(message);
        if (matcher.find() && !UnscrambleScramble.containsKey(Unscramble)) {
            ni9logic.LOGGER.warn("[SCRAMBLE-GAME] » Answer detected but the answer is not present in the text file");
            String Scramble = matcher.group(1);
            updateHash_and_TextFile(Unscramble, Scramble);
        }
    }

    public static void readyMap() {
        // Path to our data
        Path path = Paths.get(PATHH);

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
            ni9logic.LOGGER.warn("[SCRAMBLE-GAME] » Extracted data from scramble-data.txt into the hashmap");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateHash_and_TextFile(String Unscramble, String Scramble) {
        Path path = Paths.get(PATHH);

        // Appending the new question and answer to the file
        try {
            ni9logic.LOGGER.warn("[SCRAMBLE-GAME] » Answer & Word added in the text file");
            Files.write(path, (Unscramble + ":" + Scramble + "\n").getBytes(), StandardOpenOption.APPEND);
            // Updating the map as well with the new question and answer
            UnscrambleScramble.put(Unscramble, Scramble);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ni9logic.LOGGER.warn("[SCRAMBLE-GAME] » Answer added in the hashmap");
    }
}
