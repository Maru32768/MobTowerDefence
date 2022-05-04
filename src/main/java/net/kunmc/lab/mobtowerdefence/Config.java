package net.kunmc.lab.mobtowerdefence;

import net.kunmc.lab.configlib.BaseConfig;
import net.kunmc.lab.configlib.value.IntegerValue;
import net.kunmc.lab.configlib.value.LocationValue;
import net.kunmc.lab.configlib.value.MaterialValue;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Config extends BaseConfig {
    public final MaterialValue targetBlockType = new MaterialValue(Material.BEACON, true);
    public final LocationValue targetBlockLocation = new LocationValue();
    public final IntegerValue durability = new IntegerValue(1200, 1, Integer.MAX_VALUE);

    public Config(@NotNull Plugin plugin) {
        super(plugin);
    }

    @Override
    public void saveConfigIfPresent() {
        super.saveConfigIfPresent();
    }
}
