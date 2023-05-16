package net.ni9logic.ni9logictmod.features;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
                    if (LockItemInHand.target_item.equals(LockItemInHand.getItemNameInMainHand())) {
                        Robot robot = new Robot();
                        Random random = new Random();
                        int sleepTime = 70 + random.nextInt(450);
                        robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);

                        Thread.sleep(sleepTime); // Randomizes the cps
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
        if (KeyAutoClicker.wasPressed()) {
            canAutoClick = !canAutoClick; // toggle the sending on/off
            if (canAutoClick) {
                Send_Enable_Auto_Clicker_Text();
            } else {
                Send_Disable_Auto_Clicker_Text();
            }
        }

        // Calls the function to toggle auto clicking
        if (canAutoClick && minecraft.currentScreen == null) {
            AutoClick();
        }
    }
}
