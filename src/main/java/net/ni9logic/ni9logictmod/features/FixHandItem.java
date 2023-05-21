package net.ni9logic.ni9logictmod.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class FixHandItem {
    public static boolean isFixHandItem = false;

    public static void handleFixHandItem() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        assert minecraft.player != null;
        ItemStack heldItem = minecraft.player.getStackInHand(Hand.MAIN_HAND);
        // Perform operations with the held item as needed
        if (isFixHandItem && heldItem.isDamageable()) {
            int itemHealth = getItemHealth(heldItem);
            if (itemHealth <= 70) {
                minecraft.player.networkHandler.sendChatCommand("fix");

                // Give a gap because it will spam otherwise
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static int getItemHealth(ItemStack itemStack) {
        // Calculate and return the item health/durability
        return itemStack.getMaxDamage() - itemStack.getDamage();
    }
}
