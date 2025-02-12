package ru.javawebinar.topjava.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class Meal {

    @Setter
    private int id;

    @NonNull
    private final LocalDateTime dateTime;

    private final String description;

    @NonNull
    private final int calories;

    public Meal(int id, @NonNull LocalDateTime dateTime, @NonNull String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
