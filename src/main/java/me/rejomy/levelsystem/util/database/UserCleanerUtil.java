package me.rejomy.levelsystem.util.database;

import me.rejomy.levelsystem.config.Config;
import me.rejomy.levelsystem.database.DataBase;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCleanerUtil {
    private int taskID = -1;

    public UserCleanerUtil(JavaPlugin plugin, DataBase dataBase) {
        runTask(plugin, dataBase);
    }

    public void runTask(JavaPlugin plugin, DataBase dataBase) {
        taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, () -> {
            ResultSet resultSet = dataBase.getAll();

            // When result set is null so theres been an error somewhere.
            if (resultSet == null) {
                return;
            }

            try {
                while (resultSet.next()) {
                    long lastWriteTimeStamp = resultSet.getLong("time");

                    if (System.currentTimeMillis() - lastWriteTimeStamp > Config.INSTANCE.PURGE_PERIOD) {
                        dataBase.delete(resultSet.getString("name"));
                    }
                }

                resultSet.close();
                resultSet.getStatement().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, Config.INSTANCE.PURGE_TASK_DELAY, Config.INSTANCE.PURGE_TASK_DELAY);
    }

    public void stopTask() {
        if (taskID != -1) {
            Bukkit.getScheduler().cancelTask(taskID);
            taskID = -1;
        }
    }
}
