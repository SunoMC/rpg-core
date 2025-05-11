package net.sunomc.rpg.core.player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.sunomc.rpg.SunoMC;
import net.sunomc.rpg.core.common.ChatIcon;
import net.sunomc.rpg.utils.utils.ChatBuilder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.sunomc.rpg.core.common.Group;
import net.sunomc.rpg.utils.handler.GroupHandler;
import net.sunomc.rpg.utils.data.MinecraftData;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.GameMode.*;


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

    public SunoPlayer load() {
        // load logik oder so hier
        return this;
    }

    public SunoPlayer save() {
        // save logik oder so hier
        return this;
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
     * @return The player's body yaw rotation in degrees default 0
     */
    public float getBodyYaw() {
        if(bukkitPlayer instanceof LivingEntity entity) {
            return entity.getBodyYaw();
        }
        return 0;
    }

    /**
     * Sends a formatted message to the player with specified message type.
     *
     * @param type The type of message which determines the icon and styling
     * @param sender The component representing the message sender
     * @param message The content of the message to send
     * @param hoverMsg Whether the sender name should be clickable for messaging
     */
    public void sendMessage(@NotNull ChatIcon.Preset type,
                            Component sender,
                            Component message,
                            boolean hoverMsg) {
        sendMessage(ChatBuilder.buildMessage(type.asIcon(), sender, message, hoverMsg));
    }

    /**
     * Sends a fully customizable formatted message to the player.
     *
     * @param icon The ChatIcon to display with the message
     * @param sender The component representing the message sender
     * @param message The content of the message to send
     * @param hoverMsg Whether the sender name should be clickable for messaging
     */
    public void sendMessage(ChatIcon icon,
                            Component sender,
                            Component message,
                            boolean hoverMsg) {
        sendMessage(ChatBuilder.buildMessage(icon, sender, message, hoverMsg));
    }

    /**
     * Sends a standard chat message to the player.
     * Automatically uses the sender's name and enables message hover/click functionality.
     * @param type The type of message which determines the icon and styling
     * @param sender The Player object representing the message sender
     * @param message The content of the message to send
     */
    public void sendMessage(ChatIcon.Preset type,
                            Player sender,
                            Component message) {
        Component senderName = Component.text(SunoMC.getPlayer(sender).getName());
        boolean msg = true;

        if (sender.getUniqueId().equals(bukkitPlayer.getUniqueId())) {
            senderName = Component.text("You").color(TextColor.color(0xe8c9f5));
            msg = false;
        }

        sendMessage(ChatBuilder.buildMessage(type.asIcon(), senderName, message, msg));
    }

    /**
     * Sends a standard chat message to the player.
     * Automatically uses the sender's name and enables message hover/click functionality.
     *
     * @param sender The Player object representing the message sender
     * @param message The content of the message to send
     */
    public void sendMessage(Player sender,
                            Component message) {
        sendMessage(ChatIcon.Preset.CHAT, sender, message);
    }

    // TODO : do docs
    public void sendMessage(Component message) {
        bukkitPlayer.sendMessage(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SunoPlayer sunoPlayer = (SunoPlayer) obj;

        return this.getUniqueId().equals(sunoPlayer.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }

}