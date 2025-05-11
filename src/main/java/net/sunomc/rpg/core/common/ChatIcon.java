package net.sunomc.rpg.core.common;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a chat icon consisting of a symbol (emoji/letter) with a specific color.
 */
public record ChatIcon(
        char symbol,
        TextColor color
) {
    /**
     * Creates a text component from this chat icon.
     * @return A colored component containing the icon symbol
     */
    @Contract(value = " -> new", pure = true)
    public @NotNull Component asComponent() {
        return Component.text(symbol, color);
    }

    /**
     * Creates a chat icon with the specified symbol and color.
     * @param symbol The emoji or letter to display
     * @param color The color of the icon
     * @return A new ChatIcon instance
     */
    @Contract("_, _ -> new")
    public static @NotNull ChatIcon of(char symbol, TextColor color) {
        return new ChatIcon(symbol, color);
    }

    public enum Preset {
        ERROR(new ChatIcon('✘',  TextColor.color(0xFF0000))),
        INFO(new ChatIcon('⚀',  TextColor.color(0xA55BA3))),
        QUESTION(new ChatIcon('?',  TextColor.color(0xF2D900))),
        SUCCESS(new ChatIcon('✔',  TextColor.color(0x00FF00))),
        FORTNITE(new ChatIcon('⚁',  TextColor.color(0x007EBD))),
        NETWORK(new ChatIcon('☄', TextColor.color(0xA000FF))),
        CHAT(new ChatIcon('♯', TextColor.color(0x149BBC))),
        MSG(new ChatIcon('✉', TextColor.color(0xFF8F6C)));

        private final ChatIcon icon;

        Preset(ChatIcon icon) {
            this.icon = icon;
        }

        public ChatIcon asIcon() {return icon;}
    }
}