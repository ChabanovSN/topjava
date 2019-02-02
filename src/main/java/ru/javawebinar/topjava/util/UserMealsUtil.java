package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        filteredWithExceeded.forEach(System.out::println);

        List<UserMealWithExceed> filteredWithExceededList = getFilteredWithExceededList(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        filteredWithExceededList.forEach(System.out::println);

//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> collectCaloriesBuDays = mealList.stream().collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));

        return mealList.stream().filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(),
                        collectCaloriesBuDays.get(m.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

    }
    //Optional 2
    public static List<UserMealWithExceed> getFilteredWithExceededList(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> collectCaloriesBuDays = new HashMap<>();
        for(UserMeal m : mealList){
            collectCaloriesBuDays.put(m.getDateTime().toLocalDate(),
                    collectCaloriesBuDays.getOrDefault(m.getDateTime().toLocalDate(),0) + m.getCalories());
        }

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();
        for(UserMeal m : mealList){
            if(TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                userMealWithExceedList.add(new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(),
                        collectCaloriesBuDays.get(m.getDateTime().toLocalDate()) > caloriesPerDay));
        }
        return userMealWithExceedList;

    }
}
