package net.sunomc.rpg;

import lombok.Getter;

import net.sunomc.rpg.core.player.SunoPlayer;
import net.sunomc.rpg.utils.handler.CommandHandler;
import net.sunomc.rpg.utils.handler.EventHandler;
import net.sunomc.rpg.utils.handler.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.sunomc.rpg.utils.handler.GroupHandler;

import java.io.File;

public final class RpgCore extends JavaPlugin {

    @Getter
    private static RpgCore instance;

    @Override
    public void onEnable() {

        instance = this;

        Bukkit.getOnlinePlayers().forEach(player -> SunoMC.addPlayer(new SunoPlayer(player).load()));

        GroupHandler groupHandler = new GroupHandler();
        EventHandler eventHandler = new EventHandler(Bukkit.getPluginManager(), this);
        PacketHandler packetHandler = new PacketHandler();
        CommandHandler commandHandler = new CommandHandler(this);

        groupHandler.load();
        eventHandler.registerAll();
        commandHandler.registerAll();
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
