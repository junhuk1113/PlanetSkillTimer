package net.pmkjun.planetskilltimer;

public class PlanetSkillTimer
{
	public static final String MOD_ID = "planetskilltimer";

	public static PlanetSkillTimerClient client;
	public static void init() {
		client = new PlanetSkillTimerClient();
		client.init();
	}
}
