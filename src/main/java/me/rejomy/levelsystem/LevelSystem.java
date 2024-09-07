package me.rejomy.levelsystem;

import me.rejomy.levelsystem.command.LevelCommand;
import me.rejomy.levelsystem.config.Config;
import me.rejomy.levelsystem.database.DataBase;
import me.rejomy.levelsystem.database.impl.MySQL;
import me.rejomy.levelsystem.database.impl.SQLite;
import me.rejomy.levelsystem.expansion.PAPIExpansion;
import me.rejomy.levelsystem.listener.ConnectionListener;
import me.rejomy.levelsystem.manager.DataManager;
import me.rejomy.levelsystem.util.database.UserCleanerUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LevelSystem extends JavaPlugin {

    /**
     * Uses for purge outdated records
     */
    private UserCleanerUtil userCleanerUtil;

    private DataManager dataManager;
    private DataBase dataBase;

    @Override
    public void onLoad() {
        saveDefaultConfig();

        Config.INSTANCE = new Config();
        Config.INSTANCE.load(getConfig());
    }

    @Override
    public void onDisable() {
        dataManager.clear();
        Config.INSTANCE = null;
        userCleanerUtil.stopTask();
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            setDataBase();

            dataManager = new DataManager(dataBase);

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
                new PAPIExpansion(dataManager).register();
            }

            getCommand("levelsystem").setExecutor(new LevelCommand(dataBase, dataManager));
            Bukkit.getPluginManager().registerEvents(new ConnectionListener(dataManager), this);

            if (userCleanerUtil != null) {
                userCleanerUtil.runTask(this, dataBase);
            } else
                userCleanerUtil = new UserCleanerUtil(this, dataBase);
        });
    }

    void setDataBase() {
        dataBase = null;

        try {
            switch (getConfig().getString("database").toLowerCase()) {
                case "sqlite" -> {
                    dataBase = new SQLite();
                }
                case "mysql" -> {
                    dataBase = new MySQL(getConfig());
                }
            }

            if (dataBase == null) {
                getLogger().severe("");
                getLogger().severe("Error: Incorrect database type " + getConfig().getString("database"));
                getLogger().severe("Choose mysql or sqlite database");
                getLogger().severe("");

                getLogger().severe("Disabling plugin...");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } catch (Exception exception) {
            getLogger().severe("");
            getLogger().severe("Error when working with the database :(");
            getLogger().severe(exception.getLocalizedMessage());
            getLogger().severe("");
            getLogger().severe("Disabling plugin...");

            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
}
