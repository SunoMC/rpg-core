package net.sunomc.rpg.game.common;

import lombok.Getter;
import net.kyori.adventure.text.Component;

public record Character(
        Component name,
        String id,
        int imgSpace
) {

    public static final String NPC_CHAR = "\uC000";

    // TODO : npc logik

    @Getter
    public enum Mood {
        HAPPY(0),
        SAD(1),
        ANGRY(2),
        SCARED(3),
        NEUTRAL(4),
        CONFUSED(5),
        BORED(6),
        EXCITED(7);

        public final int id;

        Mood(int id) {
            this.id = id;
        }
    }

    public static String getNpcChar() {
        return NPC_CHAR;
    }

}
