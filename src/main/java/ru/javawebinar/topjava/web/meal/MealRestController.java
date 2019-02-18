package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Controller
public class MealRestController {

     public final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

   public   Meal create(Meal meal, int userId){
       return service.create(meal,userId);
    }

   public void delete(int id,int userId) throws NotFoundException{
        service.delete(id,userId);
    }

   public Meal get(int id,int userId) throws NotFoundException{
       int uathId = SecurityUtil.authUserId();
        return service.get(id,uathId);
    }

   public void update(Meal meal,int userId){
       service.update(meal,userId);
   }

   public List<Meal> getAll(int userId){
        return service.getAll(userId);
   }

    public List<Meal> filteredTime(int userId, LocalDate startDate, LocalDate endDate,
                                   LocalTime startTime, LocalTime endTime) {
        return service.filteredTime(userId, startDate, endDate, startTime, endTime);
    }
    public List<MealTo> getWithExcess(int userId, int calories){
        return service.getWithExcess(userId,calories);
    }
}