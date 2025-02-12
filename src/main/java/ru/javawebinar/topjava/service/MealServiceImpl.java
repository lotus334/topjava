package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MealServiceImpl implements MealService {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    @Override
    public void add(LocalDateTime dateTime, String description, int calories) {
        int generatedId = ID_GENERATOR.getAndIncrement();
        MealRepository.add(
                new Meal(generatedId, dateTime, description, calories)
        );
    }

    @Override
    public void update(int id, LocalDateTime dateTime, String description, int calories) {
        MealRepository.update(
                new Meal(id, dateTime, description, calories)
        );
    }

    @Override
    public List<Meal> getAll() {
        return MealRepository.getAll();
    }

    @Override
    public Meal getById(int id) {
        return MealRepository.getById(id);
    }

    @Override
    public void deleteById(int id) {
        MealRepository.deleteById(id);
    }
}
