package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealService {
    void add(LocalDateTime dateTime, String description, int calories);

    void update(int id, LocalDateTime dateTime, String description, int calories);

    List<Meal> getAll();

    Meal getById(int id);

    void deleteById(int id);
}
