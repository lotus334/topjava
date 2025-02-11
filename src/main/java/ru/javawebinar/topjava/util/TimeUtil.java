package ru.javawebinar.topjava.util;

import java.time.LocalTime;
import java.util.Objects;

public class TimeUtil {
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return (Objects.isNull(startTime) || lt.compareTo(startTime) >= 0) && (Objects.isNull(startTime) || lt.compareTo(endTime) < 0);
    }
}
