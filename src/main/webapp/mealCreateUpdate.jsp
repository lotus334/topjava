<%@ page import="ru.javawebinar.topjava.model.Meal" %>
<%@ page import="ru.javawebinar.topjava.util.StringUtil" %>
<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<%
    Meal meal = (Meal) request.getAttribute("meal");
    String dateTimeStr = StringUtil.getEmptyIfNull(meal == null ? null : meal.getDateTime());
    String description = StringUtil.getEmptyIfNull(meal == null ? null : meal.getDescription());
    String caloriesStr = StringUtil.getEmptyIfNull(meal == null ? null : meal.getCalories());
    String mealIdStr = StringUtil.getEmptyIfNull(meal == null ? null : meal.getId());
%>
<form method="POST" action='meals'>
    <%
        if (Objects.nonNull(meal)) {
    %>
    Идентификатор : <input type="number" readonly="readonly" name="mealId" value="<%=mealIdStr%>"/><br/>
    <%
        }
    %>
    Дата/Время : <input type="datetime-local" name="dateTime" value="<%=dateTimeStr%>"/><br/>
    Описание : <input type="text" name="description" value="<%=description%>"/><br/>
    Калории : <input type="number" name="calories" value="<%=caloriesStr%>"/><br/>
    <input type="submit" value="Отправить"/>
</form>
</body>
</html>