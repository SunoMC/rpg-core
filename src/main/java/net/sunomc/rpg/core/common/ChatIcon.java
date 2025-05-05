package net.sunomc.rpg.core.common;

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
}