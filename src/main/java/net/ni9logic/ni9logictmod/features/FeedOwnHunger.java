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

                    /* I just got to know that we kinda use some kind of async function and await thing to wait for the
                    Give a gap because it will spam otherwise
                     to respond */
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
