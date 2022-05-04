package net.kunmc.lab.mobtowerdefence.game;

import net.kunmc.lab.mobtowerdefence.Config;
import net.kunmc.lab.mobtowerdefence.game.feedback.*;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Game implements Listener {
    private final Plugin plugin;
    private final Config config;
    private final List<BukkitTask> taskList = new ArrayList<>();
    private boolean isRunning = false;
    private TargetBlock targetBlock;

    public Game(Plugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
    }

    public Feedback start() {
        if (config.targetBlockLocation.value() == null) {
            return new TargetBlockIsNull();
        }

        if (isRunning) {
            return new AlreadyRunning();
        }
        isRunning = true;

        targetBlock = new TargetBlock(plugin, config.targetBlockLocation.value(), new Durability(config.durability.value()));
        taskList.add(SettingMobTargetTask.runNewTask(plugin, targetBlock));

        Bukkit.getPluginManager().registerEvents(this, plugin);

        targetBlock.world().setGameRule(GameRule.MOB_GRIEFING, false);

        return new SucceededInStarting();
    }

    public Feedback stop() {
        if (!isRunning) {
            return new NotRunning();
        }

        taskList.forEach(BukkitTask::cancel);
        taskList.clear();
        targetBlock.remove();

        HandlerList.unregisterAll(this);

        isRunning = false;
        return new SucceededInStopping();
    }

    @EventHandler
    private void onPluginDisable(PluginDisableEvent e) {
        if (targetBlock != null) {
            targetBlock.remove();
        }
    }
}
