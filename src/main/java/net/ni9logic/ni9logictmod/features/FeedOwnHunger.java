package net.ni9logic.ni9logictmod.features;

import net.minecraft.client.MinecraftClient;

public class FeedOwnHunger {
    static MinecraftClient minecraft = MinecraftClient.getInstance();
    public static boolean isAutoFeed = false;

    public static void handleHunger() {
        if (isAutoFeed) {
            if (minecraft.player != null) {
                int myFoodLevel = minecraft.player.getHungerManager().getFoodLevel();
                if (myFoodLevel <= 8) {
                    minecraft.player.networkHandler.sendChatCommand("feed");
                }
            }
        }
    }
}
