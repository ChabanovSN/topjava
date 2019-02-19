package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);


    private ConfigurableApplicationContext appCtx;
    private MealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("action").equals("timefilter")) {
            log.info("filter time and/or date");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            request.setAttribute("meals", controller.getAllFiltered(startDate, endDate, startTime, endTime));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                String id = request.getParameter("id");

                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
                Meal meal = new Meal(SecurityUtil.authUserId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                String datetime = request.getParameter("dateTime");
                String description = request.getParameter("description");
                String calories = request.getParameter("calories");
                if (description != null) {
                    controller.create(datetime, description, calories);
                }
                break;
            case "update":
                meal = controller.get(request.getParameter("id"));
                System.out.println("UPDATE  " + meal);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                datetime = request.getParameter("dateTime");
                description = request.getParameter("description");
                calories = request.getParameter("calories");
                String idString = request.getParameter("id");
                if (description != null)
                    controller.update(datetime, description, calories, idString);

                break;

            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", controller.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }


    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }
}
