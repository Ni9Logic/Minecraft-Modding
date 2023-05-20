package net.ni9logic.ni9logictmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.ni9logic.utils.ChatMessagess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ni9logic implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("ni9logic");

    @Override
    public void onInitialize() {

        // Register event handlers for the ClientReceiveMessageEvents
        ClientReceiveMessageEvents.MODIFY_GAME.register((message, overlay) -> {
            // Modify the received game message
            assert message != null;

            // Log the modified message outside the event handler
            ChatMessagess.recentMessage = message.getString();
            System.out.println("Modify-Game" + message.getString());

            return message; // Return the modified message
        });

        ClientReceiveMessageEvents.CHAT.register(((message, signedMessage, sender, params, receptionTimestamp) ->
        {
            assert message != null;
            ChatMessagess.recentMessage = message.getString();
            System.out.println("Chat-Game" + message.getString());
        }));

        System.setProperty("java.awt.headless", "false");
    }
}
