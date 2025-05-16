package net.sunomc.rpg.game.registry;

import net.sunomc.rpg.game.common.Character;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A centralized registry for managing all NPCs (Non-Player Characters) in the game.
 * Provides thread-safe operations for adding, removing, and querying NPCs by their unique IDs.
 */
public final class NpcRegistry {
    private static final Map<String, Character> npcMap = new HashMap<>();

    /**
     * Registers a new NPC with a unique identifier.
     *
     * @param npc The {@link Character} instance to register.
     * @throws IllegalArgumentException if the NPC ID is already in use.
     */
    public static void registerNpc(Character npc) {
        if (npcMap.containsKey(npc.id())) {
            throw new IllegalArgumentException("NPC ID '" + npc.id() + "' is already registered.");
        }
        npcMap.put(npc.id(), npc);
    }

    /**
     * Unregisters an NPC by its ID.
     *
     * @param npcId The ID of the NPC to remove.
     * @return The removed {@link Character}, or empty if the ID was not found.
     */
    public static Character unregisterNpc(String npcId) {
        return npcMap.remove(npcId);
    }

    /**
     * Retrieves an NPC by its ID.
     *
     * @param npcId The ID of the NPC to fetch.
     * @return An {@link Optional} containing the NPC, or empty if not found.
     */
    public static @NotNull Character getNpc(String npcId) {
        return npcMap.get(npcId);
    }

    /**
     * Returns an immutable view of all registered NPCs.
     * Useful for iteration or bulk operations without modification risks.
     *
     * @return An unmodifiable {@link Map} of NPC IDs to their {@link Character} instances.
     */
    @Contract(pure = true)
    public static @NotNull @UnmodifiableView Map<String, Character> getAllNPCs() {
        return Collections.unmodifiableMap(npcMap);
    }

    /**
     * Checks if an NPC with the given ID exists.
     *
     * @param npcId The ID to check.
     * @return True if the NPC is registered, false otherwise.
     */
    public static boolean hasNpc(String npcId) {
        return npcMap.containsKey(npcId);
    }

    /**
     * Clears all registered NPCs from the registry.
     */
    public static void clear() {
        npcMap.clear();
    }
}