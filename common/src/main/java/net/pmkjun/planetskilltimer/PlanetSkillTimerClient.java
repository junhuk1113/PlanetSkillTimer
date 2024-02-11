package net.pmkjun.planetskilltimer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.pmkjun.planetskilltimer.config.ConfigManage;
import net.pmkjun.planetskilltimer.file.Data;
import net.pmkjun.planetskilltimer.gui.SkillTimerGui;
import net.pmkjun.planetskilltimer.util.Timer;

public class PlanetSkillTimerClient {
    private final MinecraftClient mc;
    private static PlanetSkillTimerClient instance;
    public Data data;
    public ConfigManage configManage;

    private final SkillTimerGui skillTimerGui;
    private final Timer timer = new Timer();
    public PlanetSkillTimerClient(){
        this.mc = MinecraftClient.getInstance();
        instance = this;
        this.configManage = new ConfigManage();
        this.data = this.configManage.load();
        if(this.data == null){
            this.data = new Data();
            this.configManage.save();
        }
        this.skillTimerGui = new SkillTimerGui();
    }
    public void init(){

    }
    public void renderEvent(DrawContext context) {
        this.skillTimerGui.renderTick(context,this.timer);
        this.timer.updateTime();
    }
    public void updateLastSkilltime(int skilltype){
        this.data.lastSkillTime[skilltype] = this.timer.getCurrentTime();
        this.configManage.save();
    }

    public static  PlanetSkillTimerClient getInstance(){
        return instance;
    }
}
