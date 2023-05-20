package net.ni9logic.ni9logictmod.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.ni9logic.ni9logictmod.ni9logic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class Ni9LogicMixin {
    // Commented for testing purposes
//    @Inject(method = "onGameMessage", at = @At("HEAD"))
//    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
//        assert MinecraftClient.getInstance().player != null;
//        Text Message = packet.content();
//        if (Message != null) {
//            ChatMessagess.recentMessage = Message.getString();
//            MinecraftClient.getInstance().player.sendMessage(Text.of(ChatMessagess.recentMessage));
//            System.out.println(ChatMessagess.recentMessage);
//        }
//    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(CallbackInfo info) {
        ni9logic.LOGGER.info("This line is printed by THE chat coords mixin!");
    }
}
