package net.sunomc.rpg.core.handler;

import java.util.Objects;

import org.bukkit.plugin.java.JavaPlugin;

import net.sunomc.rpg.branding.MsgCommand;


public final class CommandHandler {
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
