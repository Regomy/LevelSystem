package me.rejomy.levelsystem.util.transfer.impl;

import me.rejomy.levelsystem.api.impl.LevelChangeEvent;
import me.rejomy.levelsystem.config.Config;
import me.rejomy.levelsystem.data.PlayerData;
import me.rejomy.levelsystem.database.DataBase;
import me.rejomy.levelsystem.util.transfer.Transfer;

public class LevelTransfer extends Transfer {

    public LevelTransfer(DataBase dataBase, PlayerData data, int value, int newValue) {
        super(dataBase, data, value, newValue);
    }

    @Override
    public void handle() {
        LevelChangeEvent event = new LevelChangeEvent(value, newValue);

        if (transfer(event)) {
            // Reset player experience when level is changing.
            if (Config.INSTANCE.XP_RESET) {
                ExperienceTransfer experienceTransfer = new ExperienceTransfer(dataBase, data,
                        data.getExperience(), 0);

                experienceTransfer.shouldWriteToDb = false;
                experienceTransfer.handle();
            }

            data.setLevel(newValue);

            if (shouldWriteToDb)
                dataBase.add(data.getPlayer().getName(), data.getLevel(), data.getExperience());
        }
    }

    @Override
    protected boolean validate() {
        if (newValue < 1) {
            throw new NumberFormatException("Level value cannot small than one");
        }

        return true;
    }
}
