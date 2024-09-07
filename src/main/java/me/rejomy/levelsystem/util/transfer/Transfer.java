package me.rejomy.levelsystem.util.transfer;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.rejomy.levelsystem.api.ValueChangeEvent;
import me.rejomy.levelsystem.data.PlayerData;
import me.rejomy.levelsystem.database.DataBase;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class Transfer {

    @NotNull
    protected final DataBase dataBase;
    @NotNull
    protected final PlayerData data;

    /**
     * Uses for stop sending two sql requests per handle.
     */
    public boolean shouldWriteToDb = true;

    /**
     * Current player unit value.
     */
    @NotNull
    protected final int value;

    /**
     * New player unit value, calculated from player request
     *  and updated after run events in api.
     */
    @NotNull
    protected int newValue;

    /**
     * Should be used in Command for handle command request.
     */
    public abstract void handle();

    /**
     * Check value and calls API methods
     */
    public boolean transfer(ValueChangeEvent event) {
        if (!validate()) return false;

        Bukkit.getPluginManager().callEvent(event);

        newValue = event.getNewValue();

        return newValue != value && !event.isCancelled() && validate();
    }

    /**
     * Prevent stupid situations.
     */
    protected boolean validate() {
        if (newValue < 0) {
            throw new NumberFormatException("Result value " + newValue + " cannot be negative.");
        }

        return true;
    }
}
