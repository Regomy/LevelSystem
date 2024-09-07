package me.rejomy.levelsystem.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class ValueChangeEvent extends Event implements Cancellable {

    private boolean cancelled = false;

    /**
     * Current player level value.
     */
    public final int currentValue;

    /**
     * Value that should set to current.
     */
    @NotNull
    private int newValue;

    /**
     * Here you can change newValue to your favorite value.
     * Be careful! If you change value, you should change xp level too, else plugin will try
     *   upgrade player level every time, because xp level was larger than need for level up.
     * @param value should be larger than zero.
     */
    public void setNewValue(int value) {
        if (value < 0) {
            throw new NumberFormatException("New value should be larger than zero.");
        }

        this.newValue = value;
    }
}
