package net.sunomc.rpg.utils.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;

import net.sunomc.rpg.RpgCore;
import net.sunomc.rpg.utils.events.PacketReceiveEvent;
import net.sunomc.rpg.utils.events.PacketSendEvent;

public class PacketHandler extends PacketAdapter {

    public PacketHandler() {
        super(RpgCore.getInstance(), ListenerPriority.NORMAL, PacketType.values());
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        PacketReceiveEvent call = new PacketReceiveEvent(event, packet);

        RpgCore.getInstance().getServer().getPluginManager().callEvent(call);

        event.setCancelled(call.isCancelled());

        if (!call.getPacket().equals(packet)) {
            event.setPacket(call.getPacket());
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        PacketSendEvent call = new PacketSendEvent(event, packet);

        RpgCore.getInstance().getServer().getPluginManager().callEvent(call);

        event.setCancelled(call.isCancelled());

        if (!call.getPacket().equals(packet)) {
            event.setPacket(call.getPacket());
        }
    }
}
