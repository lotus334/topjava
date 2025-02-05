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
     * @param meal           приём пищи
     * @param startTime      начало периода включительно
     * @param endTime        конец периода исключительно
     * @param caloriesPerDay допустимое количество калорий включительно
     * @return null если приём пищи не попадает в период, иначе - Приём пищи с анализом переедания
     */
    public static UserMealWithExcess filteredByCycles(UserMeal meal, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return filteredByCycles(Collections.singletonList(meal), startTime, endTime, caloriesPerDay).stream().findFirst().orElse(null);
    }

    /**
     * @param meals          приёмы пищи
     * @param startTime      начало периода включительно
     * @param endTime        конец периода исключительно
     * @param caloriesPerDay допустимое количество калорий включительно
     * @return пустой список если приёмы пищи не попадает в период, иначе - список приёмов пищи с анализом переедания
     */
    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> dateToCalories = new HashMap<>();

        for (UserMeal meal : meals) {
            dateToCalories.merge(
                    meal.getDateTime().toLocalDate(),
                    meal.getCalories(),
                    Integer::sum
            );
        }

        for (UserMeal meal : meals) {
            if (isInPeriod(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(
                        new UserMealWithExcess(
                                meal,
                                isExcess(dateToCalories.get(meal.getDateTime().toLocalDate()), caloriesPerDay)
                        )
                );
            }
        }
        return result;
    }

    /**
     * @param meal           приём пищи
     * @param startTime      начало периода включительно
     * @param endTime        конец периода исключительно
     * @param caloriesPerDay допустимое количество калорий включительно
     * @return null если приём пищи не попадает в период, иначе - Приём пищи с анализом переедания
     */
    public static UserMealWithExcess filteredByStreams(UserMeal meal, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return filteredByStreams(Collections.singletonList(meal), startTime, endTime, caloriesPerDay).stream().findFirst().orElse(null);
    }

    /**
     * @param meals          приёмы пищи
     * @param startTime      начало периода включительно
     * @param endTime        конец периода исключительно
     * @param caloriesPerDay допустимое количество калорий включительно
     * @return пустой список если приёмы пищи не попадает в период, иначе - список приёмов пищи с анализом переедания
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
                .filter(meal -> isInPeriod(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(
                        meal -> new UserMealWithExcess(
                                meal,
                                isExcess(dateToCalories.get(meal.getDateTime().toLocalDate()), caloriesPerDay)
                        )
                ).collect(Collectors.toList());
    }

    public static boolean isMealsEquals(UserMeal meal, UserMealWithExcess mealWithExcess) {
        return meal.getDateTime().isEqual(mealWithExcess.getDateTime())
                && meal.getDescription().compareTo(mealWithExcess.getDescription()) == 0
                && Objects.equals(meal.getCalories(), mealWithExcess.getCalories());
    }

    private static boolean isInPeriod(LocalTime localTime, LocalTime startTime, LocalTime endTime) {
        return !localTime.isBefore(startTime) && localTime.isBefore(endTime);
    }

    private static boolean isExcess(int calories, int caloriesPerDay) {
        return calories > caloriesPerDay;
    }
}
