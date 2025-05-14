package net.sunomc.rpg.branding;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import net.sunomc.rpg.RpgCore;
import net.sunomc.rpg.SunoMC;
import net.sunomc.rpg.core.events.PacketSendEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MotdBranding implements Listener {

    private final Random random = new Random();

    // MOTD options for different statuses and permissions
    private final List<String> OFFLINE_STUFF_MOTDS = Arrays.asList(
            "§4§lSunoMC RPG §8- §cOffline Mode",
            "§cServer is currently offline §8- §7Maintenance ongoing",
            "§4§l⚠ OFFLINE MODE ⚠ §8- §cOnly staff can join"
    );

    private final List<String> OFFLINE_NORMAL_MOTDS = Arrays.asList(
            "§4§lSunoMC RPG §8- §cServer Offline",
            "§cServer is currently unavailable",
            "§4§lServer Maintenance §8- §cPlease check back later"
    );

    private final List<String> MAINTENANCE_STUFF_MOTDS = Arrays.asList(
            "§6§lSunoMC RPG §8- §eMaintenance Mode",
            "§eServer is under maintenance §8- §7Staff only",
            "§6§l⚠ MAINTENANCE ⚠ §8- §eOnly staff can join"
    );

    private final List<String> MAINTENANCE_NORMAL_MOTDS = Arrays.asList(
            "§6§lSunoMC RPG §8- §eMaintenance",
            "§eServer is currently under maintenance",
            "§6§lServer Maintenance §8- §eWe'll be back soon!"
    );

    private final List<String> ONLINE_STUFF_MOTDS = Arrays.asList(
            "§a§lSunoMC RPG §8- §2v1.21.4",
            "§2Welcome back, staff member!",
            "§a§lSunoMC RPG §8- §7Staff Mode"
    );

    private final List<String> ONLINE_NORMAL_MOTDS = Arrays.asList(
            "§a§lSunoMC RPG §8- §2Join now!",
            "§2Adventure awaits! §8- §7v1.21.4",
            "§a§lSunoMC RPG §8- §7New updates available!"
    );

    @EventHandler
    public void onServerInfo(@NotNull PacketSendEvent event) {
        if(event.getType() == PacketType.Status.Server.SERVER_INFO) {
            WrappedServerPing ping = event.getPacket().getServerPings().read(0);

            // Get player UUID if available (for permission checks)
            UUID playerUUID = event.getPlayer() != null ? event.getPlayer().getUniqueId() : null;
            boolean isStaff = playerUUID != null && isStaffMember(playerUUID);

            setMotD(ping, isStaff);
            setFavicon(ping);
            setVersion(ping, isStaff);
        }
    }

    private boolean isStaffMember(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        if (player == null) return false;

        // Check for staff permission (adjust as needed)
        return player.hasPermission("sunomc.staff") ||
                player.isOp();
    }

    private void setVersion(@NotNull WrappedServerPing ping, boolean isStaff) {
        SunoMC.ServerStatus status = SunoMC.getStatus();

        switch (status) {
            case OFFLINE -> {
                ping.setVersionName("§4Offline");
                ping.setVersionProtocol(0);
            }

            case MAINTENANCE -> {
                if (isStaff) {
                    ping.setVersionName("§6Maintenance §8- §eStaff Access");
                    ping.setVersionProtocol(766);
                } else {
                    ping.setVersionName("§6Maintenance");
                    ping.setVersionProtocol(0);
                }
            }

            default -> {
                ping.setVersionName("§aSunoMC v1.21.4");
                ping.setVersionProtocol(766);
            }

        }
    }

    private void setMotD(@NotNull WrappedServerPing ping, boolean isStaff) {
        String header;
        String footer = "§7play.sunomc.net";

        SunoMC.ServerStatus status = SunoMC.getStatus();
        String motd = switch (status) {
            case OFFLINE -> isStaff ?
                    getRandomMotd(OFFLINE_STUFF_MOTDS) :
                    getRandomMotd(OFFLINE_NORMAL_MOTDS);
            case MAINTENANCE -> isStaff ?
                    getRandomMotd(MAINTENANCE_STUFF_MOTDS) :
                    getRandomMotd(MAINTENANCE_NORMAL_MOTDS);
            default -> isStaff ?
                    getRandomMotd(ONLINE_STUFF_MOTDS) :
                    getRandomMotd(ONLINE_NORMAL_MOTDS);
        };

        ping.setMotD(motd + "\n" + footer);
    }

    private String getRandomMotd(@NotNull List<String> motds) {
        return motds.get(random.nextInt(motds.size()));
    }

    private void setFavicon(@NotNull WrappedServerPing ping) {
        try {
            File iconFile = new File(RpgCore.getInstance().getDataFolder(),
                    SunoMC.getStatus().name().toLowerCase() + "-icon.png");

            if (iconFile.exists()) {
                BufferedImage icon = ImageIO.read(iconFile);
                ping.setFavicon(WrappedServerPing.CompressedImage.fromPng(icon));
            }
        } catch (IOException e) {
            RpgCore.getConsol().warning("Server-Icon konnte nicht geladen werden!");
        }
    }
}