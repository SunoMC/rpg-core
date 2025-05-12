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

        new GroupHandler().load();
        new EventHandler(Bukkit.getPluginManager(), this).registerAll();
        new PacketHandler();
        new CommandHandler(this).registerAll();
        new TranslationHandler().setup(null);

        try {
            TranslationHandler.loadLanguageFile("en-en");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
