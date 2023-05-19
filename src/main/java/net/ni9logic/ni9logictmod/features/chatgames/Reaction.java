package net.ni9logic.ni9logictmod.features.chatgames;

import net.minecraft.client.MinecraftClient;
import net.ni9logic.utils.ChatMessagess;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reaction {
    public static boolean isReactionGameActive = false;

    public static void playReaction() {
        if (isReactionGameActive) {
            if (ChatMessagess.recentMessage != null && ChatMessagess.recentMessage.contains("REACTION » ")) {
                Pattern pattern = Pattern.compile("REACTION » First to type (\\w+) in the chat wins");
                Matcher matcher = pattern.matcher(ChatMessagess.recentMessage);
                if (matcher.find()) {
                    String word = matcher.group(1);
                    // Sends the words as soon as it gets it
                    assert MinecraftClient.getInstance().player != null;

                    // Randomly answers on different intervals because it's super-fast otherwise. It'll solve it in like 0.1 seconds.
                    Random random = new Random();
                    int minDelay = 1200; // 1.2 seconds in milliseconds
                    int maxDelay = 2300; // 2.3 seconds in milliseconds

                    int randomDelay = random.nextInt(maxDelay - minDelay + 1) + minDelay;
                    try {
                        Thread.sleep(randomDelay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    MinecraftClient.getInstance().player.networkHandler.sendChatMessage(word);

                    // Setting it back to null because it'll start spamming otherwise
                    ChatMessagess.recentMessage = null;
                }
            }
        }
    }
}
