package me.rejomy.levelsystem.api.impl;

import lombok.Getter;
import lombok.Setter;
import me.rejomy.levelsystem.api.ValueChangeEvent;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class XpChangeEvent extends ValueChangeEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public XpChangeEvent(int currentValue, int newValue) {
        super(currentValue, newValue);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
