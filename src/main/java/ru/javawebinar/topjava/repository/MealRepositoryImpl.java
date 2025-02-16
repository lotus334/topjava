package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepositoryImpl implements MealRepository {
    private final AtomicInteger idGenerator = new AtomicInteger();
    private final ConcurrentHashMap<Integer, Meal> idToMeal = new ConcurrentHashMap<>();

    @Override
    public Meal add(LocalDateTime dateTime, String description, int calories) {
        int generatedId = idGenerator.getAndIncrement();
        return idToMeal.put(
                generatedId,
                new Meal(generatedId, dateTime, description, calories)
        );
    }

    @Override
    public Meal update(int id, LocalDateTime dateTime, String description, int calories) {
        return idToMeal.computeIfPresent(id, (integer, meal) -> new Meal(integer, dateTime, description, calories));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(idToMeal.values());
    }

    @Override
    public Meal getById(int id) {
        return idToMeal.get(id);
    }

    @Override
    public void deleteById(int id) {
        idToMeal.remove(id);
    }
}
