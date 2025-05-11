package net.sunomc.rpg.utils.handler;

import org.bukkit.Bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;

import net.sunomc.rpg.RpgCore;
import net.sunomc.rpg.utils.events.PacketReceiveEvent;
import net.sunomc.rpg.utils.events.PacketSendEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class PacketHandler extends PacketAdapter {
    private static final List<PacketType> SUPPORTED_PACKET_TYPES = getSupportedPacketTypes();

    public PacketHandler() {
        super(RpgCore.getInstance(),
                ListenerPriority.NORMAL,
                SUPPORTED_PACKET_TYPES
        );
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(this);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (!event.getPacket().getType().name().startsWith("Dynamic-")) {
            handlePacketEvent(event, true);
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (!event.getPacket().getType().name().startsWith("Dynamic-")) {
            handlePacketEvent(event, false);
        }
    }

    private void handlePacketEvent(PacketEvent originalEvent, boolean isReceiving) {
        if (Bukkit.isPrimaryThread()) {
            executePacketEvent(originalEvent, isReceiving);
        } else {
            Bukkit.getScheduler().runTask(RpgCore.getInstance(), () -> {
                executePacketEvent(originalEvent, isReceiving);
            });
        }
    }

    private void executePacketEvent(PacketEvent originalEvent, boolean isReceiving) {
        PacketContainer packet = originalEvent.getPacket();

        if (isReceiving) {
            PacketReceiveEvent call = new PacketReceiveEvent(originalEvent, packet);
            Bukkit.getPluginManager().callEvent(call);

            originalEvent.setCancelled(call.isCancelled());
            if (!call.getPacket().equals(packet)) {
                originalEvent.setPacket(call.getPacket());
            }
        } else {
            PacketSendEvent call = new PacketSendEvent(originalEvent, packet);
            Bukkit.getPluginManager().callEvent(call);

            originalEvent.setCancelled(call.isCancelled());
            if (!call.getPacket().equals(packet)) {
                originalEvent.setPacket(call.getPacket());
            }
        }
    }

    private static @NotNull List<PacketType> getSupportedPacketTypes() {
        List<PacketType> types = new ArrayList<>();

        for (PacketType p : PacketType.values()) {
            if (p.isSupported() &&
                    !p.isDeprecated() &&
                    !p.isAsyncForced()
            ) types.add(p);
        }

        return types;
    }

}