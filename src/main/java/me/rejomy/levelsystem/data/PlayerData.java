package me.rejomy.levelsystem.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Here is container for player data.
 */
@Getter
@Setter
@AllArgsConstructor
public class PlayerData {

    @NotNull
    Player player;

    /**
     * Current player level.
     * Level is directly related to experience.
     * (!) This value should be equals or large than zero.
     */
    private int level;

    /**
     * Current player experience value.
     * (!) This value should be equals or large than zero.
     */
    private int experience;

    public void setExperience(int value) {
        if (value < 0) {
            throw new NumberFormatException("Value should be larger than zero.");
        }

        this.experience = value;
    }

    public void setLevel(int value) {
        if (value < 0) {
            throw new NumberFormatException("Value should be larger than zero.");
        }

        this.level = value;
    }
}
