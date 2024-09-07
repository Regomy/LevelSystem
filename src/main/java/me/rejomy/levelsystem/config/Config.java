package me.rejomy.levelsystem.config;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public static Config INSTANCE;

    public boolean XP_RESET;
    public int XP_STEP;

    public boolean PURGE_ENABLE;
    public int PURGE_TASK_DELAY;
    public int PURGE_PERIOD;

    public void load(FileConfiguration config) {
        XP_RESET = config.getBoolean("xp-system.reset");
        XP_STEP = config.getInt("xp-system.step");

        PURGE_ENABLE = config.getBoolean("purge.enable");
        // Convert minutes to seconds and after to ticks.
        PURGE_TASK_DELAY = config.getInt("purge.task-repeat-delay") * 60 * 20;
        // Convert days -> hours -> minutes -> seconds -> millis
        PURGE_PERIOD = config.getInt("purge.max-none-activity-days") * 24 * 60 * 60 * 1000;
    }
}
