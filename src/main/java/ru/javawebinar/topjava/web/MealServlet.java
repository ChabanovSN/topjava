package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.MEALS;

public class MealServlet extends HttpServlet {

     private static final Logger log = getLogger(MealServlet.class);
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              log.info("get meals");
              request.setAttribute("mealList", MealsUtil.getFilteredWithExcess(MEALS,
                      LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
              request.getRequestDispatcher("mealList.jsp").forward(request,response);
    }
}
