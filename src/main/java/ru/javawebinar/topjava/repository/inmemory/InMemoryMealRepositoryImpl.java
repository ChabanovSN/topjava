package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.MEALS;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    public static  final Comparator<Meal> MEAL_COMPARATOR = (m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime());

    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
       fillRepository();
    }

    @Override
    public Meal save(Meal meal,int userId) {
        log.info("save {}", userId, meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }else if(get(meal.getId(),userId)==null)
            return null;

     Map<Integer,Meal>  mealMap =  repository.computeIfAbsent(userId, ConcurrentHashMap::new);
     mealMap.put(meal.getId(),meal);
     return meal;
    }

    @Override
    public boolean delete(int id,int userId) {
        log.info("delete {}", id,userId);
        Map<Integer,Meal>  mealMap = repository.get(userId);
       return mealMap !=null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id,int userId) {
        log.info("get {}", id,userId);
        Map<Integer,Meal>  mealMap = repository.get(userId);
        return mealMap ==null ? null : mealMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll {}", userId);
        Objects.requireNonNull(userId);
      return repository.get(userId).values().stream().sorted(MEAL_COMPARATOR).collect(Collectors.toList());
    }

    @Override
    public List<Meal> filteredTime(int userId,LocalDateTime start, LocalDateTime end) {
        log.info("filteredTime {}", userId);
        Objects.requireNonNull(userId);
        return repository.get(userId).values().stream()
                .filter(m -> DateTimeUtil.isBetween(m.getDateTime(),start,end))
                .sorted(MEAL_COMPARATOR).collect(Collectors.toList());
    }

    private void fillRepository() {
        int counterTo6 = 0;
        int idUser = 1;
        for (int i = 0; i < MEALS.size(); i++) {
            ++counterTo6;
            save(MEALS.get(i), idUser);
            if (counterTo6 == 6) {
                counterTo6 = 0;
                ++idUser;

            }
        }
    }
}

