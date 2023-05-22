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

public class Scramble {
    public static boolean isScrambleGameActive = true;
    private static final Map<String, String> UnscrambleScramble = new HashMap<>();
    private static final String PATHH = "C:\\Users\\Rakhman Gul\\Desktop\\Chat-Coords-main\\src\\main\\java\\net\\ni9logic\\ni9logictmod\\features\\chatgames\\utils\\scramble-data.txt";

    private static String Unscramble;


    public static void playScramble(String message) {
        if (isScrambleGameActive) {
            if (message.contains("SCRAMBLE » ") && !message.contains("SCRAMBLE » The answer ") && !message.contains("won the game")) {
                Pattern pattern = Pattern.compile("SCRAMBLE » Unscramble the word (\\w+) to win");
                Matcher matcher = pattern.matcher(message);

                if (matcher.find()) {
                    ni9logic.LOGGER.info("SCRAMBLE-GAME - Scramble Game Found heading towards input method");
                    Unscramble = matcher.group(1);
                    if (UnscrambleScramble.containsKey(Unscramble)) {
                        sendAnswer.inputAnswer(Unscramble);
                    }
                }
            } else if (message.contains("SCRAMBLE » The answer ")) {
                addUnscrambleScramble(message);
            }
        }
    }

    public static void addUnscrambleScramble(String message) {
        ni9logic.LOGGER.info("SCRAMBLE-GAME - Found answer for the scramble and the question was also not found in hashmap");
        Pattern answerPattern = Pattern.compile("SCRAMBLE » The answer was (\\w+)");
        Matcher matcher = answerPattern.matcher(message);
        if (matcher.find() && !UnscrambleScramble.containsKey(Unscramble)) {
            ni9logic.LOGGER.info("SCRAMBLE-GAME - Scramble game word was not found in scramble-data.txt hence adding...");
            String Scramble = matcher.group(1);
            addUnscrambleScramble(Unscramble, Scramble);
            ni9logic.LOGGER.info("Added the answer in the hashmap");
        }
    }

    public static void readyMap() {
        ni9logic.LOGGER.info("SCRAMBLE-GAME - Extracting data from scramble-data.txt into the hashmap...");
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addUnscrambleScramble(String Unscramble, String Scramble) {
        ni9logic.LOGGER.info("SCRAMBLE-GAME - Adding the question and the answer into the text file as well as into the hashmap");
        Path path = Paths.get(PATHH);

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
