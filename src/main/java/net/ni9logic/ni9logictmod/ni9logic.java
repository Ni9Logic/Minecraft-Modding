package net.ni9logic.ni9logictmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.ni9logic.utils.ChatMessagess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ni9logic implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger("ni9logic");

    @Override
    public void onInitialize() {
        System.setProperty("java.awt.headless", "false");

        ServerMessageEvents.CHAT_MESSAGE.register(
                (message, sender, params) -> {
                    ChatMessagess.recentMessage = message.getContent().getString();
                    System.out.println(ChatMessagess.recentMessage);
                });

        ServerMessageEvents.GAME_MESSAGE.register((server, message, overlay) -> {
            ChatMessagess.recentMessage = message.getContent().toString();
            System.out.println(ChatMessagess.recentMessage);
        });
    }
}
