package me.rejomy.levelsystem.util.transfer.impl;

import me.rejomy.levelsystem.api.impl.XpChangeEvent;
import me.rejomy.levelsystem.config.Config;
import me.rejomy.levelsystem.data.PlayerData;
import me.rejomy.levelsystem.database.DataBase;
import me.rejomy.levelsystem.util.transfer.Transfer;

public class ExperienceTransfer extends Transfer {

    public ExperienceTransfer(DataBase dataBase, PlayerData data, int value, int newValue) {
        super(dataBase, data, value, newValue);
    }

    @Override
    public void handle() {
        XpChangeEvent event = new XpChangeEvent(value, newValue);

        if (transfer(event)) {
            // Calculate experience based player level.
            int nextLevelExperience = (data.getLevel() + 1) * Config.INSTANCE.XP_STEP;

            if (newValue >= nextLevelExperience) {
                int predictedLevel = newValue / 1000;

                LevelTransfer levelTransfer = new LevelTransfer(dataBase, data, data.getLevel(),
                        predictedLevel);

                levelTransfer.shouldWriteToDb = false;

                levelTransfer.handle();
            }

            data.setExperience(newValue);

            if (shouldWriteToDb)
                dataBase.add(data.getPlayer().getName(), data.getLevel(), data.getExperience());
        }
    }
}
