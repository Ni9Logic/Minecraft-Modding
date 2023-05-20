package net.ni9logic.utils;

import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.concurrent.TimeUnit;

import static net.ni9logic.ni9logictmod.Ni9LogicMod.minecraft;

public class StopWatch {
    public static long elapsedSeconds;

    public static void myTimer(String Message, long startTime) {
        //Style
        Style timer = Style.EMPTY
                .withBold(true);

        // Convert elapsed time to the desired format
        long currentTime = System.currentTimeMillis();
        long elapsedTimeMillis = currentTime - startTime;
        long elapsedMilliseconds = elapsedTimeMillis % 1000;
        elapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis);

        assert minecraft.player != null;
        minecraft.player.sendMessage(Text.of(String.format("%s: %d.%04d", Message, elapsedSeconds, elapsedMilliseconds))
                .copy().setStyle(timer), true);
    }
}
