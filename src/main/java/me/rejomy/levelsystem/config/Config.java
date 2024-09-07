package me.rejomy.levelsystem.config;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static Config INSTANCE;

    public boolean XP_RESET;

    public int XP_STEP;

    public void load(FileConfiguration config) {
        XP_RESET = config.getBoolean("xp-system.reset");

        XP_STEP = config.getInt("xp-system.step");
    }
}
