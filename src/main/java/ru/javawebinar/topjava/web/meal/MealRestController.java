package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {

    public final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal get(String id) {
        int idInt = Integer.parseInt(id);
        log.info("get {}", idInt);
        return service.get(idInt, SecurityUtil.authUserId());
    }

    public Meal create(String datetime,String description, String calories ) {
                Meal meal = new Meal(null,
                LocalDateTime.parse(datetime),
                description,Integer.parseInt(calories));
        checkNew(meal);
        log.info("create {}", meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(String id) {
        int idInt = Integer.parseInt(id);
        log.info("delete {}", idInt);
        service.delete(idInt, SecurityUtil.authUserId());
    }

    public Meal update(String datetime,String description, String calories, String id) {
        System.out.println("mealresr contralerr update "+id+" d"+datetime+"des"+description);
        int idInt = Integer.parseInt(id);
        Meal meal = new Meal(idInt,
                LocalDateTime.parse(datetime),
                description,Integer.parseInt(calories));
        log.info("update {} with id={}", meal, idInt);
        assureIdConsistent(meal, idInt);
        service.update(meal, SecurityUtil.authUserId());
        return meal;
    }

   public List<MealTo> getAll(){
       log.info("getAll");
       return MealsUtil.getWithExcess(service
               .getAll(SecurityUtil.authUserId(), LocalDate.MIN, LocalDate.MAX), SecurityUtil.authUserCaloriesPerDay());
   }

    public List<MealTo> getAllFiltered(String startDate, String endDate, String startTime, String endTime) {
        log.info("getAllFiltered");
        LocalDate startD = startDate.equals("") ? null : LocalDate.parse(startDate);
        LocalDate endD = endDate.equals("") ? null : LocalDate.parse(endDate);
        LocalTime startT = startTime.equals("") ? null : LocalTime.parse(startTime);
        LocalTime endT = endTime.equals("") ? null : LocalTime.parse(endTime);
        return MealsUtil.getFilteredWithExcess(service
                        .getAll (SecurityUtil.authUserId(),
                                startDate == null ? LocalDate.MIN : startD,
                                endDate == null ? LocalDate.MAX : endD),
                SecurityUtil.authUserCaloriesPerDay(),
                startTime == null ? LocalTime.MIN : startT,
                endTime == null ? LocalTime.MAX : endT);
    }
}