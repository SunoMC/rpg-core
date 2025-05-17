package net.sunomc.rpg.core.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.sunomc.rpg.SunoMC;
import net.sunomc.rpg.core.builder.ChatBuilder;
import net.sunomc.rpg.core.common.ChatIcon;
import net.sunomc.rpg.core.common.SunoPlayer;
import net.sunomc.rpg.core.handler.TranslationHandler;
import net.sunomc.rpg.game.common.Character;
import net.sunomc.rpg.game.dialog.Dialog;
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
            sender.sendMessage("Only players can use this command."); // TODO : übersetzbar machen
            return true;
        }

        SunoPlayer player = SunoMC.getPlayer(bukkitPlayer);

        Dialog dialog = Dialog.from(
                new net.sunomc.rpg.game.common.Character(Component.text("hallo"), "", 0),
                Character.Mood.BORED).text(Component.text("Test text das man sehen kann ob das jetz geht free hahhahh"));

        player.sendDialog(dialog);

        player.sendRawMessage(dialog.getText());

        return switch (command.getName().toLowerCase()) {
            case "msg" -> handleMessageCommand(player, args);
            case "r" -> handleReplyCommand(player, args);
            default -> false;
        };
    }

    private boolean handleMessageCommand(SunoPlayer player, @NotNull String @NotNull [] args) {
        if (args.length < 2) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("Suno"),
                    TranslationHandler
                            .getTranslationFor("commands.general", "error.usage")
                            .formatToError("command", "/msg <player> <message>"),
                    false);
            return false;
        }

        Player targetBukkit = Bukkit.getPlayer(args[0]);
        if (targetBukkit == null) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("Suno"),
                    TranslationHandler
                            .getTranslationFor("not-found", "player")
                            .toRawComponent(),
                    false);
            return false;
        }

        SunoPlayer target = SunoMC.getPlayer(targetBukkit);
        if (target.equals(player)) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("Suno"),
                    TranslationHandler
                            .getTranslationFor("commands.msg", "error.self.message")
                            .toRawComponent(),
                    false);
            return false;
        }

        String messageContent = String.join(" ", args).substring(args[0].length() + 1);
        sendPrivateMessage(player, target, messageContent);
        return true;
    }

    private boolean handleReplyCommand(SunoPlayer player, @NotNull String @NotNull [] args) {
        if (args.length < 1) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("SunoMC"),
                    TranslationHandler
                            .getTranslationFor("commands.general", "error.usage")
                            .formatToError("command", "/r <message>"),
                    false);
            return false;
        }

        UUID lastMessagerId = lastMessagers.get(player.getUniqueId());
        if (lastMessagerId == null) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("SunoMC"),
                    TranslationHandler
                            .getTranslationFor("commands.msg", "error.reply.none")
                            .toRawComponent(),
                    false);
            return false;
        }

        Player targetBukkit = Bukkit.getPlayer(lastMessagerId);
        if (targetBukkit == null) {
            player.sendMessage(ChatIcon.Preset.ERROR,
                    Component.text("SunoMC"),
                    TranslationHandler
                            .getTranslationFor("commands.msg", "error.reply.offline")
                            .toRawComponent(),
                    false);
            return false;
        }

        SunoPlayer target = SunoMC.getPlayer(targetBukkit);
        String messageContent = String.join(" ", args);
        sendPrivateMessage(player, target, messageContent);
        return true;
    }

    private void sendPrivateMessage(@NotNull SunoPlayer sender, @NotNull SunoPlayer recipient, String message) {
        Component messageComponent = Component.text(message);
        Component senderName = Component.text(sender.getName());
        Component recipientName = Component.text(recipient.getName());

        Component outgoingToSender = ChatBuilder.buildPrivateMessage(YOU_COMPONENT, recipientName, messageComponent, true);
        Component outgoingToRecipient = ChatBuilder.buildPrivateMessage(senderName, YOU_COMPONENT, messageComponent, false);

        sender.sendRawMessage(outgoingToSender);
        recipient.sendRawMessage(outgoingToRecipient);

        updateLastChats(sender, recipient);
    }

    private void updateLastChats(@NotNull SunoPlayer sender, @NotNull SunoPlayer recipient) {
        lastMessagers.put(sender.getUniqueId(), recipient.getUniqueId());
        lastMessagers.put(recipient.getUniqueId(), sender.getUniqueId());
    }
}
