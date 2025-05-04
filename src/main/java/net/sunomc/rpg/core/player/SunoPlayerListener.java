package net.sunomc.rpg.core.player;

import net.sunomc.rpg.core.SunoMC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SunoPlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SunoPlayer sunoPlayer = new SunoPlayer(event.getPlayer());
        SunoMC.addPlayer(sunoPlayer);
    }

}
