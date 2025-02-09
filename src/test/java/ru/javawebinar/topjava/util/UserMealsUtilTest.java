package ru.javawebinar.topjava.util;

import org.junit.Test;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtilTest {

    @Test
    public void filteredByCycles_returnsMeals_whenMealsInPeriod() {
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0);
        String description = "Завтрак";
        int calories = 500;
        List<UserMeal> meals = Collections.singletonList(
                new UserMeal(dateTime, description, calories)
        );
        List<UserMealWithExcess> expected = Collections.singletonList(
                UserMealWithExcess.builder().dateTime(dateTime).description(description).calories(calories).excess(false).build()
        );
        filteredByCyclesTest(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000, expected);
    }

    @Test
    public void filteredByStreams_returnsMeal_whenMealsInPeriod() {
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0);
        String description = "Завтрак";
        int calories = 500;
        List<UserMeal> meals = Collections.singletonList(
                new UserMeal(dateTime, description, calories)
        );
        List<UserMealWithExcess> expected = Collections.singletonList(
                UserMealWithExcess.builder().dateTime(dateTime).description(description).calories(calories).excess(false).build()
        );
        filteredByStreamsTest(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000, expected);
    }

    @Test
    public void filteredByCycles_returnsEmptyList_whenNoMealsInPeriod() {
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0);
        String description = "Обед";
        int calories = 1000;
        List<UserMeal> meals = Collections.singletonList(
                new UserMeal(dateTime, description, calories)
        );
        List<UserMealWithExcess> expected = Collections.emptyList();
        filteredByCyclesTest(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000, expected);
    }

    @Test
    public void filteredByCycles_returnsSomeMeals_whenMealsInPeriod() {
        List<UserMeal> meals = new ArrayList<>();
        List<UserMealWithExcess> expected = new ArrayList<>();

        fillMealsAndExpectedMealsLists(meals, expected);

        filteredByCyclesTest(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000, expected);
    }

    @Test
    public void filteredByStreams_returnsSomeMeals_whenMealsInPeriod() {
        List<UserMeal> meals = new ArrayList<>();
        List<UserMealWithExcess> expected = new ArrayList<>();

        fillMealsAndExpectedMealsLists(meals, expected);

        filteredByStreamsTest(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000, expected);
    }

    private void fillMealsAndExpectedMealsLists(List<UserMeal> meals, List<UserMealWithExcess> expected) {
        LocalDateTime dateTime1 = LocalDateTime.of(2020, Month.JANUARY, 30, 7, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2020, Month.JANUARY, 30, 11, 59);
        LocalDateTime dateTime3 = LocalDateTime.of(2020, Month.JANUARY, 31, 7, 0);
        LocalDateTime dateTime4 = LocalDateTime.of(2020, Month.JANUARY, 31, 11, 59);
        String description1 = "Завтрак";
        String description2 = "Обед";
        String description3 = "Завтрак";
        String description4 = "Обед";
        int calories1 = 500;
        int calories2 = 1000;
        int calories3 = 1000;
        int calories4 = 1;
        meals.addAll(
                Arrays.asList(
                        new UserMeal(dateTime1, description1, calories1),
                        new UserMeal(dateTime2, description2, calories2),
                        new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                        new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 500),
                        new UserMeal(dateTime3, description3, calories3),
                        new UserMeal(dateTime4, description4, calories4),
                        new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 500)
                )
        );

        expected.addAll(
                Stream.of(
                        UserMealWithExcess.builder().dateTime(dateTime1).description(description1).calories(calories1).excess(false).build(),
                        UserMealWithExcess.builder().dateTime(dateTime2).description(description2).calories(calories2).excess(false).build(),
                        UserMealWithExcess.builder().dateTime(dateTime3).description(description3).calories(calories3).excess(true).build(),
                        UserMealWithExcess.builder().dateTime(dateTime4).description(description4).calories(calories4).excess(true).build()
                ).collect(Collectors.toList())
        );
    }

    private void filteredByCyclesTest(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay, List<UserMealWithExcess> expected) {
        List<UserMealWithExcess> mealsWithExcessList = UserMealsUtil.filteredByCycles(meals, startTime, endTime, caloriesPerDay);
        if (!Objects.equals(expected, mealsWithExcessList)) {
            throw new AssertionError();
        }
    }

    private void filteredByStreamsTest(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay, List<UserMealWithExcess> expected) {
        List<UserMealWithExcess> mealsWithExcessList = UserMealsUtil.filteredByStreams(meals, startTime, endTime, caloriesPerDay);
        if (!Objects.equals(expected, mealsWithExcessList)) {
            throw new AssertionError();
        }
    }
}