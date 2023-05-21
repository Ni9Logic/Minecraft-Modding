package net.ni9logic.ni9logictmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.ni9logic.ni9logictmod.features.chatgames.Maths;
import net.ni9logic.ni9logictmod.features.chatgames.Reaction;
import net.ni9logic.ni9logictmod.features.chatgames.Scramble;
import net.ni9logic.ni9logictmod.features.chatgames.Trivia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ni9logic implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("ni9logic");
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void onInitialize() {

        assert MinecraftClient.getInstance().player != null;
        Trivia.readyMap();
        Scramble.readyMap();

        // Register event handlers for the ClientReceiveMessageEvents
        ClientReceiveMessageEvents.MODIFY_GAME.register((message, overlay) -> {
            // Modify the received game message
            assert message != null;
            String chatMessage = message.getString();

            executorService.submit(() -> {
                // Logging in chat Games
                if (chatMessage.contains("MATH » ")) {
                    Maths.playMath(chatMessage);
                } else if (chatMessage.contains("REACTION » ")) {
                    Reaction.playReaction(chatMessage);
                } else if (chatMessage.contains("SCRAMBLE » ")) {
                    Scramble.playScramble(chatMessage);
                } else if (chatMessage.contains("TRIVIA » ")) {
                    Trivia.playTrivia(chatMessage);
                }
            });

            return message; // Return the modified message
        });

        ClientReceiveMessageEvents.CHAT.register(((message, signedMessage, sender, params, receptionTimestamp) ->
        {
            assert message != null;
            System.out.println("Chat-Game: " + message.getString());
        }));
        System.setProperty("java.awt.headless", "false");
    }
}
