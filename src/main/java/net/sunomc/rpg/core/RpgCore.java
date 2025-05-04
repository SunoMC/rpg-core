package net.sunomc.rpg.core;

import lombok.Getter;

import net.sunomc.rpg.core.handler.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.sunomc.rpg.core.handler.GroupHandler;

public final class RpgCore extends JavaPlugin {

    @Getter
    private static RpgCore instance;

    @Override
    public void onEnable() {
        GroupHandler groupHandler = new GroupHandler();
        EventHandler eventHandler = new EventHandler(Bukkit.getPluginManager(), this);

        groupHandler.load();
        eventHandler.registerAll();
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
