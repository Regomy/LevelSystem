package me.rejomy.levelsystem.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DataBase {
    public abstract Connection getConnection() throws SQLException;

    public abstract void add(String name, int level, int xp);

    public abstract void delete(String name);

    public abstract String get(String name, String column);

    public abstract ResultSet get(String name);

    protected ResultSet executeQuery(String query) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.executeQuery(query);
    }

    protected void executeUpdate(String query) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    protected void createTable() throws SQLException {
        executeUpdate("CREATE TABLE IF NOT EXISTS users (name TEXT PRIMARY KEY, level INT, xp INT, time LONG)");
    }
}
