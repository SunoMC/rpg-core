package net.sunomc.rpg.core.events;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import net.sunomc.rpg.core.common.SunoPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@Setter
@Getter
public class SunoPlayerJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final SunoPlayer player;
    private final Player bukkitPlayer;
    private final boolean firstTime;

    public SunoPlayerJoinEvent(@NotNull SunoPlayer player) {
        this.player = player;
        this.bukkitPlayer = player.getServerPlayer();
        this.firstTime = player.isFirstTime();
    }

    @Override
    public @NotNull HandlerList getHandlers() {return handlers;}

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
