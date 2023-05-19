package net.ni9logic.ni9logictmod.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.ni9logic.ni9logictmod.ni9logic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class Ni9LogicMixin {

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(CallbackInfo info) {
        ni9logic.LOGGER.info("This line is printed by THE chat coords mixin!");
    }
}
