package net.sunomc.rpg.core;


import lombok.Getter;
import net.sunomc.rpg.core.group.GroupHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class RpgCore extends JavaPlugin {
    private static GroupHandler handler;

    @Getter
    private static RpgCore instance;

    @Override
    public void onEnable() {
         handler = new GroupHandler();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
