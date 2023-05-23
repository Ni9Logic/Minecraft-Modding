package net.ni9logic.ni9logictmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.ni9logic.ni9logictmod.features.AutoReply;
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

            // Executing chat games in another thread so that they won't hang the whole minecraft
            executorService.submit(() -> {
                Maths.playMath(message.getString());
                Trivia.playTrivia(message.getString());
                Reaction.playReaction(message.getString());
                Scramble.playScramble(message.getString());
                AutoReply.playAutoReply(message.getString());
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
