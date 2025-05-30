package net.sunomc.rpg.core.handler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;
import lombok.Getter;

import net.sunomc.rpg.core.common.Group;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

@Getter
public final class GroupHandler {
    private static Group defaultGroup;
    private static Set<Group> groups;
    private Scoreboard scoreboard;
    private static boolean loaded;

    private static final Group FALLBACK = new Group("fallback", "fallback", "", 0, true, Set.of());

    private GroupHandler() {}

    private void setup() {
        groups = new HashSet<>();
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        if(!loaded) load();
    }

    /**
     * Load all groups for the GroupHandler
     */
    public static void load() {
        if(loaded) return;

        Set<Group> loadedGroups = new HashSet<>();
        // Set to DB later
        loadedGroups.add(new Group("admin", "AdminToll", "", 100, false, Set.of("suno.admin.*")));
        loadedGroups.add(new Group("mod", "Mod der Beste", "", 50, false, Set.of("suno.mod.*")));
        loadedGroups.add(new Group("player", "Player und so", "", 0, true, Set.of("suno.listener.*")));

        defaultGroup = loadedGroups.stream()
                .filter(Group::isDefault)
                .findFirst()
                .orElse(FALLBACK);

        loadedGroups.add(defaultGroup);

        groups = Collections.unmodifiableSet(loadedGroups);
        loaded = true;
    }

    /**
     * Get the Group with the same ID
     *
     * @param id The id to search for
     * @return The Group or {@code null} with the id
     */
    public static @Nullable Group getGroupById(String id) {
        return groups.stream()
                .filter(group -> group.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public static @NotNull Group getDefault() {
        return defaultGroup != null ? defaultGroup : FALLBACK;
    }

}
