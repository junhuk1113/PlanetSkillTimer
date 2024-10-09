package net.pmkjun.planetskilltimer.config;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.pmkjun.planetskilltimer.PlanetSkillTimerClient;
import net.pmkjun.planetskilltimer.gui.widget.Slider;

public class ConfigScreen extends Screen{

    private Minecraft mc;
    private PlanetSkillTimerClient client;
    private final Screen parentScreen;

    private Button toggleSkillTimerButton;
    private Button toggleAlertSoundButton;
    private Button[] toggleSkillsButton = new Button[4];
    private Button toggleTpsCorrectionButton;
    String[] SkillList = {"farming","felling","mining","digging"};
    private Slider XPosSlider;
    private Slider YPosSlider;
    private int width, height;

    public ConfigScreen(Screen parentScreen) {
        super(Component.literal("스킬 타이머 설정"));
        this.parentScreen = parentScreen;
        this.mc = Minecraft.getInstance();
        this.client = PlanetSkillTimerClient.getInstance();

        this.width = 150;
        this.height = (20 + 2) * 9;
    }
    @Override
    protected void init() {
        Component text;
        if(client.data.toggleSkilltimer){
            text = Component.translatable("planetskilltimer.config.skilltimer").append(
                    Component.translatable("planetskilltimer.config.enable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.GREEN).withBold(true)));
        }
        else{
            text = Component.translatable("planetskilltimer.config.skilltimer").append(
                    Component.translatable("planetskilltimer.config.disable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.RED).withBold(true)));
        }
        toggleSkillTimerButton = Button.builder(text,button -> {
            toggleSkilltimer();
        }).pos(getRegularX(), getRegularY()).size(150, 20).build();
        this.addRenderableWidget(toggleSkillTimerButton);

        Component text2;
        if(client.data.toggleAlertSound){
            text2 = Component.translatable("planetskilltimer.config.sound").append(
                    Component.translatable("planetskilltimer.config.enable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.GREEN).withBold(true)));
        }
        else{
            text2 = Component.translatable("planetskilltimer.config.sound").append(
                    Component.translatable("planetskilltimer.config.disable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.RED).withBold(true)));
        }
        toggleAlertSoundButton = Button.builder(text2,button -> {
            toggleAlertSound();
        }).pos(getRegularX(),getRegularY()+(20+2)*1).size(150,20).build();
        this.addRenderableWidget(toggleAlertSoundButton);

        for(int i = 0; i < 4 ; i++){
            if(client.data.toggleSkills[i]){
                text = Component.translatable("planetskilltimer.config."+SkillList[i]).append(
                        Component.translatable("planetskilltimer.config.enable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.GREEN).withBold(true)));
            }
            else{
                text = Component.translatable("planetskilltimer.config."+SkillList[i]).append(
                        Component.translatable("planetskilltimer.config.disable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.RED).withBold(true)));
            }
            switch (i){
                case 0:
                    toggleSkillsButton[i] = Button.builder(text,button -> {
                        toggleSkills(0);
                    }).pos(getRegularX(),getRegularY()+(20+2)*(i+2)).size(150,20).build();
                    break;
                case 1:
                    toggleSkillsButton[i] = Button.builder(text,button -> {
                        toggleSkills(1);
                    }).pos(getRegularX(),getRegularY()+(20+2)*(i+2)).size(150,20).build();
                    break;
                case 2:
                    toggleSkillsButton[i] = Button.builder(text,button -> {
                        toggleSkills(2);
                    }).pos(getRegularX(),getRegularY()+(20+2)*(i+2)).size(150,20).build();
                    break;
                case 3:
                    toggleSkillsButton[i] = Button.builder(text,button -> {
                        toggleSkills(3);
                    }).pos(getRegularX(),getRegularY()+(20+2)*(i+2)).size(150,20).build();
                    break;

            }

            this.addRenderableWidget(toggleSkillsButton[i]);
        }

        Button exitButton = Button.builder(Component.translatable("planetskilltimer.config.exit"), button -> {
            mc.setScreen(parentScreen);
        }).pos(mc.getWindow().getGuiScaledWidth() / 2 - 35, mc.getWindow().getGuiScaledHeight() - 22).size(70, 20).build();
        this.addRenderableWidget(exitButton);

        XPosSlider = new Slider(getRegularX(), getRegularY()+(20+2)*6,150,20,Component.literal("X : "),0,1000,this.client.data.SkillTimerXpos){
            @Override
            protected void applyValue() {
                client.data.SkillTimerXpos = this.getValueInt();
                client.configManage.save();
            }
        };
        this.addRenderableWidget(XPosSlider);
        YPosSlider = new Slider(getRegularX(), getRegularY()+(20+2)*7,150,20,Component.literal("Y : "),0,1000,this.client.data.SkillTimerYpos){
            @Override
            protected void applyValue() {
                client.data.SkillTimerYpos = this.getValueInt();
                client.configManage.save();
            }
        };
        this.addRenderableWidget(YPosSlider);
        toggleTpsCorrectionButton = Button.builder(Component.translatable("planetskilltimer.config.tpscorrection"), button -> {
            toggleTpsCorrection();
            setTpsCorrectionButtonText();
        }).pos(getRegularX(), getRegularY()+(20+2)*8).size(150, 20).build();
        setTpsCorrectionButtonText();
        this.addRenderableWidget(toggleTpsCorrectionButton);
    }
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        XPosSlider.render(guiGraphics,mouseX,mouseY,delta);
        YPosSlider.render(guiGraphics,mouseX,mouseY,delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    private void toggleSkilltimer(){
        if(client.data.toggleSkilltimer){
            toggleSkillTimerButton.setMessage(Component.translatable("planetskilltimer.config.skilltimer").append(
                    Component.translatable("planetskilltimer.config.disable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.RED).withBold(true))));
            client.data.toggleSkilltimer = false;
            client.configManage.save();
        }
        else{
            toggleSkillTimerButton.setMessage(Component.translatable("planetskilltimer.config.skilltimer").append(
                    Component.translatable("planetskilltimer.config.enable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.GREEN).withBold(true))));
            client.data.toggleSkilltimer = true ;
            client.configManage.save();
        }
    }

    private void toggleAlertSound(){
        if(client.data.toggleAlertSound){
            toggleAlertSoundButton.setMessage(Component.translatable("planetskilltimer.config.sound").append(
                    Component.translatable("planetskilltimer.config.disable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.RED).withBold(true))));
            client.data.toggleAlertSound = false;
            client.configManage.save();
        }
        else{
            toggleAlertSoundButton.setMessage(Component.translatable("planetskilltimer.config.sound").append(
                    Component.translatable("planetskilltimer.config.enable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.GREEN).withBold(true))));
            client.data.toggleAlertSound = true ;
            client.configManage.save();
        }
    }

    private void toggleSkills(int skilltype){
        if(client.data.toggleSkills[skilltype]){
            toggleSkillsButton[skilltype].setMessage(Component.translatable("planetskilltimer.config."+SkillList[skilltype]).append(
                    Component.translatable("planetskilltimer.config.disable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.RED).withBold(true))));
            client.data.toggleSkills[skilltype] = false;
            client.configManage.save();
        }
        else{
            toggleSkillsButton[skilltype].setMessage(Component.translatable("planetskilltimer.config."+SkillList[skilltype]).append(
                    Component.translatable("planetskilltimer.config.enable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.GREEN).withBold(true))));
            client.data.toggleSkills[skilltype] = true ;
            client.configManage.save();
        }
    }

    private void toggleTpsCorrection(){
        client.data.toggleTpsCorrection = !client.data.toggleTpsCorrection;
    }
    private void setTpsCorrectionButtonText(){
        if(client.data.toggleTpsCorrection){
            toggleTpsCorrectionButton.setMessage(Component.translatable("planetskilltimer.config.tpscorrection").append(
                Component.translatable("planetskilltimer.config.enable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.GREEN).withBold(true))));
        }
        else{
            toggleTpsCorrectionButton.setMessage(Component.translatable("planetskilltimer.config.tpscorrection").append(
                Component.translatable("planetskilltimer.config.disable").withStyle(Style.EMPTY.applyFormat(ChatFormatting.RED).withBold(true))));
        }
    }

    int getRegularX() {
        return  mc.getWindow().getGuiScaledWidth() / 2 - width / 2;
    }

    int getRegularY() {
        return mc.getWindow().getGuiScaledHeight() / 2 - height / 2;
    }
}
