package net.mrboogybam.chatcoords;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCoordsClient implements ClientModInitializer {

		public static final String LOG_PATH = "C:\\Users\\Rakhman Gul\\AppData\\Roaming\\.minecraft\\logs\\latest.log";

		private static KeyBinding Keybinding1;
		private static KeyBinding Keybinding2;
		private boolean canSendCoords = false;
		public static Vec3d prevPos = null;

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
				try {
						FileWriter fw = new FileWriter(LOG_PATH);
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

						// Selects the desired string and copies it for us in the clipboard
						StringSelection selection = new StringSelection(toType);
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, null);

						String guide = toType + " automatically copied by Ni9Logic for you <3";

						assert MinecraftClient.getInstance().player != null;
						MinecraftClient.getInstance().player.sendMessage(Text.of(guide)
										.copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))));

						clearChatLog();
				}
		}

//		public static void Math_reaction(String chatLog) throws ScriptException {
//				String patternString = "MATH » (.*) = ?";
//				Pattern pattern = Pattern.compile(patternString);
//				Matcher matcher = pattern.matcher(chatLog);
//				if (matcher.find()) {
//						// Our python interpreter for evaluate function
//						String expression = matcher.group(1);
//						ScriptEngineManager manager = new ScriptEngineManager();
//						ScriptEngine engine = manager.getEngineByName("python");
//						if (engine == null) {
//								throw new RuntimeException("Python engine not found");
//						}
//
//						Object result = engine.eval(expression);
//						String res = result.toString();
//
//						// Selects the desired string and copies it for us in the clipboard
//						StringSelection selection = new StringSelection(res);
//						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//						clipboard.setContents(selection, null);
//
//						String guide = res + " automatically copied by Ni9Logic for you <3";
//
//						if (MinecraftClient.getInstance().player != null) {
//								MinecraftClient.getInstance().player.sendMessage(Text.of(guide)
//												.copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))));
//						}
//
//						clearChatLog();
//				}
//
//		}

		public static void is_Teleported() throws AWTException, InterruptedException {
				// We are checking this because after exiting it's so fast that it still manages to call this function and the game crashes right there
				if (MinecraftClient.getInstance().player == null) {
						return;
				}
				Vec3d curPos = MinecraftClient.getInstance().player.getPos();
				if (prevPos != null && !prevPos.equals(curPos)) {
						// Checking difference
						double dx = curPos.getX() - prevPos.getX();
						double dy = curPos.getY() - prevPos.getY();
						double dz = curPos.getZ() - prevPos.getZ();
						double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
						if (dist > 0.1) {
								Robot robot = new Robot();

								robot.keyPress(KeyEvent.VK_ESCAPE);
								robot.keyRelease(KeyEvent.VK_ESCAPE);

								for (int i = 0; i < 8; i++) {
										robot.keyPress(KeyEvent.VK_TAB);
										robot.keyRelease(KeyEvent.VK_TAB);
								}

								robot.keyPress(KeyEvent.VK_ENTER);
								robot.keyRelease(KeyEvent.VK_ENTER);
						}
				}
				prevPos = curPos;
		}

		@Override
		public void onInitializeClient() {
				// Basically when the client turns on
				System.setProperty("java.awt.headless", "false");
				Keybinding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
								"Teleport Detector",
								InputUtil.Type.KEYSYM,
								GLFW.GLFW_KEY_N,
								"Coords by LOGIC"
				));
				Keybinding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
								"Load Math",
								InputUtil.Type.KEYSYM,
								GLFW.GLFW_KEY_M,
								"Coords by LOGIC"
				));


				// Refreshes the client on every little single update
				ClientTickEvents.END_CLIENT_TICK.register(client -> {
						// Loads the chatLog very frequently, this is an expensive operation but our mod is about it so yeah
						// + I really don't know how much time it really consumes
						String chatLog = logFile();
						// Gets REACTION >>
						reaction(chatLog);
						// Gets MATH >>
//						try {
//								Math_reaction(chatLog);
//						} catch (ScriptException e) {
//								throw new RuntimeException(e);
//						}

						// If N is pressed
						if (Keybinding1.wasPressed()) {
								canSendCoords = !canSendCoords; // toggle the sending on/off
								if (canSendCoords) {
										prevPos = null;
										assert MinecraftClient.getInstance().player != null;
										MinecraftClient.getInstance().player.sendMessage(Text.of("Don't move teleport detect enabled")
														.copy().setStyle(Style.EMPTY.withColor(TextColor.parse("red"))));
								} else {
										prevPos = null;
										assert MinecraftClient.getInstance().player != null;
										MinecraftClient.getInstance().player.sendMessage(Text.of("Free to move teleport detect disabled")
														.copy().setStyle(Style.EMPTY.withColor(TextColor.parse("green"))));
								}
						}

						// Calls the function to update co-ordinates
						// update_Coordinates(canSendCoords);
						if (canSendCoords) {
								try {
										is_Teleported();
								} catch (AWTException | InterruptedException e) {
										throw new RuntimeException(e);
								}
						}


						if (Keybinding2.wasPressed()) {
								String line = "---------------------------------------------";
								String msg = "MATH » 2 + 3 + 100 * 5 = ?";

								assert MinecraftClient.getInstance().player != null;
								MinecraftClient.getInstance().player.sendMessage(Text.of(line));
								MinecraftClient.getInstance().player.sendMessage(Text.of(msg));
								MinecraftClient.getInstance().player.sendMessage(Text.of(line));
						}
				});

		}

}
