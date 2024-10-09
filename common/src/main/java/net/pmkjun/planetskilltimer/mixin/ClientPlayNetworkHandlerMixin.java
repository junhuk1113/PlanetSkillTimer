package net.pmkjun.planetskilltimer.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;
import net.pmkjun.planetskilltimer.PlanetSkillTimerClient;
import net.pmkjun.planetskilltimer.util.TpsTracker;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    private PlanetSkillTimerClient client = PlanetSkillTimerClient.getInstance();
    //private MinecraftClient mcc = MinecraftClient.getInstance();

    @Inject(at = @At("TAIL"), method = "onGameJoin")
    private void triggerJoinEvent(GameJoinS2CPacket packet, CallbackInfo info) {
        TpsTracker.INSTANCE.onGameJoined();
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
        String hash = packet.getSHA1();
        boolean required = packet.isRequired();  // 서버에서 리소스팩을 필수로 요구하는지 확인
        //boolean ishashMatch = (hash == client.data.hash);
        /*if(!ishashMatch&&client.data.hash != null){ // 해시가 일치하지 않는 경우
            client.data.hash = hash;
            client.configManage.save();
            this.serverInfo.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.ENABLED);
            mc.getResourcePackManager().disable("mineplanet.kr");
        }*/
        // 서버에서 리소스팩을 필수로 요구하고, 클라이언트에서 리소스팩 정책이 DISABLED인 경우에만 처리
        if (this.serverInfo != null && this.serverInfo.getResourcePackPolicy() == ServerInfo.ResourcePackPolicy.DISABLED && required) {
            client.data.hash = hash;
            client.configManage.save();
            // 리소스팩 거부했더라도 ACCEPTED 신호를 보냄
            this.sendResourcePackStatus(ResourcePackStatusC2SPacket.Status.ACCEPTED);

            // 리소스팩을 다운로드하지 않고 SUCCESSFULLY_LOADED 신호를 전송
            // 별도의 스레드 생성
            new Thread(() -> {
            try {
                Thread.sleep(1000); // 1000ms 대기
            } catch (InterruptedException e) {
                System.err.println("스레드가 중단되었습니다: " + e.getMessage());
            }

            // 리소스팩을 다운로드하지 않고 SUCCESSFULLY_LOADED 신호를 전송
                this.sendResourcePackStatus(ResourcePackStatusC2SPacket.Status.SUCCESSFULLY_LOADED);
            }).start();

            // 기존 리소스팩 처리 로직을 취소 (리소스팩을 실제로 적용하지 않음)     
            ci.cancel();
        }
    }
    private void sendResourcePackStatus(ResourcePackStatusC2SPacket.Status status) {
        // 서버에 리소스팩 상태 신호 전송
        this.connection.send(new ResourcePackStatusC2SPacket(status));
    }
}