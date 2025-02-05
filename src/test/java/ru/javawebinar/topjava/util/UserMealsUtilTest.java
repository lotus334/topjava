package ru.javawebinar.topjava.util;

import org.junit.Test;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class UserMealsUtilTest {

    @Test
    public void filteredByCycles() {
        UserMeal meal1 = new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        UserMealWithExcess mealWith1 = UserMealsUtil.filteredByCycles(meal1, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        assert mealWith1 != null;
        assert UserMealsUtil.isMealsEquals(meal1, mealWith1);

        UserMealWithExcess mealWith1_1 = UserMealsUtil.filteredByStreams(meal1, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        assert mealWith1_1 != null;
        assert UserMealsUtil.isMealsEquals(meal1, mealWith1);

        UserMealWithExcess mealWith2 = UserMealsUtil.filteredByCycles(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000
        );
        assert mealWith2 == null;

        UserMealWithExcess mealWith3 = UserMealsUtil.filteredByCycles(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000
        );
        assert mealWith3 == null;

        UserMealWithExcess mealWith4 = UserMealsUtil.filteredByCycles(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000
        );
        assert mealWith4 == null;

        UserMealWithExcess mealWith5 = UserMealsUtil.filteredByCycles(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000
        );
        assert mealWith5 != null;

        UserMealWithExcess mealWith6 = UserMealsUtil.filteredByCycles(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000
        );
        assert mealWith6 == null;

        UserMealWithExcess mealWith7 = UserMealsUtil.filteredByCycles(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000
        );
        assert mealWith7 == null;

        // Pers
        UserMealWithExcess mealWith8 = UserMealsUtil.filteredByCycles(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 11, 59), "Завтрак", 2001),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000
        );
        assert mealWith8 != null;

        UserMealWithExcess mealWith9 = UserMealsUtil.filteredByCycles(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 12, 0), "Завтрак", 2000),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000
        );
        assert mealWith9 == null;

        UserMealWithExcess mealWith10 = UserMealsUtil.filteredByCycles(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 7, 0), "Завтрак", 2000),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000
        );
        assert mealWith10 != null;

        UserMealWithExcess mealWith11 = UserMealsUtil.filteredByCycles(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 6, 59), "Завтрак", 2000),
                LocalTime.of(7, 0),
                LocalTime.of(12, 0),
                2000
        );
        assert mealWith11 == null;
    }

    @Test
    public void filteredByCyclesCheckExcess() {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 7, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 11, 59), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 7, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 11, 59), "Обед", 1),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 500)
        );

        List<UserMealWithExcess> mealsByCycles = UserMealsUtil.filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        assert mealsByCycles.stream().filter(meal -> !meal.getExcess()).count() == 2;

        assert mealsByCycles.stream().filter(UserMealWithExcess::getExcess).count() == 2;

        List<UserMealWithExcess> mealsByStreams = UserMealsUtil.filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        assert mealsByStreams.stream().filter(meal -> !meal.getExcess()).count() == 2;

        assert mealsByStreams.stream().filter(UserMealWithExcess::getExcess).count() == 2;

    }
}