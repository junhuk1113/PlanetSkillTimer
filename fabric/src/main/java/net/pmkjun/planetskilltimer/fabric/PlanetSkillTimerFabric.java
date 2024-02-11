package net.pmkjun.planetskilltimer.fabric;

import net.pmkjun.planetskilltimer.PlanetSkillTimer;
import net.fabricmc.api.ModInitializer;
import net.pmkjun.planetskilltimer.fabric.input.KeyMappings;

public class PlanetSkillTimerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        KeyMappings keyMappings = new KeyMappings();
        keyMappings.register();

        PlanetSkillTimer.init();
    }
}