package me.rejomy.levelsystem.database.impl;

import me.rejomy.levelsystem.database.DataBase;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL extends DataBase {
    FileConfiguration config;

    public MySQL(FileConfiguration config) throws Exception {
        this.config = config;

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        createTable();
    }

    @Override
    public Connection getConnection() throws SQLException {
        String host = config.getString("database.mysql.host");
        String port = config.getString("database.mysql.port");
        String basename = config.getString("database.mysql.basename");
        String username = config.getString("database.mysql.username");
        String password = config.getString("database.mysql.password");

        return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + basename +
                "?user=" + username + "&password=" + password);
    }

    @Override
    public void add(String name, int level, int xp) {
        try {
            executeUpdate("INSERT INTO users VALUES ('" + name + "','" + level + "','" + xp + "', '" + System.currentTimeMillis() + "') " +
                    "ON DUPLICATE KEY UPDATE level = VALUES(level), xp = VALUES(xp)");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void delete(String name) {
        try {
            executeUpdate("DELETE FROM users WHERE name='" + name + "'");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String get(String name, String column) {
        try {
            ResultSet resultSet = executeQuery("SELECT " + column + " FROM users WHERE name=('" + name + "')");

            if (resultSet.next()) {
                return resultSet.getString(column);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public ResultSet get(String name) {
        try {
            ResultSet resultSet = executeQuery("SELECT * FROM users WHERE name=('" + name + "')");

            // Check if player data contains in db.
            if (resultSet.next()) {
                return resultSet;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public ResultSet getAll() {
        try {
            return executeQuery("SELECT * from users");
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

}
