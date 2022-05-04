package net.kunmc.lab.mobtowerdefence.game;

import org.bukkit.entity.Mob;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

class SettingMobTargetTask extends BukkitRunnable {
    private final TargetBlock targetBlock;

    public static BukkitTask runNewTask(Plugin plugin, TargetBlock targetBlock) {
        return new SettingMobTargetTask(targetBlock).runTaskTimerAsynchronously(plugin, 0, 0);
    }

    private SettingMobTargetTask(TargetBlock targetBlock) {
        this.targetBlock = targetBlock;
    }

    @Override
    public void run() {
        targetBlock.world()
                .getEntitiesByClass(Mob.class)
                .forEach(targetBlock::handleAsTarget);
    }
}
