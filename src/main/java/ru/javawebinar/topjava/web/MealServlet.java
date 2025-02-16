package ru.javawebinar.topjava.web;

import com.sun.istack.internal.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

// Servlets are singleton by 3.0v
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    public static final int CALORIES_PER_DAY = 2000;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final String MEAL_CREATE_UPDATE_JSP = "/mealCreateUpdate.jsp";
    public static final String MEALS_JSP = "/meals.jsp";
    private static final MealRepository MEAL_REPOSITORY = new MealRepositoryImpl();

    @Override
    public void init() throws ServletException {
        MEAL_REPOSITORY.add(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        MEAL_REPOSITORY.add(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        MEAL_REPOSITORY.add(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        MEAL_REPOSITORY.add(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        MEAL_REPOSITORY.add(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        MEAL_REPOSITORY.add(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
        MEAL_REPOSITORY.add(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        switch (ActionType.fromString(request.getParameter("action"))) {
            case CREATE:
                request.getRequestDispatcher(MEAL_CREATE_UPDATE_JSP).forward(request, response);
                break;

            case UNKNOWN:
            case READ:
                request.setAttribute("meals", MealsUtil.filteredByStreams(MEAL_REPOSITORY.getAll(), CALORIES_PER_DAY));
                request.setAttribute("formatter", FORMATTER);
                request.getRequestDispatcher(MEALS_JSP).forward(request, response);
                break;

            case UPDATE:
                Integer id = getId(request);
                if (id != null) {
                    request.setAttribute("meal", MEAL_REPOSITORY.getById(id));
                    request.getRequestDispatcher(MEAL_CREATE_UPDATE_JSP).forward(request, response);
                } else {
                    response.sendRedirect("meals");
                }
                break;

            case DELETE:
                Integer mealId = getId(request);
                if (mealId != null) {
                    MEAL_REPOSITORY.deleteById(mealId);
                    request.setAttribute("meals", MealsUtil.filteredByStreams(MEAL_REPOSITORY.getAll(), CALORIES_PER_DAY));
                    request.setAttribute("formatter", FORMATTER);
                }
                response.sendRedirect("meals");
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
            MEAL_REPOSITORY.update(id, dateTime, description, calories);
        } else {
            MEAL_REPOSITORY.add(dateTime, description, calories);
        }

        response.sendRedirect("meals");
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
        UNKNOWN(null),
        ;

        private final String name;

        public static ActionType fromString(@Nullable String name) {
            ActionType foundedActionType = Arrays.stream(values()).filter(actionType -> Objects.equals(actionType.name, name)).findFirst().orElse(null);
            if (Objects.isNull(foundedActionType)) {
                return UNKNOWN;
            }
            return foundedActionType;
        }
    }
}
