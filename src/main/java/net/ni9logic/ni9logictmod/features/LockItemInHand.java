package net.ni9logic.ni9logictmod.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.ni9logic.ni9logictmod.Ni9LogicMod.minecraft;

public class LockItemInHand {
    public static KeyBinding KeyLockItem;
    private static boolean isLockItem = false;
    public static String target_item = "";


    public static void handleLIH() {
        MutableText ti = Text.of(getItemNameInMainHand()).copy();

        if (KeyLockItem.wasPressed()) {
            isLockItem = !isLockItem;
            if (isLockItem) {
                Style activateStyle = Style.EMPTY
                        .withColor(Formatting.DARK_RED)
                        .withBold(true);

                assert minecraft.player != null;
                target_item = getItemNameInMainHand();
                MutableText toggle = Text.of(" [LOCKED] ").copy().setStyle(activateStyle);

                minecraft.player.sendMessage(ti.append(toggle), true);
            } else {
                Style activateStyle = Style.EMPTY
                        .withColor(Formatting.DARK_GREEN)
                        .withBold(true);

                assert minecraft.player != null;
                target_item = getItemNameInMainHand();
                MutableText toggle = Text.of(" [UNLOCKED] ").copy().setStyle(activateStyle);

                minecraft.player.sendMessage(ti.append(toggle), true);
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
