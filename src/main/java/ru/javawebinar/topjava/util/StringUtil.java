package ru.javawebinar.topjava.util;

import java.util.Objects;

public class StringUtil {
    public static <V> String getEmptyIfNull(V str) {
        if (Objects.isNull(str)) {
            return "";
        }
        return String.valueOf(str);
    }
}
