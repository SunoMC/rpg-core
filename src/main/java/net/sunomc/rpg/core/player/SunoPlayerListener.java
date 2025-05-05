package net.sunomc.rpg.core.player;

import net.sunomc.rpg.SunoMC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SunoPlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SunoPlayer sunoPlayer = new SunoPlayer(event.getPlayer());
        SunoMC.addPlayer(sunoPlayer);
        sunoPlayer.load();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        SunoPlayer sunoPlayer = SunoMC.getPlayer(event.getPlayer());
        SunoMC.removePlayer(sunoPlayer);
        sunoPlayer.save();
    }

}
