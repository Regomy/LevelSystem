package me.rejomy.levelsystem.command;

import me.rejomy.levelsystem.data.PlayerData;
import me.rejomy.levelsystem.database.DataBase;
import me.rejomy.levelsystem.manager.DataManager;
import me.rejomy.levelsystem.util.NumberUtil;
import me.rejomy.levelsystem.util.transfer.impl.ExperienceTransfer;
import me.rejomy.levelsystem.util.transfer.impl.LevelTransfer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class LevelCommand implements CommandExecutor {
    private final DataBase dataBase;
    private final DataManager dataManager;

    public LevelCommand(DataBase dataBase, DataManager dataManager) {
        this.dataBase = dataBase;
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length <= 1) {
            sendHelp(sender);
            return false;
        }

        if (args[0].equalsIgnoreCase("info")) {
            if (args.length != 2) {
                sendHelp(sender);
                return true;
            }

            String playerName = args[1];
            PlayerData data = checkPlayer(playerName);

            sender.sendMessage("[LevelSystem]");
            sender.sendMessage("[LevelSystem] Player " + playerName + " has:");
            sender.sendMessage(String.format("[LevelSystem] level: %s and experience: %s",
                    data.getLevel(), data.getExperience()));
            sender.sendMessage("[LevelSystem]");
            return true;
        }

        boolean isLevelRequest = args[0].equalsIgnoreCase("level");
        boolean isXpRequest = args[0].equalsIgnoreCase("xp");

        // If request is invalid, we should send help message to the player.
        if (!isLevelRequest && !isXpRequest) {
            sendHelp(sender);
            return false;
        }

        String playerName = args[2];
        PlayerData data = checkPlayer(playerName);

        int value = NumberUtil.parseInt(args[3]);

        // Check if value is not number, throw exception.
        if (value == -1) {
            throw new IllegalArgumentException("Value should be large than zero, your value " + args[3]);
        }

        // Handle current at this moment value for send feedback to the sender.
        int previousValue = isLevelRequest? data.getLevel() : data.getExperience();

        int newValue = 0;

        switch (args[1].toLowerCase(Locale.ENGLISH)) {
            case "take" -> {
                if (isLevelRequest) {
                    newValue = data.getLevel() - value;
                } else if (isXpRequest) {
                    newValue = data.getExperience() - value;
                }
            }

            case "add" -> {
                if (isLevelRequest) {
                    newValue = data.getLevel() + value;
                } else if (isXpRequest) {
                    newValue = data.getExperience() + value;
                }
            }

            case "set" -> newValue = value;

            default -> {
                sendHelp(sender);
                return false;
            }
        }

        run(data, isLevelRequest, newValue);
        sender.sendMessage(String.format("We are success changing %s %s from %s to %s",
                playerName, isLevelRequest? "level" : "xp", previousValue, newValue));

        return false;
    }

    PlayerData checkPlayer(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        PlayerData data;

        // Try load player data from database
        if (player == null) {
            data = dataManager.create(null, playerName);
        } else data = dataManager.get(player);

        if (data == null) {
            throw new NullPointerException("Player with name " + playerName + " dont has a data in memory.");
        }

        return data;
    }

    void run(PlayerData data, boolean level, int value) {
        if (level) {
            new LevelTransfer(dataBase, data, data.getLevel(), value).handle();
        } else {
            new ExperienceTransfer(dataBase, data, data.getExperience(), value).handle();
        }
    }

    void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("Syntax: /level xp add|take|set player xp");
        sender.sendMessage("Syntax: /level level add|take|set player level");
        sender.sendMessage("Syntax: /level info player");
        sender.sendMessage("");
        sender.sendMessage("For example: /level level take player 2");
        sender.sendMessage("");
    }
}
