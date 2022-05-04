package net.kunmc.lab.mobtowerdefence.game.feedback;

import net.kunmc.lab.mobtowerdefence.game.Feedback;
import org.bukkit.ChatColor;

public class SucceededInStopping implements Feedback {
    @Override
    public String message() {
        return ChatColor.GREEN + "終了しました.";
    }
}
