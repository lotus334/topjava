package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    /**
     * @param meals          list of UserMeal
     * @param startTime      the beginning of the period inclusive
     * @param endTime        end of the exclusive period
     * @param caloriesPerDay the allowed number of calories is inclusive
     * @return an empty list if meals do not fall within the period, otherwise - a list of UserMealWithExcess
     */
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateToCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            dateToCalories.merge(
                    meal.getDateTime().toLocalDate(),
                    meal.getCalories(),
                    Integer::sum
            );
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(
                        UserMealWithExcess.builder()
                                .dateTime(meal.getDateTime())
                                .description(meal.getDescription())
                                .calories(meal.getCalories())
                                .excess(isExcess(dateToCalories.get(meal.getDateTime().toLocalDate()), caloriesPerDay))
                                .build()
                );
            }
        }
        return result;
    }

    /**
     * @param meals          list of UserMeal
     * @param startTime      the beginning of the period inclusive
     * @param endTime        end of the exclusive period
     * @param caloriesPerDay the allowed number of calories is inclusive
     * @return an empty list if meals do not fall within the period, otherwise - a list of UserMealWithExcess
     */
    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateToCalories = new HashMap<>();
        meals.forEach(
                meal -> dateToCalories.merge(
                        meal.getDateTime().toLocalDate(),
                        meal.getCalories(),
                        Integer::sum
                )
        );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(
                        meal -> UserMealWithExcess.builder()
                                .dateTime(meal.getDateTime())
                                .description(meal.getDescription())
                                .calories(meal.getCalories())
                                .excess(isExcess(dateToCalories.get(meal.getDateTime().toLocalDate()), caloriesPerDay))
                                .build()
                ).collect(Collectors.toList());
    }

    private static boolean isExcess(int calories, int caloriesPerDay) {
        return calories > caloriesPerDay;
    }
}
