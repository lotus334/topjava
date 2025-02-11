package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepository {

    private static final ConcurrentHashMap<Integer, Meal> ID_TO_MEAL = new ConcurrentHashMap<>();
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    public static void addMeal(Meal meal) {
        innerAddMeal(meal);
    }

    public static void addMeals(Collection<Meal> meals) {
        for (Meal meal : meals) {
            innerAddMeal(meal);
        }
    }

    private static void innerAddMeal(Meal meal) {
        int generatedId = ID_GENERATOR.getAndIncrement();
        meal.setId(generatedId);
        ID_TO_MEAL.put(generatedId, meal);
    }

    public static void updateMeal(Meal meal) {
        ID_TO_MEAL.put(meal.getId(), meal);
    }

    public static List<Meal> getMeals() {
        return new ArrayList<>(ID_TO_MEAL.values());
    }

    public static Meal getMealById(int id) {
        return ID_TO_MEAL.get(id);
    }

    public static void deleteMealById(Integer id) {
        ID_TO_MEAL.remove(id);
    }
}
