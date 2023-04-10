package net.kunmc.lab.mobtowerdefence.command;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.mobtowerdefence.MobTowerDefence;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop");

        execute(ctx -> {
            MobTowerDefence.instance.game.stop().sendFeedBack(ctx.getSender());
        });
    }
}
