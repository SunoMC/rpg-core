package net.sunomc.rpg.game.common;

import lombok.Getter;
import net.kyori.adventure.text.Component;

public record Character(
        Component name,
        String id,
        int imgSpace
) {

    public static final int NPC_CHAR = 0xC000;

    // TODO : npc logik

    @Getter
    public enum Mood {
        NEUTRAL(0),
        SAD(1),
        ANGRY(2),
        SCARED(3),
        HAPPY(4),
        CONFUSED(5),
        BORED(6),
        EXCITED(7);

        private final int id;

        Mood(int id) {
            this.id = id;
        }
    }

    public static int getNpcChar() {
        return NPC_CHAR;
    }

}
