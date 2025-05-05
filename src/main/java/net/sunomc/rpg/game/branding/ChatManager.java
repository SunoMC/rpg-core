package net.sunomc.rpg.game.branding;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.papermc.paper.event.player.AsyncChatEvent;

import net.sunomc.rpg.RpgCore;
import net.sunomc.rpg.SunoMC;
import net.sunomc.rpg.core.common.ChatIcon;
import net.sunomc.rpg.core.common.SunoChatEvent;
import net.sunomc.rpg.core.player.SunoPlayer;
import net.sunomc.rpg.utils.utils.ChatBuilder;

public class ChatManager implements Listener {

    @EventHandler
    public void onChatEvent(AsyncChatEvent event) {
        Bukkit.getScheduler().runTask(RpgCore.getInstance(), () -> {
            SunoPlayer player = SunoMC.getPlayer(event.getPlayer());
            Component message = event.message();
            SunoChatEvent call = new SunoChatEvent(player, message, new ChatIcon('♯', TextColor.color(0x149BBC)));

            RpgCore.getInstance().getServer().getPluginManager().callEvent(call);

            if (!call.isCancelled()) {
                if (call.getTargetWorld() != null) {
                    call.getTargetWorld().getPlayers().forEach(target -> target.sendMessage(ChatBuilder.buildChatMessage(player.getName(), message)));
                } else {
                    Bukkit.getOnlinePlayers().forEach(target -> target.sendMessage(ChatBuilder.buildChatMessage(player.getName(), message)));
                }
            }

        });
        event.setCancelled(true);
    }

}
