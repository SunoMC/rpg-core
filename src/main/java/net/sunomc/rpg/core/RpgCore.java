package net.sunomc.rpg.core;


import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class RpgCore extends JavaPlugin {

    @Getter
    private static RpgCore instance;

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
