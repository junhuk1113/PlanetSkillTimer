package net.pmkjun.planetskilltimer.forge;

import net.pmkjun.planetskilltimer.PlanetSkillTimer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pmkjun.planetskilltimer.forge.input.KeyMappings;

@Mod(PlanetSkillTimer.MOD_ID)
public class PlanetSkillTimerForge {
    public PlanetSkillTimerForge() {
        KeyMappings keyMappings = new KeyMappings();
        keyMappings.register();

        PlanetSkillTimer.init();
    }
}