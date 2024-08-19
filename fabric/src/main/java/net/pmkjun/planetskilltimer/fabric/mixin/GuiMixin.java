package net.pmkjun.planetskilltimer.fabric.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.pmkjun.planetskilltimer.PlanetSkillTimerClient;
import net.pmkjun.planetskilltimer.mixin.BossBarHudAccessor;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({InGameHud.class})
public class GuiMixin {
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = {"render(Lnet/minecraft/client/gui/DrawContext;F)V"}, at = {@At("RETURN")} ,cancellable = false)
    private void renderMixin(DrawContext context, float tickDelta, CallbackInfo info) {
        PlanetSkillTimerClient.getInstance().renderEvent(context);
        BossBarHud bossBarHud = client.inGameHud.getBossBarHud();
        Map<UUID, ClientBossBar> bossBars = ((BossBarHudAccessor) bossBarHud).getBossBars();
        
        // bossBars 맵 순회
        for (Map.Entry<UUID, ClientBossBar> entry : bossBars.entrySet()) {
            ClientBossBar bossBar = entry.getValue();
            if (client.player != null&&bossBar!=null) {
                client.player.sendMessage(Text.literal("BossBar Text: " + bossBar.getName().getString()), false);
            }
            else if(bossBar == null) client.player.sendMessage(Text.literal("bossBar is NULL!"));
        }
    }
}