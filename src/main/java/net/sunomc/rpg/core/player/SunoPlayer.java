package net.sunomc.rpg.core.player;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.sunomc.rpg.core.common.Group;
import net.sunomc.rpg.utils.handler.GroupHandler;
import net.sunomc.rpg.utils.data.MinecraftData;


/**
 * The SunoRPGPlayer Class that's contain data for other Systems<p>
 * <pre>
 * Vars: {@code bukkitPlayer} - is the ServerPlayer
 *       {@code playerData} - is the Space for data save</pre>
 */
public class SunoPlayer {
    private final Player bukkitPlayer;
    private final MinecraftData playerData;


    public SunoPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.playerData = new MinecraftData();
    }

    public void load() {
        // load logik oder so hier
    }

    public void save() {
        // save logik oder so hier
    }

    /**
     * Sets data at the specified path
     * @param path Path to the data (e.g. "info.stats.strength")
     * @param value Value to store
     */
    public void setData(String path, Object value) {
        playerData.set(path, value);
    }

    /**
     * Gets data from the specified path
     * @param path Path to the data
     * @param type Class type to cast the result to
     * @param <T> Type parameter
     * @return The value at the specified path, cast to the requested type, or null if not found
     */
    public <T> T getData(String path, Class<T> type) {
        return playerData.get(path, type);
    }

    /**
     * Gets data from the specified path with a default value if not found or not of correct type
     * @param path Path to the data
     * @param type Class type to cast the result to
     * @param defaultValue Default value to return if path not found or type doesn't match
     * @param <T> Type parameter
     * @return The value at the specified path, cast to the requested type, or the default value if not found or wrong type
     */
    public <T> T getData(String path, Class<T> type, T defaultValue) {
        return playerData.get(path, type, defaultValue);
    }

    /**
     * Retrieves the Bukkit Player instance associated with this SunoPlayer.
     *
     * @return The bukkitPlayer instance connected to this SunoPlayer
     */
    public Player getServerPlayer() {
        return bukkitPlayer;
    }

    /**
     * Gets the player's current display name, which may be modified by nicknames.
     * Falls back to the original name if no nickname is set.
     *
     * @return The player's current display name (nickname if set, otherwise original name)
     */
    public String getName() {
        return playerData.get("suno.admin.nick.name", String.class, getOriginalName());
    }

    /**
     * Gets the player's original Minecraft username, unaffected by nicknames.
     *
     * @return The player's original Minecraft username
     */
    public String getOriginalName() {
        return bukkitPlayer.getName();
    }

    /**
     * Gets the player's current UUID, which may be modified by nick systems.
     * Falls back to the original UUID if no modified UUID is set.
     *
     * @return The player's current UUID (modified if nick system is active, otherwise original)
     */
    public UUID getUniqueId() {
        return playerData.get("suno.admin.nick.uuid", UUID.class, getOriginalUniqueId());
    }

    /**
     * Gets the player's original Minecraft UUID, unaffected by nick systems.
     *
     * @return The player's original Minecraft UUID
     */
    public UUID getOriginalUniqueId() {
        return bukkitPlayer.getUniqueId();
    }

    /**
     * Gets the player's current group, which may be modified by nick systems.
     * Falls back to the original group if no modified group is set.
     *
     * @return The player's current group (modified if nick system is active, otherwise original)
     */
    public Group getGroup() {
        return GroupHandler.getGroupById(playerData.get("suno.admin.nick.group", String.class, getOriginalGroup().id()));
    }

    /**
     * Gets the player's original group, unaffected by nick systems.
     *
     * @return The player's original group
     */
    public Group getOriginalGroup() {
        return GroupHandler.getGroupById(playerData.get("suno.group.id", String.class, GroupHandler.getDefault().id()));
    }

    /**
     * Gets the player's current location in the world.
     *
     * @return The player's current location
     */
    public Location getLocation() {
        return bukkitPlayer.getLocation();
    }

    /**
     * Checks if the player is currently vanished (hidden from other players).
     *
     * @return true if the player is vanished, false otherwise
     */
    public boolean isVanished() {
        return playerData.get("suno.admin.vanish", Boolean.class, false);
    }

    /**
     * Checks if the player has a specific permission, based on their original group.
     *
     * @param permission The permission string to check
     * @return true if the player has the permission, false otherwise
     */
    public boolean hasPermission(String permission) {
        return getOriginalGroup().hasPermission(permission);
    }

    /**
     * Gets the player's body yaw rotation value.
     *
     * @return The player's body yaw rotation in degrees
     */
    public float getBodyYaw() {
        return playerData.get("minecraft.player.body_yaw", Float.class, 0f);
    }

    /**
     * Updates the player's body yaw rotation value.
     * This is typically called by the {@link PlayerListener} when handling player movement events.
     *
     * @param yaw The new body yaw rotation value in degrees
     */
    protected void setBodyYaw(float yaw) {
        playerData.set("minecraft.player.body_yaw", yaw);
    }

}