package net.kunmc.lab.mobtowerdefence.command;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandContext;
import net.kunmc.lab.mobtowerdefence.Config;
import net.kunmc.lab.mobtowerdefence.MobTowerDefence;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SetTargetBlockCommand extends Command {
    public SetTargetBlockCommand() {
        super("setTargetBlock");
    }

    @Override
    protected void execute(CommandContext ctx) {
        if (!(ctx.getSender() instanceof Player)) {
            ctx.sendFailure("プレイヤーから実行してください.");
            return;
        }
        Player p = ((Player) ctx.getSender());

        Block b = p.getTargetBlock(10);
        if (b == null) {
            ctx.sendFailure("ブロックをターゲットしながら実行してください.");
            return;
        }
        Location loc = b.getLocation();

        Config config = MobTowerDefence.instance.config;
        config.targetBlockLocation.value(loc);
        config.saveConfigIfPresent();

        ctx.sendSuccess(String.format("{type:%s x:%d y:%d z:%d}に設定しました.",
                b.getType(),
                loc.getBlockX(),
                loc.getBlockY(),
                loc.getBlockZ()));

    }
}
