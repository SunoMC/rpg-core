package net.sunomc.rpg;

import lombok.Getter;

import net.sunomc.rpg.core.common.SunoPlayer;
import net.sunomc.rpg.core.handler.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class RpgCore extends JavaPlugin {

    @Getter
    private static RpgCore instance;

    @Override
    public void onEnable() {

        instance = this;

        Bukkit.getOnlinePlayers().forEach(player -> SunoMC.addPlayer(new SunoPlayer(player)));

        GroupHandler groupHandler = new GroupHandler();
        EventHandler eventHandler = new EventHandler(Bukkit.getPluginManager(), this);
        PacketHandler packetHandler = new PacketHandler();
        CommandHandler commandHandler = new CommandHandler(this);
        TranslationHandler translationHandler = new TranslationHandler(null);

        try {
            TranslationHandler.loadLanguageFile("en-en");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
