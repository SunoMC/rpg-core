package net.sunomc.rpg.core.events;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.World;

import net.sunomc.rpg.core.common.ChatIcon;
import net.sunomc.rpg.core.common.SunoPlayer;
@Setter
@Getter
public class SunoChatEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final SunoPlayer player;
    private boolean cancel;

    private final Component originalMessage;
    private Component message;
    private ChatIcon icon;
    private World targetWorld;

    public SunoChatEvent(SunoPlayer player, Component message, ChatIcon icon) {
        this.player = player;
        this.message = message;
        this.originalMessage = message;
        this.icon = icon;
        this.targetWorld = player.getLocation().getWorld();
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {return handlers;}

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
