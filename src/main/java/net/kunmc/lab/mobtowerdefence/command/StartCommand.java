package net.kunmc.lab.mobtowerdefence.command;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.mobtowerdefence.MobTowerDefence;

public class StartCommand extends Command {
    public StartCommand() {
        super("start");
    }

    @Override
    protected void execute(CommandContext ctx) {
        MobTowerDefence.instance.game.start().sendFeedBack(ctx.getSender());
    }
}
