package net.pmkjun.planetskilltimer.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;
import net.pmkjun.planetskilltimer.util.TpsTracker;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("TAIL"), method = "onGameJoin")
    private void triggerJoinEvent(GameJoinS2CPacket packet, CallbackInfo info) {
        TpsTracker.INSTANCE.onGameJoined();
    }
    
    @Inject(method = "onResourcePackSend", at = @At("HEAD"))
    private void onResourcePackSendHead(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
        // 예: 리소스팩의 URL이나 해시를 로그에 기록
        System.out.println("Resource Pack URL: " + packet.getURL());
        System.out.println("Resource Pack Hash: " + packet.getSHA1());
    }

    @Inject(method = "sendResourcePackStatus", at = @At("HEAD"))
    private void sendResourcePackStatus(ResourcePackStatusC2SPacket.Status packStatus, CallbackInfo ci) {
        System.out.println(packStatus.name());
    }
}