package net.sunomc.rpg.utils.handler;

import net.sunomc.rpg.branding.ChatManager;
import net.sunomc.rpg.branding.MsgCommand;
import net.sunomc.rpg.core.player.PlayerListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class CommandHandler {
    private JavaPlugin plugin;

    public CommandHandler(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
    }

    public boolean registerAll() {
        try {
            Objects.requireNonNull(plugin.getCommand("msg")).setExecutor(new MsgCommand());
            Objects.requireNonNull(plugin.getCommand("r")).setExecutor(new MsgCommand());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
