package net.mrboogybam.chatcoords.mixin;

import net.mrboogybam.chatcoords.ChatCoords;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ChatCoordsMixin {
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        // ChatCoords.LOGGER.info("This line is printed by THE chat coords mixin!");
    }
}
