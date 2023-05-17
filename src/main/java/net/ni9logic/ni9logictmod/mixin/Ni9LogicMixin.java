package net.ni9logic.ni9logictmod.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.ni9logic.ni9logictmod.ni9logic;
import net.ni9logic.utils.ChatMessagess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class Ni9LogicMixin {
    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onChatMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        String Message = packet.content().getString();
        if (Message != null) {
            ChatMessagess.recentMessage = Message;
        }
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(CallbackInfo info) {
        ni9logic.LOGGER.info("This line is printed by THE chat coords mixin!");
    }
}
