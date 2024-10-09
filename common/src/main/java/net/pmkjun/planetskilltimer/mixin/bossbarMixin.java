package net.pmkjun.planetskilltimer.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.pmkjun.planetskilltimer.file.Stat;

import java.util.Map;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class bossbarMixin {
    @Inject(method = "update", at={@At(value = "RETURN", ordinal = 0)})
    public void render(ClientboundBossEventPacket packet, CallbackInfo ci){
        Minecraft mc = Minecraft.getInstance();
        //mc.player.sendMessage(Text.literal("handlePacket 함수 실행됨"));
        BossHealthOverlay bossBarHud = mc.gui.getBossOverlay();
        Map<UUID, LerpingBossEvent> bossBars = ((BossBarHudAccessor) bossBarHud).getBossBars();

        for (Map.Entry<UUID, LerpingBossEvent> entry : bossBars.entrySet()) {
            LerpingBossEvent bossBar = entry.getValue();
            if (mc.player != null&&bossBar!=null) {
                //mc.player.sendMessage(Text.literal("BossBar Text: " + bossBar.getName().getString()), false);
                String bossbarText = bossBar.getName().getString();
                String temp;
                if(bossbarText.contains("%)")){
                    for (int i = 0; i < Stat.list.length ; i++)
                    {
                        if(bossbarText.contains(Stat.list[i])){
                            //mc.player.sendMessage(Text.literal(bossbarText), false);
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
