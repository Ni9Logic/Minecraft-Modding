package net.ni9logic.ni9logictmod.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import static net.ni9logic.ni9logictmod.Ni9LogicMod.minecraft;

public class LockItemInHand {
    public static KeyBinding KeyLockItem;
    private static boolean isLockItem = false;
    public static String target_item = "";


    public static void handleLIH() {
        if (KeyLockItem.wasPressed()) {
            isLockItem = !isLockItem;
            if (isLockItem) {
                target_item = getItemNameInMainHand();
                assert minecraft.player != null;
                minecraft.player.sendMessage(Text.of(target_item + " has been locked")
                        .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("red"))), true);
            } else {
                assert minecraft.player != null;
                minecraft.player.sendMessage(Text.of(target_item + " has been unlocked")
                        .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))), true);
            }
        }

        if (isLockItem) {
            assert MinecraftClient.getInstance().player != null;
            PlayerInventory inventory = MinecraftClient.getInstance().player.getInventory();
            if (!getItemNameInMainHand().equals(target_item)) {
                try {
                    Thread.sleep(1000);

                    for (int slot = 0; slot < inventory.size(); slot++) {
                        ItemStack itemStack = inventory.getStack(slot);
                        if (itemStack.getName().getString().equals(target_item)) {
                            ItemStack mainHandItem = inventory.getMainHandStack();
                            inventory.setStack(slot, mainHandItem);
                            inventory.setStack(inventory.selectedSlot, itemStack);
                            Thread.sleep(1000);
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String getItemNameInMainHand() {
        if (minecraft.player == null) {
            return "";
        }
        ItemStack itemStack = minecraft.player.getMainHandStack();
        return itemStack.getName().getString();
    }
}
