package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealService {
    void addMeal(LocalDateTime dateTime, String description, int calories);

    void updateMeal(int id, LocalDateTime dateTime, String description, int calories);

    List<Meal> getMeals();

    Meal getMealById(int id);

    void deleteMealById(Integer id);
}
