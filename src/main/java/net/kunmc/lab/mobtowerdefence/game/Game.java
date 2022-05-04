package net.kunmc.lab.mobtowerdefence.game;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import net.kunmc.lab.mobtowerdefence.Config;
import net.kunmc.lab.mobtowerdefence.game.feedback.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
    private DurabilityBossBar bossBar;

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

        targetBlock = TargetBlock.create(plugin, config.targetBlockType.value(), config.targetBlockLocation.value(), new Durability(config.durability.value()));
        taskList.add(SettingMobTargetTask.runNewTask(plugin, targetBlock));

        bossBar = DurabilityBossBar.create(targetBlock.durability());
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);

        targetBlock.world().setGameRule(GameRule.MOB_GRIEFING, false);
        Bukkit.getPluginManager().registerEvents(this, plugin);

        return new SucceededInStarting();
    }

    public Feedback stop() {
        if (!isRunning) {
            return new NotRunning();
        }

        targetBlock.remove();
        taskList.forEach(BukkitTask::cancel);
        taskList.clear();
        bossBar.remove();
        HandlerList.unregisterAll(this);

        isRunning = false;
        return new SucceededInStopping();
    }

    @EventHandler
    private void onTick(ServerTickEndEvent e) {
        targetBlock.world().getEntitiesByClass(Mob.class).forEach(targetBlock::handleAsTarget);

        if (targetBlock.durability().isZero()) {
            Component title = Component.translatable(targetBlock.materialName()).append(Component.text("が破壊された!"));
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.showTitle(Title.title(title, Component.text().build()));
            });

            stop();
        }
    }

    @EventHandler
    private void onTargetBlockAttacked(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            return;
        }

        if (targetBlock.isTargetBlock(e.getEntity())) {
            targetBlock.damage(e.getDamage());
        }
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {
        bossBar.addPlayer(e.getPlayer());
    }

    @EventHandler
    private void onPluginDisable(PluginDisableEvent e) {
        if (targetBlock != null) {
            targetBlock.remove();
        }

        if (bossBar != null) {
            bossBar.remove();
        }
    }
}
