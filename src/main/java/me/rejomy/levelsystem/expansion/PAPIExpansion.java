package me.rejomy.levelsystem.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.rejomy.levelsystem.manager.DataManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPIExpansion extends PlaceholderExpansion {
    private final DataManager dataManager;

    @Override
    public @NotNull String getIdentifier() {
        return "lvlsystem";
    }
    @Override
    public @NotNull String getAuthor() {
        return "rejomy";
    }
    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    public PAPIExpansion(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        switch (params.toLowerCase()) {
            case "level" -> {
                return String.valueOf(dataManager.getLevel(player));
            }
            case "xp" -> {
                return String.valueOf(dataManager.getExperience(player));
            }
        }

        return params;
    }
}
