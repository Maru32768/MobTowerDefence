package net.kunmc.lab.mobtowerdefence.game;

import org.bukkit.command.CommandSender;

public interface Feedback {
    String message();

    default void sendFeedBack(CommandSender sender) {
        sender.sendMessage(message());
    }
}
