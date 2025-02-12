<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<%@ page import="ru.javawebinar.topjava.util.StringUtil" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<%
    Meal meal = (Meal) request.getAttribute("meal");
    String dateTimeStr = StringUtil.getEmptyIfNull(meal == null ? null : meal.getDateTime());
    String description = StringUtil.getEmptyIfNull(meal == null ? null : meal.getDescription());
    String caloriesStr = StringUtil.getEmptyIfNull(meal == null ? null : meal.getCalories());
    String h2 = meal == null ? "Создание еды" : "Редактирование еды";
%>
<h2><%=h2%></h2>
<form method="POST" action='meals'>
    Дата/Время : <input type="datetime-local" name="dateTime" value="<%=dateTimeStr%>"/><br/>
    Описание : <input type="text" name="description" value="<%=description%>"/><br/>
    Калории : <input type="number" name="calories" value="<%=caloriesStr%>"/><br/>
    <input type="submit" value="Отправить"/>
</form>
</body>
</html>