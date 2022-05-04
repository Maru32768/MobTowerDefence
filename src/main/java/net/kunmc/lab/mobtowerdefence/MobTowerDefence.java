package net.kunmc.lab.mobtowerdefence;

import net.kunmc.lab.commandlib.Command;
import net.kunmc.lab.commandlib.CommandLib;
import net.kunmc.lab.configlib.ConfigCommand;
import net.kunmc.lab.configlib.ConfigCommandBuilder;
import net.kunmc.lab.mobtowerdefence.command.SetTargetBlockCommand;
import net.kunmc.lab.mobtowerdefence.command.StartCommand;
import net.kunmc.lab.mobtowerdefence.command.StopCommand;
import net.kunmc.lab.mobtowerdefence.game.Game;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobTowerDefence extends JavaPlugin {
    public static MobTowerDefence instance;
    public Config config;
    public Game game;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config(this);

        ConfigCommand configCommand = new ConfigCommandBuilder(config).build();
        CommandLib.register(this, new Command("mtd") {{
            addChildren(new StartCommand(), new StopCommand(), new SetTargetBlockCommand(), configCommand);
        }});

        game = new Game(this, config);
    }
}
