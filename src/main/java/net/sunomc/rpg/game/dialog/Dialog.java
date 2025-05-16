package net.sunomc.rpg.game.dialog;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.sunomc.rpg.game.common.Character;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Dialog {

    private final Character character;
    private final Character.Mood mood;

    private Component text;
    private Dialog next;
    private List<Action> actions;

    private Dialog(Character character, Character.Mood mood) {
        this.character = character;
        this.mood = mood;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Dialog from(Character character, Character.Mood mood) {
        return new Dialog(character, mood);
    }

    public Dialog text(Component text) {
        this.text = text;
        return this;
    }

    public Dialog addAction(Action action) {
        if (actions == null) actions = new ArrayList<>();
        this.actions.add(action);
        return this;
    }

    public Dialog next(Dialog next) {
        this.next = next;
        return this;
    }

}
