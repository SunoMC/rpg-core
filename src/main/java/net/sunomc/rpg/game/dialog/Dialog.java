package net.sunomc.rpg.game.dialog;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.sunomc.rpg.game.common.Character;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Dialog {
    private final Character character;
    private final Character.Mood mood;
    private Component text;
    private Dialog next;
    private List<Action> actions;
    private Rule actionRule = Rule.ALL_BEFORE_NEXT; // Default rule

    private Dialog(Character character, Character.Mood mood) {
        this.character = Objects.requireNonNull(character, "Character cannot be null");
        this.mood = Objects.requireNonNull(mood, "Mood cannot be null");
    }

    @Contract("_, _ -> new")
    public static @NotNull Dialog from(Character character, Character.Mood mood) {
        return new Dialog(character, mood);
    }

    public Dialog text(Component text) {
        this.text = Objects.requireNonNull(text, "Text cannot be null");
        return this;
    }

    public Dialog addAction(Action action) {
        if (actions == null) actions = new ArrayList<>();
        this.actions.add(Objects.requireNonNull(action, "Action cannot be null"));
        return this;
    }

    public Dialog next(@Nullable Dialog next) {
        this.next = next;
        return this;
    }

    public Dialog actionRule(Rule rule) {
        this.actionRule = Objects.requireNonNull(rule, "Rule cannot be null");
        return this;
    }

    public enum Rule {
        /**
         * All actions must be completed before progressing to next dialog
         */
        ALL_BEFORE_NEXT,
        /**
         * Only one action needs to be completed to progress
         */
        ONE_BEFORE_NEXT,
        /**
         * Actions are optional, dialog can progress without completion
         */
        OPTIONAL
    }

    public record Action(String title, String actionId) {
        public Action {
            Objects.requireNonNull(title, "Title cannot be null");
            Objects.requireNonNull(actionId, "Action ID cannot be null");
        }
    }
}