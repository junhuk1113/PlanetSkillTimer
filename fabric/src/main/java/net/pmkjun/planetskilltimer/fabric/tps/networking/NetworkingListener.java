package net.pmkjun.planetskilltimer.fabric.tps.networking;

import me.obsilabor.tpshud.TpsTracker;
import me.obsilabor.tpshud.config.ConfigManager;
import me.obsilabor.tpshud.minecraft;
import me.obsilabor.tpshud.screen.CompatibleServerScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

public class NetworkingListener {
    static {
        ClientPlayNetworking.registerGlobalReceiver(Packets.HANDSHAKE, (client, _, _, _) -> {
            if (ConfigManager.getConfig().getAskedForServerProvidedData() != true) {
                minecraft.execute(() -> {
                    client.setScreen(new CompatibleServerScreen());
                });
            } else {
                minecraft.execute(() -> {
                    client.toastManager.add(new SystemToast(
                            SystemToast.Type.TUTORIAL_HINT,
                            Text.translatable("screen.useServerProvidedData.title"),
                            Text.translatable("toast.useServerProvidedData.message")
                    ));
                });
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(Packets.TPS, (_, _, bytebuf, _) -> {
            if (ConfigManager.getConfig().getAskedForServerProvidedData() != true) {
                return;
            }
            float tps = (float) bytebuf.readDouble();
            TpsTracker.INSTANCE.setServerProvidedTps(tps);
        });
    }