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
        if (isFixHandItem) {
            int itemHealth = getItemHealth(heldItem);
            if (itemHealth <= 70) {
                minecraft.player.networkHandler.sendChatCommand("/fix");
            }
        }
    }

    private static int getItemHealth(ItemStack itemStack) {
        // Calculate and return the item health/durability
        return itemStack.getMaxDamage() - itemStack.getDamage();
    }
}
