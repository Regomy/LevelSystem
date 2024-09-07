package me.rejomy.levelsystem.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtil {

    public int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }

        return -1;
    }
}
