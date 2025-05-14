package net.sunomc.rpg.core.handler;

import net.sunomc.rpg.branding.MotdBranding;
import net.sunomc.rpg.core.listener.PlayerListener;
import net.sunomc.rpg.branding.ChatBranding;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventHandler {
    private static PluginManager manager;
    private static JavaPlugin plugin;

    private EventHandler() {}

    public static void registerAll(PluginManager pluginManager, JavaPlugin javaPlugin) {
        manager = pluginManager;
        plugin = javaPlugin;

        try {
            manager.registerEvents(new PlayerListener(), plugin);
            manager.registerEvents(new ChatBranding(), plugin);
            manager.registerEvents(new MotdBranding(), plugin);
        } catch (Exception ignored) {
        }
    }

}
