package net.pmkjun.planetskilltimer.forge.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pmkjun.planetskilltimer.config.ConfigScreen;
import net.pmkjun.planetskilltimer.input.IKeyMappings;

public class KeyMappings implements IKeyMappings {
    public static KeyBinding openSettingScreen =
            new KeyBinding("planetskilltimer.key.open_settings", InputUtil.GLFW_KEY_J, "planetskilltimer.key.open_settings");

    @Override
    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(KeyMappings::registerKeyBindings);
    }

    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(openSettingScreen);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if(event.phase == TickEvent.Phase.END) {
            while(openSettingScreen.wasPressed()) {
                mc.setScreen(new ConfigScreen(mc.currentScreen));
            }
        }
    }
}
