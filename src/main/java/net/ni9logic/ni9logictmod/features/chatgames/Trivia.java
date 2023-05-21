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

public class Trivia {
    public static boolean isTriviaGameActive = true;
    private static final Map<String, String> questionAnswer = new HashMap<>();

    private static String question;

    public static void playTrivia(String message) {
        if (isTriviaGameActive) {
            Pattern pattern = Pattern.compile("TRIVIA » (.*)");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {

                question = matcher.group(1);
                if (questionAnswer.containsKey(question)) {
                    sendAnswer.inputAnswer(questionAnswer.get(question)); // Basically this is our answer to trivia if its present inside the hashmap
                }
            }

            // Wait for the answer && sleep for 20 seconds to get the answer
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Pattern answerPattern = Pattern.compile("TRIVIA » The answer was (\\w+)");
            matcher = answerPattern.matcher(message);

            if (matcher.find() && !questionAnswer.containsKey(question)) {
                String answer = matcher.group(1);
                // This updates the file and the hashmap as well for next
                addQuestionAnswer(question, answer);
            }
        }
    }

    public static void readyMap() {
        // Path to our data
        Path path = Paths.get("src/net/ni9logic/ni9logictmod/features/chatgames/utils/trivia-data.txt");

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

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void addQuestionAnswer(String question, String answer) {
        Path path = Paths.get("src/net/ni9logic/ni9logictmod/features/chatgames/utils/trivia-data.txt");

        // Appending the new question and answer to the file
        try {
            Files.write(path, (question + ":" + answer + "\n").getBytes(), StandardOpenOption.APPEND);
            // Updating the map as well with the new question and answer
            questionAnswer.put(question, answer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
