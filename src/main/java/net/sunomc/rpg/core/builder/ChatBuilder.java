package net.sunomc.rpg.core.builder;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;

import net.sunomc.rpg.core.common.ChatIcon;


/**
 * Utility class for building formatted chat messages with consistent styling.
 * Provides methods to create chat messages with icons, clickable listener names,
 * and various message types (public, private, system).
 */
public class ChatBuilder {

    private static final TextColor BRACKET_COLOR = TextColor.color(0xFFFFFF);
    private static final TextColor ARROW_COLOR = TextColor.color(0x555555);

    /**
     * Builds a clickable listener name component.
     *
     * @param nameComponent The component representing the listener's name
     * @param suggestMsg Whether to make the name clickable for sending messages
     * @return Component with the listener name, optionally with click/hover events
     */
    public static Component buildClickablePlayerName(Component nameComponent,
                                                     boolean suggestMsg) {
        if (!suggestMsg) {
            return nameComponent;
        }

        String playerName = PlainTextComponentSerializer.plainText().serialize(nameComponent);

        return nameComponent
                .hoverEvent(HoverEvent.showText(
                        Component.text("Click to send a message to ").color(TextColor.color(0xAAAAAA))
                                .append(nameComponent)
                ))
                .clickEvent(ClickEvent.suggestCommand("/msg " + playerName + " "));
    }

    /**
     * Wraps an icon component in brackets with consistent styling.
     *
     * @param icon The icon component to wrap
     * @return Component containing the bracketed icon
     */
    public static Component buildBracketedIcon(Component icon) {
        return Component.empty()
                .append(Component.text("[", BRACKET_COLOR))
                .append(icon)
                .append(Component.text("]", BRACKET_COLOR));
    }

    /**
     * Builds a formatted message with icon, sender, and message content.
     *
     * @param icon The chat icon to display
     * @param senderComponent The component representing the message sender
     * @param message The message content
     * @param msg Whether the sender name should be clickable for messaging
     * @return Fully formatted message component
     */
    public static @NotNull Component buildMessage(@NotNull ChatIcon icon,
                                                  Component senderComponent,
                                                  Component message,
                                                  boolean msg) {
        return Component.text("")
                .append(buildBracketedIcon(icon.asComponent()))
                .append(Component.text(" "))
                .append(buildClickablePlayerName(senderComponent, msg))
                .append(Component.text(" » ", ARROW_COLOR))
                .append(message);
    }

    /**
     * Builds a standard chat message with default settings.
     *
     * @param senderComponent The component representing the message sender
     * @param message The message content
     * @return Formatted chat message component
     */
    public static @NotNull Component buildChatMessage(Component senderComponent,
                                                      Component message) {
        return buildMessage(ChatIcon.Preset.CHAT.asIcon(), senderComponent, message, true);
    }

    /**
     * Builds a standard chat message with configurable messaging.
     *
     * @param senderComponent The component representing the message sender
     * @param message The message content
     * @param msg Whether the sender name should be clickable for messaging
     * @return Formatted chat message component
     */
    public static @NotNull Component buildChatMessage(Component senderComponent,
                                                      Component message,
                                                      boolean msg) {
        return buildMessage(ChatIcon.Preset.CHAT.asIcon(), senderComponent, message, msg);
    }

    /**
     * Builds a private message between two players.
     *
     * @param senderComponent The component representing the message sender
     * @param recipientComponent The component representing the message recipient
     * @param message The message content
     * @return Formatted private message component
     */
    public static @NotNull Component buildPrivateMessage(Component senderComponent,
                                                         Component recipientComponent,
                                                         Component message,
                                                         boolean isSender) {
        return Component.text("")
                .append(buildBracketedIcon(ChatIcon.Preset.MSG.asIcon().asComponent()))
                .append(Component.text(" "))
                .append(buildClickablePlayerName(senderComponent, !isSender))
                .append(Component.text(" → ", ARROW_COLOR))
                .append(buildClickablePlayerName(recipientComponent, isSender))
                .append(Component.text(" » ", ARROW_COLOR))
                .append(message);
    }
}