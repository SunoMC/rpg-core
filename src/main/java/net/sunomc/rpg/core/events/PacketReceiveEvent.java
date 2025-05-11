package net.sunomc.rpg.core.events;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;



public class PacketReceiveEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancel;

    @Getter
    private final PacketEvent packetEvent;
    private final PacketContainer container;

    public PacketReceiveEvent(PacketEvent packetEvent, PacketContainer container) {
        this.packetEvent = packetEvent;
        this.container = container;
    }

    public Player getPlayer() {return packetEvent.getPlayer();}
    public PacketType getType() {return packetEvent.getPacketType();}
    public PacketContainer getPacket() {return container;}

    @Override
    public boolean isCancelled() {return cancel;}

    @Override
    public void setCancelled(boolean cancel) {this.cancel = cancel;}

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
