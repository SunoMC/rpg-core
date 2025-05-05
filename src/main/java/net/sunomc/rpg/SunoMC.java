package net.sunomc.rpg;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.sunomc.rpg.core.player.SunoPlayer;

/**
 * The Suno class manages online players in the RPG system.
 * It provides methods to add, retrieve, and manage SunoPlayer instances.
 *
 * <p>This class maintains a set of all currently online SunoPlayers
 * and offers utility methods to access them by various identifiers.</p>
 */
public final class SunoMC {

    /**
     * A set containing all currently online SunoPlayer instances.
     */
    public static Set<SunoPlayer> onlinePlayers;

    static {
        onlinePlayers = new HashSet<>();
    }

    /**
     * Adds a player to the online player set if not already present.
     *
     * @param player the SunoPlayer to add
     */
    public static void addPlayer(SunoPlayer player) {
        if (onlinePlayers.contains(player)) {
            return;
        }
        onlinePlayers.add(player);
    }

    /**
     * Removes a player from the online player set if present.
     *
     * @param player the SunoPlayer to remove
     */
    public static void removePlayer(SunoPlayer player) {
        if (!onlinePlayers.contains(player)) {
            return;
        }
        onlinePlayers.remove(player);
    }

    /**
     * Gets all online SunoPlayers.
     *
     * @return an unmodifiable set of all online SunoPlayers
     */
    public static Set<SunoPlayer> getPlayers() {return onlinePlayers;}

    /**
     * Gets a SunoPlayer by their name.
     *
     * @param name the name of the player to retrieve
     * @return the SunoPlayer with the specified name, or null if not found
     */
    public static SunoPlayer getPlayer(String name) {return getPlayer(Bukkit.getOfflinePlayer(name).getUniqueId());};

    /**
     * Gets a SunoPlayer by their Bukkit Player instance.
     *
     * @param player the Bukkit Player instance
     * @return the corresponding SunoPlayer, or null if not found
     * @throws NullPointerException if player is null
     */
    public static SunoPlayer getPlayer(@NotNull Player player) {return getPlayer(player.getUniqueId());}

    /**
     * Gets a SunoPlayer by their UUID.
     *
     * @param uuid the UUID of the player to retrieve
     * @return the SunoPlayer with the specified UUID, or null if not found
     */
    public static SunoPlayer getPlayer(UUID uuid) {
        return onlinePlayers.stream()
                .filter(suno -> suno.getUniqueId().equals(uuid))
                .findFirst()
                .orElse(null);
    }
}