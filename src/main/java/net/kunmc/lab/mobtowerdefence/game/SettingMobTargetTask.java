package net.kunmc.lab.mobtowerdefence.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SettingMobTargetTask extends BukkitRunnable implements Listener {
    private final TargetBlock targetBlock;
    private final Map<Mob, Player> mobToPlayerMap = new ConcurrentHashMap<>();
    private final double distanceOfReleasingTargetedPlayer;

    public static BukkitTask runNewTask(Plugin plugin, TargetBlock targetBlock, double distanceOfReleasingTargetedPlayer) {
        return new SettingMobTargetTask(plugin, targetBlock, distanceOfReleasingTargetedPlayer).runTaskTimerAsynchronously(plugin, 0, 0);
    }

    private SettingMobTargetTask(Plugin plugin, TargetBlock targetBlock, double distanceOfReleasingTargetedPlayer) {
        this.targetBlock = targetBlock;
        this.distanceOfReleasingTargetedPlayer = distanceOfReleasingTargetedPlayer;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            double distance = targetBlock.distance(p.getLocation());
            if (distance >= distanceOfReleasingTargetedPlayer) {
                mobToPlayerMap.keySet().forEach(k -> {
                    mobToPlayerMap.remove(k, p);
                });
            }
        });

        targetBlock.world().getEntitiesByClass(Mob.class).forEach(x -> {
            Player p = mobToPlayerMap.get(x);
            if (p == null) {
                targetBlock.handleAsTarget(x);
                return;
            }

            x.setTarget(p);
        });
    }

    @EventHandler
    private void onPlayerAttackMob(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!(e.getEntity() instanceof Mob)) {
            return;
        }
        Player p = ((Player) e.getDamager());
        Mob mob = ((Mob) e.getEntity());

        mobToPlayerMap.put(mob, p);
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent e) {
        mobToPlayerMap.entrySet().stream()
                .filter(x -> x.getValue() == e.getEntity())
                .map(Map.Entry::getKey)
                .forEach(mobToPlayerMap::remove);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {
        mobToPlayerMap.keySet().forEach(k -> {
            mobToPlayerMap.remove(k, e.getPlayer());
        });
    }
}
