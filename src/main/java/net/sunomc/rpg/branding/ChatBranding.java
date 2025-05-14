package net.sunomc.rpg.branding;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.papermc.paper.event.player.AsyncChatEvent;

import net.sunomc.rpg.RpgCore;
import net.sunomc.rpg.SunoMC;
import net.sunomc.rpg.core.common.ChatIcon;
import net.sunomc.rpg.core.events.SunoChatEvent;
import net.sunomc.rpg.core.common.SunoPlayer;

public class ChatBranding implements Listener {

    @EventHandler
    public void onChatEvent(AsyncChatEvent event) {
        Bukkit.getScheduler().runTask(RpgCore.getInstance(), () -> {
            SunoPlayer player = SunoMC.getPlayer(event.getPlayer());
            Component message = event.message().color(TextColor.color(0xAAAAAA));
            SunoChatEvent call = new SunoChatEvent(player, message, new ChatIcon('♯', TextColor.color(0x149BBC)));

            RpgCore.getInstance().getServer().getPluginManager().callEvent(call);

            if (!call.isCancelled()) {
                if (call.getTargetWorld() != null) {
                    call.getTargetWorld().getPlayers().forEach(target -> SunoMC.getPlayer(target).sendMessage(player.getServerPlayer(), message));
                } else {
                    Bukkit.getOnlinePlayers().forEach(target -> SunoMC.getPlayer(target).sendMessage(ChatIcon.Preset.NETWORK, player.getServerPlayer(), message));
                }
            }

        });
        event.setCancelled(true);
    }

}
