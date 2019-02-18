package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MealsUtil {
    public static final List<Meal> MEALS = fillList();

    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static List<MealTo> getWithExcess(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExcess(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredWithExcess(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return getFilteredWithExcess(meals, caloriesPerDay, meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }

    private static List<MealTo> getFilteredWithExcess(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createWithExcess(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(toList());
    }

    public static MealTo createWithExcess(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }



    private static List<Meal> fillList() {
        List<Meal> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int n = i;
            ++n;
            list.add(new Meal( LocalDateTime.of(2019, Month.of(n), n+3, 10, 0), "Завтрак", 500));
            list.add(new Meal( LocalDateTime.of(2019, Month.of(n), n+3, 13, 0), "Обед", 1000));
            list.add(new Meal( LocalDateTime.of(2019, Month.of(n), n+3, 20, 0), "Ужин", 500));
            list.add(new Meal( LocalDateTime.of(2019, Month.of(n), n+4, 10, 0), "Завтрак", 1000));
            list.add(new Meal( LocalDateTime.of(2019, Month.of(n), n+4, 13, 0), "Обед", 500));
            list.add(new Meal( LocalDateTime.of(2019, Month.of(n), n+4, 20, 0), "Ужин", 510));
        }
        return list;
    }
}