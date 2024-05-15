package net.pmkjun.planetskilltimer.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.pmkjun.planetskilltimer.util.TpsTracker;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("TAIL"), method = "onGameJoin")
    private void triggerJoinEvent(GameJoinS2CPacket packet, CallbackInfo info) {
        TpsTracker.INSTANCE.onGameJoined();
    }
}