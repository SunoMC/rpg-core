package net.sunomc.rpg.core.handler;

import java.util.Objects;

import org.bukkit.plugin.java.JavaPlugin;

import net.sunomc.rpg.core.commands.MsgCommand;


public final class CommandHandler {
    private static JavaPlugin plugin;

    private CommandHandler() {}

    public static boolean registerAll(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
        try {
            Objects.requireNonNull(plugin.getCommand("msg")).setExecutor(new MsgCommand());
            Objects.requireNonNull(plugin.getCommand("r")).setExecutor(new MsgCommand());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
