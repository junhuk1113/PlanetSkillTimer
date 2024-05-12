package net.pmkjun.planetskilltimer.fabric.tps.event;

import me.obsilabor.alert.Cancellable;
import net.minecraft.network.packet.Packet;

public class PacketReceiveEvent extends Cancellable {
    private Packet<?> packet;

    public PacketReceiveEvent(Packet<?> packet) {
        this.packet = packet;
    }
}