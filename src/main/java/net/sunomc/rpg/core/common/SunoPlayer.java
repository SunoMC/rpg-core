package net.sunomc.rpg.core.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.sunomc.rpg.RpgCore;
import net.sunomc.rpg.SunoMC;
import net.sunomc.rpg.core.builder.ChatBuilder;
import net.sunomc.rpg.core.data.Data;
import net.sunomc.rpg.core.handler.SqlHandler;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.sunomc.rpg.core.handler.GroupHandler;
import net.sunomc.rpg.core.data.MinecraftData;
import org.jetbrains.annotations.NotNull;


/**
 * The SunoRPGPlayer Class that's contain data for other Systems<p>
 * <pre>
 * Vars: {@code bukkitPlayer} - is the ServerPlayer
 *       {@code playerData} - is the Space for data save</pre>
 */
public class SunoPlayer {
    private final Player bukkitPlayer;
    private MinecraftData playerData;

    @Getter
    private boolean firstTime;


    public SunoPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.playerData = new MinecraftData();
        firstTime = true;
    }

    public void initializeDefaultData() {
        LocalDateTime time = LocalDateTime.now();
        playerData.set("network.ip_address", Objects.requireNonNull(bukkitPlayer.getAddress()).getAddress().getHostAddress());
        playerData.set("network.ip.first_join", time);

        playerData.set("suno.group.id", GroupHandler.getDefault().id());
        
        playerData.set("suno.stats.playtime", 0L);
        playerData.set("suno.stats.login.count", 1);
        playerData.set("suno.stats.login.last", time);
    }

    public void load() {
        try {
            SqlHandler sqlHandler = SqlHandler.getInstance();

            sqlHandler.createTable("player_data",
                    "uuid VARCHAR(36) PRIMARY KEY, " +
                            "name VARCHAR(16), " +
                            "data TEXT, " +
                            "ip_address VARCHAR(45), " +  // IPv6 45 Zeichen
                            "first_join TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP");

            String query = "SELECT data, ip_address, first_join FROM player_data WHERE uuid = ?";
            try (PreparedStatement stmt = sqlHandler.prepareStatement(query)) {
                stmt.setString(1, getOriginalUniqueId().toString());

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String jsonData = rs.getString("data");
                    Timestamp firstJoin = rs.getTimestamp("first_join");

                    playerData = (MinecraftData) Data.generateFrom(jsonData, Data.Type.JSON);

                    playerData.set("network.ip_address", Objects.requireNonNull(bukkitPlayer.getAddress()).getAddress().getHostAddress());
                    playerData.set("network.first_join", firstJoin != null ? firstJoin.toLocalDateTime() : null);
                    firstTime = false;
                } else {
                    initializeDefaultData();
                }
            }
        } catch (SQLException e) {
            RpgCore.getInstance().getLogger().log(Level.SEVERE,
                    "Failed to load player data for " + getOriginalName(), e);
        }
    }

    public void save() {
        try {
            SqlHandler sqlHandler = SqlHandler.getInstance();

            String jsonData = this.playerData.to(Data.Type.JSON);
            String ipAddress = getData("network.ip_address", String.class);

            String query = "INSERT INTO player_data (uuid, name, data, ip_address) VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE data = ?, name = ?, ip_address = ?, last_updated = CURRENT_TIMESTAMP";

            try (PreparedStatement stmt = sqlHandler.prepareStatement(query)) {
                String uuidStr = getOriginalUniqueId().toString();

                stmt.setString(1, uuidStr);
                stmt.setString(2, getOriginalName());
                stmt.setString(3, jsonData);
                stmt.setString(4, ipAddress);
                stmt.setString(5, jsonData);
                stmt.setString(6, getOriginalName());
                stmt.setString(7, ipAddress);

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            RpgCore.getInstance().getLogger().log(Level.SEVERE,
                    "Failed to save player data for " + getOriginalName(), e);
        }
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
     * Gets the listener's current display name, which may be modified by nicknames.
     * Falls back to the original name if no nickname is set.
     *
     * @return The listener's current display name (nickname if set, otherwise original name)
     */
    public String getName() {
        return playerData.get("suno.stuff.nick.name", String.class, getOriginalName());
    }

    /**
     * Gets the listener's original Minecraft username, unaffected by nicknames.
     *
     * @return The listener's original Minecraft username
     */
    public String getOriginalName() {
        return bukkitPlayer.getName();
    }

    /**
     * Gets the listener's current UUID, which may be modified by nick systems.
     * Falls back to the original UUID if no modified UUID is set.
     *
     * @return The listener's current UUID (modified if nick system is active, otherwise original)
     */
    public UUID getUniqueId() {
        return playerData.get("suno.stuff.nick.uuid", UUID.class, getOriginalUniqueId());
    }

    /**
     * Gets the listener's original Minecraft UUID, unaffected by nick systems.
     *
     * @return The listener's original Minecraft UUID
     */
    public UUID getOriginalUniqueId() {
        return bukkitPlayer.getUniqueId();
    }

    /**
     * Gets the listener's current group, which may be modified by nick systems.
     * Falls back to the original group if no modified group is set.
     *
     * @return The listener's current group (modified if nick system is active, otherwise original)
     */
    public Group getGroup() {
        return GroupHandler.getGroupById(playerData.get("suno.stuff.nick.group", String.class, getOriginalGroup().id()));
    }

    /**
     * Gets the listener's original group, unaffected by nick systems.
     *
     * @return The listener's original group
     */
    public Group getOriginalGroup() {
        return GroupHandler.getGroupById(playerData.get("suno.group.id", String.class, GroupHandler.getDefault().id()));
    }

    /**
     * Gets the listener's current location in the world.
     *
     * @return The listener's current location
     */
    public Location getLocation() {
        return bukkitPlayer.getLocation();
    }

    /**
     * Checks if the listener is currently vanished (hidden from other players).
     *
     * @return true if the listener is vanished, false otherwise
     */
    public boolean isVanished() {
        return playerData.get("suno.stuff.vanish", Boolean.class, false);
    }

    /**
     * Checks if the listener has a specific permission, based on their original group.
     *
     * @param permission The permission string to check
     * @return true if the listener has the permission, false otherwise
     */
    public boolean hasPermission(String permission) {
        return getOriginalGroup().hasPermission(permission);
    }

    /**
     * Gets the listener's body yaw rotation value.
     *
     * @return The listener's body yaw rotation in degrees default 0
     */
    public float getBodyYaw() {
        if(bukkitPlayer instanceof LivingEntity entity) {
            return entity.getBodyYaw();
        }
        return 0;
    }

    /**
     * Sends a formatted message to the listener with specified message type.
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
     * Sends a fully customizable formatted message to the listener.
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
     * Sends a standard chat message to the listener.
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
     * Sends a standard chat message to the listener.
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