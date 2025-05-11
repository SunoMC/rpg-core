package net.sunomc.rpg.branding;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.sunomc.rpg.SunoMC;
import net.sunomc.rpg.core.common.ChatIcon;
import net.sunomc.rpg.core.common.SunoPlayer;
import net.sunomc.rpg.core.builder.ChatBuilder;
import net.sunomc.rpg.core.handler.TranslationHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MsgCommand implements CommandExecutor {
    private static final Map<UUID, UUID> lastMessagers = new HashMap<>();
    private static final Component YOU_COMPONENT = Component.text("You").color(TextColor.color(0xe8c9f5));

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player bukkitPlayer)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        SunoPlayer player = SunoMC.getPlayer(bukkitPlayer);

        return switch (command.getName().toLowerCase()) {
            case "msg" -> handleMessageCommand(player, args);
            case "r" -> handleReplyCommand(player, args);
            default -> false;
        };
    }

    private boolean handleMessageCommand(SunoPlayer player, String @NotNull [] args) {
        if (args.length < 2) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("Suno"),
                    Component.text(TranslationHandler
                            .getTranslationFor("command.error.usage")
                            .format("command", "/msg <player> <message>")), // Please use /msg <player> <message>
                    false);
            return false;
        }

        Player targetBukkit = Bukkit.getPlayer(args[0]);
        if (targetBukkit == null) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("Suno"),
                    Component.text(TranslationHandler
                            .getTranslationFor("not-found.player")
                            .toRawString()), // Player not found or offline!
                    false);
            return false;
        }

        SunoPlayer target = SunoMC.getPlayer(targetBukkit);
        if (target.equals(player)) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("Suno"),
                    Component.text(TranslationHandler
                            .getTranslationFor("command.msg.voices")
                            .toRawString()), // You can't message yourself!
                    false);
            return false;
        }

        String messageContent = String.join(" ", args).substring(args[0].length() + 1);
        sendPrivateMessage(player, target, messageContent);
        return true;
    }

    private boolean handleReplyCommand(SunoPlayer player, String @NotNull [] args) {
        if (args.length < 1) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("SunoMC"),
                    Component.text("Please do /r <message>"),
                    false);
            return false;
        }

        UUID lastMessagerId = lastMessagers.get(player.getUniqueId());
        if (lastMessagerId == null) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("SunoMC"),
                    Component.text("You have no one to reply to."),
                    false);
            return false;
        }

        Player targetBukkit = Bukkit.getPlayer(lastMessagerId);
        if (targetBukkit == null) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("SunoMC"),
                    Component.text("That listener is no longer online.").color(TextColor.color(0xAAAAAA)),
                    false);
            return false;
        }

        SunoPlayer target = SunoMC.getPlayer(targetBukkit);
        String messageContent = String.join(" ", args);
        sendPrivateMessage(player, target, messageContent);
        return true;
    }

    private void sendPrivateMessage(@NotNull SunoPlayer sender, SunoPlayer recipient, String message) {
        Component messageComponent = Component.text(message);
        Component senderName = Component.text(sender.getName());
        Component recipientName = Component.text(recipient.getName());

        Component outgoingToSender = ChatBuilder.buildPrivateMessage(YOU_COMPONENT, recipientName, messageComponent, true);
        Component outgoingToRecipient = ChatBuilder.buildPrivateMessage(senderName, YOU_COMPONENT, messageComponent, false);

        sender.sendMessage(outgoingToSender);
        recipient.sendMessage(outgoingToRecipient);

        updateLastMessagers(sender, recipient);
    }

    private void updateLastMessagers(@NotNull SunoPlayer sender, @NotNull SunoPlayer recipient) {
        lastMessagers.put(sender.getUniqueId(), recipient.getUniqueId());
        lastMessagers.put(recipient.getUniqueId(), sender.getUniqueId());
    }
}