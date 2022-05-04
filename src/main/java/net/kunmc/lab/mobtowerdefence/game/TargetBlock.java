package net.kunmc.lab.mobtowerdefence.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

class TargetBlock implements Listener {
    private final Material material;
    private final Location targetBlockLocation;
    private final ArmorStand armorStand;
    private final Durability durability;

    static TargetBlock create(Plugin plugin, Material material, Location targetBlockLocation, Durability durability) {
        return new TargetBlock(plugin, material, targetBlockLocation, durability);
    }

    private TargetBlock(Plugin plugin, Material material, Location targetBlockLocation, Durability durability) {
        this.material = material;
        this.targetBlockLocation = targetBlockLocation.clone();
        this.armorStand = summonTargetArmorStand();
        this.durability = durability;

        targetBlockLocation.getBlock().setType(material);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private ArmorStand summonTargetArmorStand() {
        Location loc = targetBlockLocation.clone().toCenterLocation();
        loc.add(0, 0.5, 0);

        return world().spawn(loc, ArmorStand.class, CreatureSpawnEvent.SpawnReason.CUSTOM, x -> {
            x.setVisible(true);
            x.setInvulnerable(false);
            x.setMarker(false);
            x.setGravity(false);
            x.setCollidable(false);
        });
    }

    public String materialName() {
        return material.getTranslationKey();
    }

    public Durability durability() {
        return durability;
    }

    public void damage(double d) {
        durability.reduce(d);
    }

    public void handleAsTarget(Mob source) {
        source.setTarget(armorStand);
    }

    public World world() {
        return targetBlockLocation.getWorld();
    }

    public void remove() {
        targetBlockLocation.getBlock().setType(Material.AIR);
        armorStand.remove();
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onArmorStandKilled(EntityDeathEvent e) {
        if (e.getEntity() == armorStand) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onTargetBlockBreak(BlockBreakEvent e) {
        if (isTargetBlock(e.getBlock())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onArmorStandDamaged(EntityDamageByEntityEvent e) {
        if (e.getEntity() == armorStand) {
            e.setCancelled(true);
        }
    }

    public boolean isTargetBlock(Block b) {
        return b.getLocation().equals(targetBlockLocation);
    }

    public boolean isTargetBlock(Entity e) {
        return e == armorStand;
    }
}
