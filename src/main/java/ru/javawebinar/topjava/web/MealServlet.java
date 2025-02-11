package ru.javawebinar.topjava.web;

import com.sun.istack.internal.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    public static final int CALORIES_PER_DAY = 2000;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final String MEAL_CREATE_UPDATE_JSP = "/mealCreateUpdate.jsp";
    public static final String MEALS_JSP = "/meals.jsp";

    // TODO начитка временная
    static {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        MealRepository.addMeals(meals);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        switch (ActionType.fromString(request.getParameter("action"))) {
            case CREATE:
                request.getRequestDispatcher(MEAL_CREATE_UPDATE_JSP).forward(request, response);
                break;

            case READ:
                request.setAttribute("meals", MealsUtil.filteredByStreams(MealRepository.getMeals(), LocalTime.of(7, 0), LocalTime.of(14, 0), CALORIES_PER_DAY));
                request.setAttribute("formatter", FORMATTER);
                request.getRequestDispatcher(MEALS_JSP).forward(request, response);
                break;

            case UPDATE:
                request.setAttribute("meal", MealRepository.getMealById(getId(request)));
                request.getRequestDispatcher(MEAL_CREATE_UPDATE_JSP).forward(request, response);
                break;

            case DELETE:
                MealRepository.deleteMealById(getId(request));
                request.setAttribute("meals", MealsUtil.filteredByStreams(MealRepository.getMeals(), LocalTime.of(7, 0), LocalTime.of(14, 0), CALORIES_PER_DAY));
                request.setAttribute("formatter", FORMATTER);
                request.getRequestDispatcher(MEALS_JSP).forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Integer id = getId(request);

        if (Objects.nonNull(id)) {
            Meal meal = new Meal(id, dateTime, description, calories);
            MealRepository.updateMeal(meal);
        } else {
            Meal meal = new Meal(dateTime, description, calories);
            MealRepository.addMeal(meal);
        }

        response.sendRedirect("index.html");
    }

    private Integer getId(HttpServletRequest request) {
        String idStr = request.getParameter("mealId");
        return Objects.nonNull(idStr) ? Integer.parseInt(idStr) : null;
    }

    @AllArgsConstructor
    @Getter
    private enum ActionType {
        CREATE("create"),
        READ("read"),
        UPDATE("update"),
        DELETE("delete"),
        ;

        private final String name;

        public static ActionType fromString(@Nullable String name) {
            if (Objects.isNull(name)) {
                return READ;
            }
            return Arrays.stream(values()).filter(actionType -> Objects.equals(actionType.name, name)).findFirst().orElse(null);
        }
    }
}
