package net.sunomc.rpg.core.handler;

import net.sunomc.rpg.core.listener.PlayerListener;
import net.sunomc.rpg.branding.ChatManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventHandler {
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
