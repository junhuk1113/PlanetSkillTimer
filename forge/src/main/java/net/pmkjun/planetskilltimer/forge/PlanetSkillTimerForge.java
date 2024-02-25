package net.pmkjun.planetskilltimer.forge;

import net.pmkjun.planetskilltimer.PlanetSkillTimer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pmkjun.planetskilltimer.forge.input.KeyMappings;
import net.pmkjun.planetskilltimer.config.ConfigScreen;

@Mod(PlanetSkillTimer.MOD_ID)
public class PlanetSkillTimerForge {
    public PlanetSkillTimerForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);

        KeyMappings keyMappings = new KeyMappings();
        keyMappings.register();

        PlanetSkillTimer.init();
    }
    private void setup(final FMLCommonSetupEvent event){
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new ConfigScreen(screen)));
    }
}
