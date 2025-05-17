package net.sunomc.rpg.core.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.sunomc.rpg.RpgCore;
import net.sunomc.rpg.core.events.PacketReceiveEvent;
import net.sunomc.rpg.core.events.PacketSendEvent;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public final class PacketHandler extends PacketAdapter {
    private static final List<PacketType> SUPPORTED_PACKET_TYPES = getSupportedPacketTypes();

    private PacketHandler() {
        super(RpgCore.getInstance(),
                ListenerPriority.NORMAL,
                SUPPORTED_PACKET_TYPES
        );
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(this);
    }

    public static void setup() {
        new PacketHandler();
    }

    @Override
    public void onPacketReceiving(@NotNull PacketEvent event) {
        handlePacketEvent(event, true);
    }

    @Override
    public void onPacketSending(@NotNull PacketEvent event) {
        handlePacketEvent(event, false);
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

    private void executePacketEvent(@NotNull PacketEvent originalEvent, boolean isReceiving) {
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