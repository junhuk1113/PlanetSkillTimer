package net.pmkjun.planetskilltimer.forge;

import net.pmkjun.planetskilltimer.PlanetSkillTimer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PlanetSkillTimer.MOD_ID)
public class PlanetSkillTimerForge {
    public PlanetSkillTimerForge() {
        PlanetSkillTimer.init();
    }
}