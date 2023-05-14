package net.ni9logic.ni9logictmod.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import net.ni9logic.ni9logictmod.ni9logic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class Ni9LogicMixin {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        ni9logic.LOGGER.info("This line is printed by THE chat coords mixin!");
    }
}
