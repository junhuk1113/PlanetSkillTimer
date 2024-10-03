package net.pmkjun.planetskilltimer.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.ClientConnection;
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


    @Shadow
    @Final
    private ClientConnection connection;

    @Shadow
    private ServerInfo serverInfo;

    @Inject(method = "onResourcePackSend", at = @At("HEAD"), cancellable = true)
    private void onResourcePackSend(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
        boolean required = packet.isRequired();  // 서버에서 리소스팩을 필수로 요구하는지 확인

        // 서버에서 리소스팩을 필수로 요구하고, 클라이언트에서 리소스팩 정책이 DISABLED인 경우에만 처리
        if (this.serverInfo != null && this.serverInfo.getResourcePackPolicy() == ServerInfo.ResourcePackPolicy.DISABLED && required) {
            // 리소스팩 거부했더라도 ACCEPTED 신호를 보냄
            this.sendResourcePackStatus(ResourcePackStatusC2SPacket.Status.ACCEPTED);

            // 리소스팩을 다운로드하지 않고 SUCCESSFULLY_LOADED 신호를 전송
            this.sendResourcePackStatus(ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED);

            // 기존 리소스팩 처리 로직을 취소 (리소스팩을 실제로 적용하지 않음)
            ci.cancel();
        }
    }
    private void sendResourcePackStatus(ResourcePackStatusC2SPacket.Status status) {
        // 서버에 리소스팩 상태 신호 전송
        this.connection.send(new ResourcePackStatusC2SPacket(status));
    }
}