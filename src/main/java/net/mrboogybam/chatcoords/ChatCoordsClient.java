package net.mrboogybam.chatcoords;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCoordsClient implements ClientModInitializer {

		public static final String LOG_PATH = "C:\\Users\\Rakhman Gul\\Desktop\\Chat-Coords-main\\run\\logs\\latest.log";

		private static KeyBinding Keybinding1;
		private static KeyBinding Keybinding2;
		private static KeyBinding Keybinding3;
		private boolean canSendCoords = false;
		private long lastSentTime = 0;
		public String xRounded;
		public String yRounded;
		public String zRounded;

		public static String logFile() {
				StringBuilder sb = new StringBuilder();
				try (BufferedReader br = new BufferedReader(new FileReader(LOG_PATH))) {
						String line;
						while ((line = br.readLine()) != null) {
								sb.append(line).append("\n");
						}
				} catch (IOException e) {
						e.printStackTrace();
				}
				return sb.toString();
		}

		public static void clearChatLog() {
				PlayerEntity player = MinecraftClient.getInstance().player;
				try {
						FileWriter fw = new FileWriter(new File(LOG_PATH));
						fw.write("");
						fw.close();
				} catch (IOException e) {
						e.printStackTrace();
				}
		}

		public static void reaction(String chatLog) {
				// Reaction for the word to type
				String patternString = "REACTION » First to type (\\w+) in the chat wins";
				Pattern pattern = Pattern.compile(patternString);
				Matcher matcher = pattern.matcher(chatLog);

				if (matcher.find()) {
						String toType = matcher.group(1);  // Gets the toType
						try {
								// Calling a robot to perform operations for us
								Robot robot = new Robot();
								robot.setAutoDelay(50);
								// Selects the desired string and copies it for us in the clipboard
								StringSelection selection = new StringSelection(toType);
								Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
								clipboard.setContents(selection, null);

								// Presses the chat key
								robot.keyPress(KeyEvent.VK_T);
								robot.keyRelease(KeyEvent.VK_T);


								// Tries to paste
								robot.keyPress(KeyEvent.VK_CONTROL);
								robot.keyPress(KeyEvent.VK_V);
								robot.keyRelease(KeyEvent.VK_V);
								robot.keyRelease(KeyEvent.VK_CONTROL);

								String guide = "Now Press Ctrl + V the msg ;)";
								assert MinecraftClient.getInstance().player != null;
								MinecraftClient.getInstance().player.sendMessage(Text.of(guide)
												.copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))));

								// Presses enter for us
								robot.keyPress(KeyEvent.VK_ENTER);
								robot.keyRelease(KeyEvent.VK_ENTER);

						}
						catch (Exception e) {
								e.printStackTrace();
						}
						clearChatLog();
				}
		}

		@Override
		public void onInitializeClient() {
				System.setProperty("java.awt.headless", "false");
				Keybinding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
								"Send Coordinates",
								InputUtil.Type.KEYSYM,
								GLFW.GLFW_KEY_N,
								"Coords by LOGIC"
				));
				Keybinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
								"Read Chat for Reaction",
								InputUtil.Type.KEYSYM,
								GLFW.GLFW_KEY_B,
								"Coords by LOGIC"
				));
				Keybinding3 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
								"Send Reaction",
								InputUtil.Type.KEYSYM,
								GLFW.GLFW_KEY_M,
								"Coords by LOGIC"
				));

				ClientTickEvents.END_CLIENT_TICK.register(client -> {
						String chatLog = logFile();
						if (Keybinding1.wasPressed()) {
								canSendCoords = !canSendCoords; // toggle the sending on/off
								if (canSendCoords) {
										assert MinecraftClient.getInstance().player != null;
										MinecraftClient.getInstance().player.sendMessage(Text.of("Auto Send Coords enabled")
														.copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))));
								} else {
										assert MinecraftClient.getInstance().player != null;
										MinecraftClient.getInstance().player.sendMessage(Text.of("Auto Send Coords disabled")
														.copy().setStyle(Style.EMPTY.withColor(TextColor.parse("red"))));
								}
						}

						if (canSendCoords && System.currentTimeMillis() - lastSentTime >= 1000) {
								// Send the coordinates every 1 second
								assert client.player != null;
								double x = client.player.getX();
								double y = client.player.getY();
								double z = client.player.getZ();
								xRounded = String.format("%.0f", x);
								yRounded = String.format("%.0f", y);
								zRounded = String.format("%.0f", z);


								String coords = "x" + xRounded + ", " + "y" + yRounded + ", " + "z" + zRounded;

								assert MinecraftClient.getInstance().player != null;
								MinecraftClient.getInstance().player.sendMessage(Text.of(coords));

								lastSentTime = System.currentTimeMillis();
						}

						if (Keybinding2.wasPressed()) {
								reaction(chatLog);
						}

						if (Keybinding3.wasPressed()) {
								String line = "---------------------------------------------";
								String msg = "REACTION » First to type win in the chat wins";

								assert MinecraftClient.getInstance().player != null;
								MinecraftClient.getInstance().player.sendMessage(Text.of(line));
								MinecraftClient.getInstance().player.sendMessage(Text.of(msg));
								MinecraftClient.getInstance().player.sendMessage(Text.of(line));
						}
				});

		}
}
