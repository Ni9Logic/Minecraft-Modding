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

public class Trivia {
    private static final Map<String, String> questionAnswer = new HashMap<>();
    private static final String PATHH = "C:\\Users\\Rakhman Gul\\Desktop\\Chat-Coords-main\\src\\main\\java\\net\\ni9logic\\ni9logictmod\\features\\chatgames\\utils\\trivia-data.txt";
    private static String question;

    public static void playTrivia(String message) {
        if (chatGames) {
            if (message.contains("TRIVIA » ") && !message.contains("TRIVIA » The answer ") && !message.contains("won the game")) {
                Pattern pattern = Pattern.compile("TRIVIA » (.*)");
                Matcher matcher = pattern.matcher(message);

                if (matcher.find()) {
                    ni9logic.LOGGER.warn("[TRIVIA-GAME] » Trivia detected, checking the trivia inside text file");
                    question = matcher.group(1);
                    if (questionAnswer.containsKey(question)) {
                        ni9logic.LOGGER.warn("[TRIVIA-GAME] » Trivia found inside text file");
                        sendAnswer.inputAnswer(questionAnswer.get(question)); // Basically this is our answer to trivia if its present inside the hashmap
                    }
                }
            } else if (message.contains("TRIVIA » The answer ")) {
                ni9logic.LOGGER.warn("[TRIVIA-GAME] » Trivia not found inside text file, hence adding the trivia and the answer in text file");
                addAnswer(message);
            }
        }
    }

    public static void addAnswer(String message) {

        if (!questionAnswer.containsKey(question)) {
            Pattern answerPattern = Pattern.compile("TRIVIA » The answer was (\\w+)");
            Matcher matcher = answerPattern.matcher(message);

            if (matcher.find()) {
                String answer = matcher.group(1);
                // This updates the file and the hashmap as well for next
                updateHash_and_TextFile(question, answer);
            }
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
                String question = parts[0];
                String answer = parts[1];

                // Inserting all the question and answers into the hash map for faster future get
                questionAnswer.put(question, answer);
            }

            ni9logic.LOGGER.warn("[TRIVIA-GAME] » Extracted data from trivia-data.txt into the hashmap");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void updateHash_and_TextFile(String question, String answer) {
        Path path = Paths.get(PATHH);

        // Appending the new question and answer to the file
        try {
            ni9logic.LOGGER.warn("[TRIVIA-GAME] » Trivia & Answer added in the text file");
            Files.write(path, (question + ":" + answer + "\n").getBytes(), StandardOpenOption.APPEND);
            // Updating the map as well with the new question and answer
            questionAnswer.put(question, answer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ni9logic.LOGGER.warn("[SCRAMBLE-GAME] » Answer added in the hashmap");
    }

}
