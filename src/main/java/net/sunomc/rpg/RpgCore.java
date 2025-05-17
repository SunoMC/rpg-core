package net.sunomc.rpg;

import lombok.Getter;
import net.sunomc.rpg.core.common.Language;
import net.sunomc.rpg.core.common.SunoPlayer;
import net.sunomc.rpg.core.handler.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class RpgCore extends JavaPlugin {

    @Getter
    private static RpgCore instance;
    private static Logger logger;

    @Override
    public void onEnable() {

        instance = this;
        logger = getLogger();

        GroupHandler.load();
        EventHandler.registerAll(Bukkit.getPluginManager(), this);
        PacketHandler.setup();
        CommandHandler.registerAll(this);
        TranslationHandler.setup(null); // default /.lang
        SqlHandler.getInstance("jdbc:mysql://localhost:3306/mysql", "minecraftdev", "minecraft", logger);

    }

    @Override
    public void onLoad() {
        Language.of("en-en.json");
        Bukkit.getOnlinePlayers().forEach(player -> SunoMC.addPlayer(new SunoPlayer(player)));
        SunoMC.setStatus(SunoMC.ServerStatus.ONLINE); // TODO : logik hier für online oder wartung
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Logger getConsol() {
        return logger;
    }

}
