package net.mrboogybam.chatcoords;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class AutoClicker {
	private static boolean autoClickerActive = false;
	private static Vec3d initialRotation;

	public static void startAutoClicker(MinecraftClient client) {
		initialRotation = client.player.getRotationVec(1.0F);
		autoClickerActive = true;

		Thread clickerThread = new Thread(() -> {
			while (autoClickerActive) {
				if (client.currentScreen == null) {
					if (!client.player.getRotationVec(1.0F).equals(initialRotation)) {
						stopAutoClicker();
					} else {
						client.player.swingHand(Hand.MAIN_HAND);
						HitResult hitResult = client.crosshairTarget;
						if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
							client.interactionManager.attackEntity(client.player, ((EntityHitResult) hitResult).getEntity());
						}
					}
				}

				try {
					Thread.sleep(100); // Adjust the delay between clicks (in milliseconds)
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		clickerThread.start();
	}

	public static void stopAutoClicker() {
		autoClickerActive = false;
	}

	public static boolean isAutoClickerActive() {
		return autoClickerActive;
	}
}
