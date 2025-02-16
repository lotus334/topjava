package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    Meal add(LocalDateTime dateTime, String description, int calories);

    Meal update(int id, LocalDateTime dateTime, String description, int calories);

    List<Meal> getAll();

    Meal getById(int id);

    void deleteById(int id);
}
