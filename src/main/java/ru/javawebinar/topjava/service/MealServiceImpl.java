package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MealServiceImpl implements MealService {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    @Override
    public void addMeal(LocalDateTime dateTime, String description, int calories) {
        int generatedId = ID_GENERATOR.getAndIncrement();
        MealRepository.addMeal(
                new Meal(generatedId, dateTime, description, calories)
        );
    }

    @Override
    public void updateMeal(int id, LocalDateTime dateTime, String description, int calories) {
        MealRepository.updateMeal(
                new Meal(id, dateTime, description, calories)
        );
    }

    @Override
    public List<Meal> getMeals() {
        return MealRepository.getMeals();
    }

    @Override
    public Meal getMealById(int id) {
        return MealRepository.getMealById(id);
    }

    @Override
    public void deleteMealById(Integer id) {
        MealRepository.deleteMealById(id);
    }
}
