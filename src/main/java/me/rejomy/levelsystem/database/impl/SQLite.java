package me.rejomy.levelsystem.database.impl;

import me.rejomy.levelsystem.database.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLite extends DataBase {
    public SQLite() throws Exception {
        Class.forName("org.sqlite.JDBC").newInstance();
        createTable();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:plugins/LevelSystem/database.db");
    }

    @Override
    public void add(String name, int level, int xp) {
        try {
            executeUpdate("INSERT OR REPLACE INTO users VALUES ('" + name + "','" + level + "','" + xp + "', '" + System.currentTimeMillis() + "')");
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
            return executeQuery("SELECT * FROM users");
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
