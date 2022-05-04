package net.kunmc.lab.mobtowerdefence.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

class TargetBlock implements Listener {
    private final Plugin plugin;
    private final Location targetBlockLocation;
    private final List<ArmorStand> armorStands = new ArrayList<>();
    private Durability durability;

    TargetBlock(Plugin plugin, Location targetBlockLocation, Durability durability) {
        this.plugin = plugin;
        this.targetBlockLocation = targetBlockLocation.clone();
        summonTargetArmorStands();
        this.durability = durability;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void summonTargetArmorStands() {
        double[] dx = {-0.5, 0.5, -0.5, 0.5};
        double[] dy = {-0.5, -0.5, 0.5, 0.5};
        for (int i = 0; i < 4; i++) {
            Location loc = targetBlockLocation.clone().toCenterLocation();
            loc.add(dx[i], 0, dy[i]);
            armorStands.add(world().spawn(loc, ArmorStand.class, CreatureSpawnEvent.SpawnReason.CUSTOM, x -> {
                x.setVisible(false);
                x.setInvulnerable(true);
                x.setMarker(true);
                x.setGravity(false);
                x.setCollidable(false);
            }));
        }
    }

    public void handleAsTarget(Mob source) {
        ArmorStand nearest = armorStands.stream()
                .min((x, y) -> distanceComparator(source, x, y))
                .get();
        source.setTarget(nearest);
    }

    private int distanceComparator(Mob mob, Entity e1, Entity e2) {
        double d1 = e1.getLocation().distance(mob.getLocation());
        double d2 = e2.getLocation().distance(mob.getLocation());
        return Double.compare(d1, d2);
    }

    public World world() {
        return targetBlockLocation.getWorld();
    }

    public void remove() {
        armorStands.forEach(Entity::remove);
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if (isTargetBlock(e.getBlock())) {
            e.setCancelled(true);
        }
    }

    private boolean isTargetBlock(Block b) {
        return b.getLocation().equals(targetBlockLocation);
    }
}
