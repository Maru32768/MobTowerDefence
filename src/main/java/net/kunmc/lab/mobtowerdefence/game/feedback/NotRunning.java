package net.kunmc.lab.mobtowerdefence.game.feedback;

import net.kunmc.lab.mobtowerdefence.game.Feedback;
import org.bukkit.ChatColor;

public class NotRunning implements Feedback {
    @Override
    public String message() {
        return ChatColor.RED + "実行されていません.";
    }
}
