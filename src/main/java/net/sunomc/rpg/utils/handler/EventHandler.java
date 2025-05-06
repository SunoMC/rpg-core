package net.sunomc.rpg.utils.handler;

import net.sunomc.rpg.core.player.PlayerListener;
import net.sunomc.rpg.game.branding.ChatManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EventHandler {
    private PluginManager manager;
    private JavaPlugin plugin;

    public EventHandler(PluginManager pluginManager, JavaPlugin javaPlugin) {
        manager = pluginManager;
        plugin = javaPlugin;
    }

    public boolean registerAll() {
        try {
            manager.registerEvents(new PlayerListener(), plugin);
            manager.registerEvents(new ChatManager(), plugin);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
