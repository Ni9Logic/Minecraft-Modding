package net.mrboogybam.chatcoords;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ToggleAutoClickerCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("toggleautoclicker")
				.executes(context -> {
					MinecraftClient client = MinecraftClient.getInstance();

					if (AutoClicker.isAutoClickerActive()) {
						AutoClicker.stopAutoClicker();
						context.getSource().sendFeedback(Text.of("Auto-Clicker deactivated"), false);
					} else {
						AutoClicker.startAutoClicker(client);
						context.getSource().sendFeedback(Text.of("Auto-Clicker activated"), false);
					}

					return 1;
				}));
	}
}
