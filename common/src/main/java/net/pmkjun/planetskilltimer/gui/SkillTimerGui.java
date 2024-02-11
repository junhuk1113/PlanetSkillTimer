package net.pmkjun.planetskilltimer.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.pmkjun.planetskilltimer.PlanetSkillTimerClient;
import net.pmkjun.planetskilltimer.file.Stat;
import net.pmkjun.planetskilltimer.util.SkillLevel;
import net.pmkjun.planetskilltimer.util.Timeformat;
import net.pmkjun.planetskilltimer.util.Timer;

public class SkillTimerGui {
    private MinecraftClient mc;
    private PlanetSkillTimerClient client;
    private TextRenderer font;

    public int one = 0;
    public int two = 0;
    public int thr = 0;
    public int last = 0;
    public int coolend = 0;


    private static final Identifier[] SKILL_ICONS = {
            new Identifier("planetskilltimer","skill_farming.png"),
            new Identifier("planetskilltimer","skill_felling.png"),
            new Identifier("planetskilltimer", "skill_mining.png"),
            new Identifier("planetskilltimer","skill_digging.png")
    };
    private static final Identifier WIDGETS = new Identifier("textures/gui/widgets.png");

    public SkillTimerGui(){
        this.mc = MinecraftClient.getInstance();
        this.client = PlanetSkillTimerClient.getInstance();
    }

    public void renderTick(DrawContext context, Timer timer){
        int i = 0;
        if(!this.client.data.toggleSkilltimer) return; //스킬타이머를 껏을때 실행x

        for(int skilltype = 0; skilltype < Stat.list.length ; skilltype++){
            if(client.data.toggleSkills[skilltype]) {
                render(context, SKILL_ICONS[skilltype], i, skilltype, timer.getDifference(client.data.lastSkillTime[skilltype]));
                i++;
            }
        }
    }


    private void render(DrawContext context,Identifier texture,int i,int skilltype, long ms) {
        MatrixStack poseStack = context.getMatrices();
        long remaining_activatetime, remaining_cooldowntime;
        int activatetime, cooldowntime;

        activatetime = SkillLevel.getActivateTime(skilltype,Stat.level[skilltype]);
        cooldowntime = SkillLevel.getCooldownTime(skilltype, Stat.level[skilltype]);
        remaining_activatetime = activatetime - ms;
        remaining_cooldowntime = cooldowntime - (ms - activatetime);

        context.drawTexture(WIDGETS, getXpos()+22*i,getYpos(), 24, 23, 22, 22);

        poseStack.push();
        poseStack.translate(3+getXpos()+22*i,getYpos()+4-1,0.0D);
        poseStack.scale(0.0625F, 0.0625F, 0.0625F);

        RenderSystem.setShaderTexture(0,texture);
        context.drawTexture(texture, 0, 0, 0, 0, 256, 256);
        poseStack.scale(16.0F, 16.0F, 16.0F);
        poseStack.pop();
        //System.out.println("남은 스킬 지속시간 : "+ (remaining_activatetime/(double)1000) +"초");
        if(remaining_activatetime > 0){
            //남은 지속시간
            //System.out.println("남은 스킬 지속시간 : "+ (remaining_activatetime/(double)1000) +"초");
            poseStack.push();
            poseStack.translate((3+getXpos()+22*i+8), (getYpos() + 8-1), 0.0F);
            poseStack.scale(0.9090909F, 0.9090909F, 0.9090909F);
            context.drawCenteredTextWithShadow(this.mc.textRenderer, (Text)Text.literal(Timeformat.getString(remaining_activatetime)), 0, 0, 16777215);
            if (client.data.toggleAlertSound) {
                if(remaining_activatetime/(double)1000 <= 1 && one == 0 && remaining_activatetime/(double)1000 > 0.2){
                    this.mc.world.playSound(this.mc.player, this.mc.player.getX(), this.mc.player.getY(), this.mc.player.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                    one = 1;
                }
                if(remaining_activatetime/(double)1000 <= 2 && two == 0 && remaining_activatetime/(double)1000 > 0.2){
                    this.mc.world.playSound(this.mc.player, this.mc.player.getX(), this.mc.player.getY(), this.mc.player.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                    two = 1;
                }
                if(remaining_activatetime/(double)1000 <= 3 && thr == 0 && remaining_activatetime/(double)1000 > 0.2) {
                    this.mc.world.playSound(this.mc.player, this.mc.player.getX(), this.mc.player.getY(), this.mc.player.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                    thr = 1;
                }

                if(remaining_activatetime/(double)1000 < 0.1 && last == 0 && remaining_activatetime/(double)1000 >= 0.05){
                    this.mc.world.playSound(this.mc.player, this.mc.player.getX(), this.mc.player.getY(), this.mc.player.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                    last = 1;

                }
                if(remaining_activatetime/(double)1000 < 0.05){
                    one = 0;
                    two = 0;
                    thr = 0;
                    last = 0;
                }
            }

            System.out.println("one: " + one + " two:" + two + " three: " + thr);
            poseStack.pop();
        }
        else if(remaining_cooldowntime > 0){

            System.out.println("남은 스킬 쿨타임 : "+(remaining_cooldowntime/(double)1000)+"초");
            poseStack.push();
            poseStack.translate((3+getXpos()+22*i+8), (getYpos() + 8-1), 0.0F);
            poseStack.scale(0.9090909F, 0.9090909F, 0.9090909F);
            context.drawCenteredTextWithShadow(this.mc.textRenderer, (Text)Text.literal(Timeformat.getString(remaining_cooldowntime)), 0, 0, 16777215);
            poseStack.pop();
            if (client.data.toggleAlertSound) {
                if (remaining_cooldowntime / (double) 1000 < 0.1 && remaining_cooldowntime / (double) 1000 > 0.05 && coolend == 0) {
                    this.mc.world.playSound(this.mc.player, this.mc.player.getX(), this.mc.player.getY(), this.mc.player.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                    coolend = 1;
                }

                if(remaining_cooldowntime / (double) 1000 < 0.05) {
                    coolend = 0;
                }
            }


        }


    }
    private int getEnabledSkillCount(){
        int count = 0;
        for(boolean toggleskill : this.client.data.toggleSkills){
            if(toggleskill) count++;
        }
        return count;
    }
    private int getXpos(){
        return (this.mc.getWindow().getScaledWidth()-(22*getEnabledSkillCount())) * this.client.data.SkillTimerXpos / 1000;
    }
    private int getYpos(){
        return (this.mc.getWindow().getScaledHeight()-(22)) * this.client.data.SkillTimerYpos / 1000;
    }

}
