package net.kunmc.lab.mobtowerdefence.game;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

class DurabilityBossBar implements DurabilityObserver {
    private final double initialDurability;
    private final BossBar bossBar;

    static DurabilityBossBar create(Durability durability) {
        return new DurabilityBossBar(durability);
    }

    private DurabilityBossBar(Durability durability) {
        this.initialDurability = durability.remaining();
        this.bossBar = Bukkit.createBossBar("残り耐久値", BarColor.RED, BarStyle.SOLID);

        bossBar.setVisible(true);
        durability.addObserver(this);
    }

    void addPlayer(Player p) {
        bossBar.addPlayer(p);
    }

    void remove() {
        bossBar.setVisible(false);
        bossBar.removeAll();
    }

    @Override
    public void update(Durability durability) {
        bossBar.setProgress(durability.remaining() / initialDurability);
    }
}
