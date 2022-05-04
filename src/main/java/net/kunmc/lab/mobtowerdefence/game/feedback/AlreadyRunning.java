package net.kunmc.lab.mobtowerdefence.game.feedback;

import net.kunmc.lab.mobtowerdefence.game.Feedback;
import org.bukkit.ChatColor;

public class AlreadyRunning implements Feedback {
    @Override
    public String message() {
        return ChatColor.RED + "すでに実行中です.";
    }
}
