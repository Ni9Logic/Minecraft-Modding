package net.mrboogybam.chatcoords;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

public class AutoClicker implements Runnable {
    private static final int MIN_DELAY_MS = 1000; // Minimum delay between clicks (in milliseconds)
    private static final int MAX_DELAY_MS = 5000; // Maximum delay between clicks (in milliseconds)

    private final Robot robot;
    private final Random rand;

    public AutoClicker() throws Exception {
        this.robot = new Robot();
        this.rand = new Random();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Generate a random delay between clicks (in milliseconds)
                int delay = rand.nextInt(MAX_DELAY_MS - MIN_DELAY_MS) + MIN_DELAY_MS;

                // Click the left mouse button
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                // Sleep for the delay period
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            // Interrupted, stop the auto-clicking
        }
    }

    public static void auto_click() throws Exception {
        // Start the auto-clicker thread
        AutoClicker autoClicker = new AutoClicker();
        Thread thread = new Thread(autoClicker);
        thread.start();

        // Wait for the thread to finish (or interrupt it)
        try {
            thread.join();
        } catch (InterruptedException e) {
            thread.interrupt();
        }
    }
}
