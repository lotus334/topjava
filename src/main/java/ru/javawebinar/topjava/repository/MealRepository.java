package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MealRepository {

    private static final ConcurrentHashMap<Integer, Meal> ID_TO_MEAL = new ConcurrentHashMap<>();

    public static void add(Meal meal) {
        innerAdd(meal);
    }

    private static void innerAdd(Meal meal) {
        ID_TO_MEAL.put(meal.getId(), meal);
    }

    public static void update(Meal meal) {
        ID_TO_MEAL.put(meal.getId(), meal);
    }

    public static List<Meal> getAll() {
        return new ArrayList<>(ID_TO_MEAL.values());
    }

    public static Meal getById(int id) {
        return ID_TO_MEAL.get(id);
    }

    public static void deleteById(int id) {
        ID_TO_MEAL.remove(id);
    }
}
