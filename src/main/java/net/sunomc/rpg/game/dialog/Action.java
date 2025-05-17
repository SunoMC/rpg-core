package net.sunomc.rpg.game.dialog;

import net.kyori.adventure.text.format.TextColor;
import net.sunomc.rpg.core.common.SunoPlayer;

import java.util.function.Consumer;

public record Action(
        String id,
        String text,
        TextColor color,
        Consumer<SunoPlayer> action
) {
    public void perform(SunoPlayer player) {
        action.accept(player);
    }
}
