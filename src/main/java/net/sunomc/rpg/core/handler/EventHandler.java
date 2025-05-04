package net.sunomc.rpg.core.handler;

import net.sunomc.rpg.core.player.SunoPlayerListener;
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
            manager.registerEvents(new SunoPlayerListener(), plugin);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
