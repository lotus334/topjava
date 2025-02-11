<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border=1>
    <thead>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th>Редактирование</th>
    </tr>
    </thead>
    <tbody>
    <%
        List<MealTo> meals = (List<MealTo>) request.getAttribute("meals");
        DateTimeFormatter formatter = (DateTimeFormatter) request.getAttribute("formatter");
        for (MealTo meal : meals) {
            boolean isExcess = meal.isExcess();
            LocalDateTime dateTime = meal.getDateTime();
            String description = meal.getDescription();
            int calories = meal.getCalories();
            int mealId = meal.getId();
    %>
    <tr style="color: <%=isExcess ? "red" : "green"%>">
        <td>
            <%=dateTime.format(formatter)%>
        </td>
        <td>
            <%=description%>
        </td>
        <td>
            <%=calories%>
        </td>
        <td>
            <a href="meals?action=update&mealId=<%=mealId%>">Обновить</a>
        </td>
        <td>
            <a href="meals?action=delete&mealId=<%=mealId%>">Удалить</a>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
<button onclick="location.href='meals?action=create'" type="button">Добавить</button>
</body>
</html>