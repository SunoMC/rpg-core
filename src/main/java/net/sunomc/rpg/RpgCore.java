package net.sunomc.rpg;

import lombok.Getter;

import net.sunomc.rpg.utils.handler.EventHandler;
import net.sunomc.rpg.utils.handler.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.sunomc.rpg.utils.handler.GroupHandler;

public final class RpgCore extends JavaPlugin {

    @Getter
    private static RpgCore instance;

    @Override
    public void onEnable() {

        instance = this;

        GroupHandler groupHandler = new GroupHandler();
        EventHandler eventHandler = new EventHandler(Bukkit.getPluginManager(), this);
        PacketHandler handler = new PacketHandler();
        SunoMC sunoMC = new SunoMC();

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
