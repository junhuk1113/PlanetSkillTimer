package net.pmkjun.planetskilltimer.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.Text;
import net.pmkjun.planetskilltimer.file.Stat;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class bossbarMixin {
    @Inject(method = "handlePacket", at={@At("RETURN")})
    public void render(BossBarS2CPacket packet, CallbackInfo ci){
        MinecraftClient mc = MinecraftClient.getInstance();
        //mc.player.sendMessage(Text.literal("handlePacket 함수 실행됨"));
        BossBarHud bossBarHud = mc.inGameHud.getBossBarHud();
        Map<UUID, ClientBossBar> bossBars = ((BossBarHudAccessor) bossBarHud).getBossBars();

        for (Map.Entry<UUID, ClientBossBar> entry : bossBars.entrySet()) {
            ClientBossBar bossBar = entry.getValue();
            if (mc.player != null&&bossBar!=null) {
                //mc.player.sendMessage(Text.literal("BossBar Text: " + bossBar.getName().getString()), false);
                String bossbarText = bossBar.getName().getString();
                String temp;
                if(bossbarText.contains("%)")){
                    for (int i = 0; i < Stat.list.length ; i++)
                    {
                        if(bossbarText.contains(Stat.list[i])){
                            mc.player.sendMessage(Text.literal(bossbarText), false);
                            temp=bossbarText.substring(bossbarText.indexOf(Stat.list[i]) +Stat.list[i].length()+1,bossbarText.indexOf("(")-1);
                            if(Integer.parseInt(temp) > Stat.level[i])
                            {
                                Stat.level[i] = Integer.parseInt(temp);
                                //mc.player.sendMessage(Text.literal(Stat.list[i] + "의 레벨이 " + temp + "(으)로 상승했습니다!"), false);
                                //mc.player.sendMessage(Text.literal(Skill.list[i] + "의 지속시간은 " + SkillLevel.getActivateTime(i, Stat.level[i]) / (double) 1000 + "초 입니다."), false);
                                //mc.player.sendMessage(Text.literal(Skill.list[i] + "의 쿨타임은 " + SkillLevel.getCooldownTime(i, Stat.level[i]) / (double) 1000 + "초 입니다."), false);
                            }
                        }
                    }
                }
                else if(bossbarText.contains("최대치)")) {
                    for (int i = 0; i < Stat.list.length; i++) {
                        if (bossbarText.contains(Stat.list[i])) {
                            temp = bossbarText.substring(bossbarText.indexOf(Stat.list[i]) +Stat.list[i].length()+1, bossbarText.indexOf("(") - 1);
                            if (Integer.parseInt(temp) > Stat.level[i]) {
                                Stat.level[i] = Integer.parseInt(temp);
                                //mc.player.sendMessage(Text.literal(Stat.list[i] + "의 레벨이 " + temp + "(으)로 상승했습니다!"), false);
                                //mc.player.sendMessage(Text.literal(Skill.list[i] + "의 지속시간은 " + SkillLevel.getActivateTime(i, Stat.level[i]) / (double) 1000 + "초 입니다."), false);
                                //mc.player.sendMessage(Text.literal(Skill.list[i] + "의 쿨타임은 " + SkillLevel.getCooldownTime(i, Stat.level[i]) / (double) 1000 + "초 입니다."), false);
                            
                            }
                        }
                    }
                }
            }
            else if(bossBar == null) 
            {
                //mc.player.sendMessage(Text.literal("bossBar is NULL!"));
                continue;
            }
        }
    }
}