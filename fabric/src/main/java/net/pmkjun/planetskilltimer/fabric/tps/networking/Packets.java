package net.pmkjun.planetskilltimer.fabric.tps.networking;

import net.minecraft.util.Identifier;

public class Packets {
    public static final Identifier HANDSHAKE = new Identifier("tpshud", "handshake");
    public static final String HANDSHAKE_STRING = "tpshud:handshake";
    public static final Identifier TPS = new Identifier("tpshud", "tps");
    public static final String TPS_STRING = "tpshud:tps";
}