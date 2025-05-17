package net.sunomc.rpg.core.listener;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.sunomc.rpg.RpgCore;
import net.sunomc.rpg.SunoMC;
import net.sunomc.rpg.core.common.ChatIcon;
import net.sunomc.rpg.core.common.SunoPlayer;
import net.sunomc.rpg.core.events.PacketReceiveEvent;
import net.sunomc.rpg.core.events.PacketSendEvent;
import net.sunomc.rpg.core.events.SunoPlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        SunoPlayer player = new SunoPlayer(event.getPlayer());
        SunoMC.addPlayer(player);

        String ip = Objects.requireNonNull(player.getServerPlayer().getAddress()).getAddress().getHostAddress();
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int loginCount = player.getData("suno.stats.login.count", Integer.class, 0);

        player.setData("network.ip_address", ip);
        player.setData("network.login.ip.last", ip);
        player.setData("network.login.ip." + time, ip);

        player.setData("suno.stats.login.count", loginCount);
        player.setData("suno.stats.login.last", time);

        RpgCore.getInstance().getServer().getPluginManager().callEvent(new SunoPlayerJoinEvent(player));

    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        SunoPlayer sunoPlayer = SunoMC.getPlayer(event.getPlayer());
        SunoMC.removePlayer(sunoPlayer);
    }

    @EventHandler
    public void onPacketReceiving(@NotNull PacketReceiveEvent event) {

    }

    @EventHandler
    public void onPacketSending(@NotNull PacketSendEvent event) {
        if (event.getType().equals(PacketType.Play.Server.CHAT)) {
            modifyChatPacket(event.getPacket());
        }
    }

    private void modifyChatPacket(PacketContainer packet) {
        WrappedChatComponent wrapped = packet.getChatComponents().read(0);
        if (wrapped == null) return;

        Component originalComponent = GsonComponentSerializer.gson().deserialize(wrapped.getJson());
        String plainText = PlainTextComponentSerializer.plainText().serialize(originalComponent);

        if (!matchesBracketPattern(plainText)) {
            Component modifiedComponent = Component.text()
                    .append(Component.text("["))
                    .append(ChatIcon.Preset.NETWORK.asIcon().asComponent())
                    .append(Component.text("] "))
                    .append(originalComponent)
                    .build();

            packet.getChatComponents().write(0, WrappedChatComponent.fromJson(
                    GsonComponentSerializer.gson().serialize(modifiedComponent)
            ));
        }
    }

    private boolean matchesBracketPattern(String message) {
        if (message.length() < 3) return false;
        return message.charAt(0) == '[' && message.charAt(2) == ']';
    }

}
