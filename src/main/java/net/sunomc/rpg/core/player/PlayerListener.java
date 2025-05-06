package net.sunomc.rpg.core.player;

import net.sunomc.rpg.utils.utils.BodyYawUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.sunomc.rpg.SunoMC;
import net.sunomc.rpg.utils.events.PacketReceiveEvent;

import com.comphenix.protocol.PacketType;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        SunoPlayer sunoPlayer = new SunoPlayer(event.getPlayer());
        SunoMC.addPlayer(sunoPlayer);
        sunoPlayer.load();
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        SunoPlayer sunoPlayer = SunoMC.getPlayer(event.getPlayer());
        SunoMC.removePlayer(sunoPlayer);
        sunoPlayer.save();
    }

    @EventHandler
    public void onPacketReceive(@NotNull PacketReceiveEvent event) {
        PacketType type = event.getType();

        if (type == PacketType.Play.Client.LOOK || type == PacketType.Play.Client.POSITION_LOOK) {
            Player player = event.getPlayer();
            float bodyYaw = BodyYawUtil.getBodyYaw(player);
            SunoMC.getPlayer(player).
        }
    }

}
