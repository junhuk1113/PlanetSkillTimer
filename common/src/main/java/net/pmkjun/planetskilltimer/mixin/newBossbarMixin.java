package net.pmkjun.planetskilltimer.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.pmkjun.planetskilltimer.file.Skill;
import net.pmkjun.planetskilltimer.file.Stat;
import net.pmkjun.planetskilltimer.util.SkillLevel;


@Mixin(BossBar.class)
public class newBossbarMixin {
    MinecraftClient mc = MinecraftClient.getInstance();
    @Inject(at = @At("RETURN"), method = "setName")
    public void setName(Text name, CallbackInfo ci) {
        mc.player.sendMessage(name);
        String bossbarText = name.getString();
        String temp;
        if(bossbarText.contains("%)")){
            for (int i = 0; i < Stat.list.length ; i++)
            {
                if(bossbarText.contains(Stat.list[i])){
                    System.out.println(bossbarText);
                    temp=bossbarText.substring(bossbarText.indexOf(Stat.list[i]) +Stat.list[i].length()+1,bossbarText.indexOf("(")-1);
                    if(Integer.parseInt(temp) > Stat.level[i])
                    {
                        Stat.level[i] = Integer.parseInt(temp);
                        System.out.println(Stat.list[i]+"의 레벨이 "+ temp + "(으)로 상승했습니다!");
                        System.out.println(Skill.list[i]+"의 지속시간은 "+SkillLevel.getActivateTime(i,Stat.level[i])/(double)1000+"초 입니다.");
                        System.out.println(Skill.list[i]+"의 쿨타임은 "+SkillLevel.getCooldownTime(i,Stat.level[i])/(double)1000+"초 입니다.");
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
                        System.out.println(Stat.list[i] + "의 레벨이 " + temp + "(으)로 상승했습니다!");
                        System.out.println(Skill.list[i] + "의 지속시간은 " + SkillLevel.getActivateTime(i, Stat.level[i]) / (double) 1000 + "초 입니다.");
                        System.out.println(Skill.list[i] + "의 쿨타임은 " + SkillLevel.getCooldownTime(i, Stat.level[i]) / (double) 1000 + "초 입니다.");
                    }
                }
            }
        }
     }
}
