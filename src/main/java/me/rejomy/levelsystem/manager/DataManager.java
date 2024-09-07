package me.rejomy.levelsystem.manager;

import me.rejomy.levelsystem.data.PlayerData;
import me.rejomy.levelsystem.database.DataBase;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataManager {
    private final List<PlayerData> DATA = new ArrayList<>();

    private final DataBase dataBase;

    public DataManager(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public PlayerData get(Player player) {
        for (PlayerData data : DATA)
            if (data.getPlayer().equals(player))
                return data;

        return null;
    }

    public int getLevel(Player player) {
        PlayerData data = get(player);
        return data == null? -1 : data.getLevel();
    }

    public int getExperience(Player player) {
        PlayerData data = get(player);
        return data == null? -1 : data.getExperience();
    }

    public void add(Player player) {
        int level = 0;
        int experience = 0;

        try {
            ResultSet resultSet = dataBase.get(player.getName());

            if (resultSet != null) {
                level = resultSet.getInt("level");
                experience = resultSet.getInt("xp");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        DATA.add(new PlayerData(player, level, experience));
    }

    public void remove(Player player) {
        Iterator<PlayerData> iterator = DATA.iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getPlayer().equals(player)) {
                iterator.remove();
                break;
            }
        }
    }

    public void clear() {
        DATA.clear();
    }
}
