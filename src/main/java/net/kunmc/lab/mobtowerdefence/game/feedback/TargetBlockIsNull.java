package net.kunmc.lab.mobtowerdefence.game.feedback;

import net.kunmc.lab.mobtowerdefence.game.Feedback;
import org.bukkit.ChatColor;

public class TargetBlockIsNull implements Feedback {
    @Override
    public String message() {
        return ChatColor.RED + "targetBlockLocationが設定されていません.";
    }
}
