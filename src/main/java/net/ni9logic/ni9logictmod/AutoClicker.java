package net.ni9logic.ni9logictmod;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import static net.ni9logic.ni9logictmod.Ni9LogicMod.minecraft;

public class AutoClicker {

    public static boolean canAutoClick = false;
    public static KeyBinding KeyAutoClicker;

    public static void AutoClick() {
        HitResult rayTrace = minecraft.crosshairTarget;

        if (rayTrace instanceof EntityHitResult) {
            Entity entity = ((EntityHitResult) rayTrace).getEntity();
            if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
                try {
                    // Only Attack enemies with locked item in hand
                    if (LockItemInHand.target_item.equals(getItemNameInMainHand())) {
                        Robot robot = new Robot();
                        Random random = new Random();
                        int sleepTime = 70 + random.nextInt(450);
                        robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);

                        Thread.sleep(sleepTime);
                    }
                } catch (AWTException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static void Send_Enable_Auto_Clicker_Text() {
        assert minecraft.player != null;
        minecraft.player.sendMessage(Text.of("Auto Clicker Enabled")
                .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))), true);
    }

    public static void Send_Disable_Auto_Clicker_Text() {
        assert minecraft.player != null;
        minecraft.player.sendMessage(Text.of("Auto Clicker Disabled")
                .copy().setStyle(Style.EMPTY.withColor(TextColor.parse("red"))), true);
    }

    public static void handleAC() {
        if (AutoClicker.KeyAutoClicker.wasPressed()) {
            AutoClicker.canAutoClick = !AutoClicker.canAutoClick; // toggle the sending on/off
            if (AutoClicker.canAutoClick) {
                AutoClicker.Send_Enable_Auto_Clicker_Text();
            } else {
                AutoClicker.Send_Disable_Auto_Clicker_Text();
            }
        }

        // Calls the function to toggle auto clicking
        if (AutoClicker.canAutoClick && minecraft.currentScreen == null) {
            AutoClicker.AutoClick();
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
