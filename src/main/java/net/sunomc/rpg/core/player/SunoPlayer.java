package net.sunomc.rpg.core.player;

import java.util.UUID;

import org.bukkit.entity.Player;

import net.sunomc.rpg.core.common.Group;
import net.sunomc.rpg.utlis.handler.GroupHandler;
import net.sunomc.rpg.utlis.data.MinecraftData;


/**
 * The SunoRPGPlayer Class that's contain data for other Systems<p>
 * <pre>
 * Vars: {@code craftPlayer} - is the ServerPlayer
 *       {@code playerData} - is the Space for data save</pre>
 */
public class SunoPlayer {
    private final Player craftPlayer;
    private final MinecraftData playerData;


    public SunoPlayer(Player craftPlayer) {
        this.craftPlayer = craftPlayer;
        this.playerData = new MinecraftData();
    }

    public void load() {
        // load logik oder so hier
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
     * Gets the data as a JSON string
     * @return JSON representation of player data
     */
    public String getDataAsJson() {
        return playerData.toJson();
    }

    /**
     * Gets the data as a YAML string
     * @return YAML representation of player data
     */
    public String getDataAsYaml() {
        return playerData.toYaml();
    }

    /**
     * @return The craftPlayer that is connected to the SunoPlayer
     */
    public Player getBukkitPlayer() {
        return craftPlayer;
    }

    /**
     * Getter with Nick and Vanish
     */
    public String getName() {
        return getData("suno.admin.nick.name", String.class, getOriginalName());
    }

    public String getOriginalName() {
        return craftPlayer.getName();
    }

    public UUID getUniqueId() {
        return UUID.fromString(getData("suno.admin.nick.uuid", String.class, getOriginalUniqueId().toString()));
    }

    public UUID getOriginalUniqueId() {
        return craftPlayer.getUniqueId();
    }

    public Group getGroup(){
        return GroupHandler.getGroupById(getData("suno.admin.nick.group", String.class, getOriginalGroup().id()));
    }

    public Group getOriginalGroup(){
        return GroupHandler.getGroupById(getData("suno.group.id", String.class, GroupHandler.getDefault().id()));
    }

    public boolean isVanished() {
        return getData("suno.admin.vanish", Boolean.class, true);
    }

    public boolean hasPermission(String permission) {
        return getOriginalGroup().hasPermission(permission);
    }

}