package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {


    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository =repository;
    }

    @Override
    public Meal create(Meal meal,int userId) {
        return repository.save(meal,userId);
    }

    @Override
    public void delete(int id,int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id,userId), id);
    }

    @Override
    public Meal get(int id,int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id,userId), id);
    }

    @Override
    public void update(Meal meal,int userId) {
        checkNotFoundWithId(repository.save(meal,userId), meal.getId());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<Meal> filteredTime(int userId, LocalDate startDate, LocalDate endDate,
                                   LocalTime startTime, LocalTime endTime) {

        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);
        return repository.filteredTime(userId,start,end);
    }

    @Override
    public List<MealTo> getWithExcess(int userId, int calories) {
        return  MealsUtil.getWithExcess(getAll(userId), calories);
    }
}