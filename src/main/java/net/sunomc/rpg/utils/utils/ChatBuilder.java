package net.sunomc.rpg.utils.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.sunomc.rpg.core.common.ChatIcon;

public class ChatBuilder {

    private static final TextColor BRACKET_COLOR = TextColor.color(0xFFFFFF);
    private static final TextColor ARROW_COLOR = TextColor.color(0x555555);
    private static final TextColor SYSTEM_COLOR = TextColor.color(0x55FF55);

    public static Component buildClickablePlayerName(String name, boolean suggestMsg) {
        return suggestMsg ? Component.text(name)
                .hoverEvent(HoverEvent.showText(
                        Component.text("Nachricht an " + name + " senden")
                                .color(TextColor.color(0xAAAAAA))
                ))
                .clickEvent(ClickEvent.suggestCommand("/msg " + name + " ")) : Component.text(name);
    }

    public static Component buildBracketedIcon(Component icon) {
        return Component.empty()
                .append(Component.text("[", BRACKET_COLOR))
                .append(icon)
                .append(Component.text("]", BRACKET_COLOR));
    }

    public static Component buildMessage(ChatIcon icon, String sender, Component message, boolean msg){
        return Component.text("")
                .append(buildBracketedIcon(icon.asComponent()))
                .append(Component.text(" "))
                .append(buildClickablePlayerName(sender, msg))
                .append(Component.text(" » ", ARROW_COLOR))
                .append(message);
    }

    public static Component buildChatMessage(String sender, Component message){
        return buildMessage(new ChatIcon('♯', TextColor.color(0x149BBC)), sender, message, true);
    }

    public static Component buildChatMessage(String sender, Component message, boolean msg){
        return buildMessage(new ChatIcon('♯', TextColor.color(0x149BBC)), sender, message, msg);
    }

    public static Component buildPrivatMessage(String sender, String recipient, Component message){
        return Component.text("")
                .append(buildBracketedIcon(new ChatIcon('✉', TextColor.color(0xFF5555)).asComponent()))
                .append(Component.text(" "))
                .append(buildClickablePlayerName(sender, true))
                .append(Component.text(" → ", ARROW_COLOR))
                .append(buildClickablePlayerName(recipient, true))
                .append(Component.text(" » ", ARROW_COLOR))
                .append(message);
    }

}
