<%@ page import="ru.javawebinar.topjava.model.MealTo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <%
        List<MealTo> meals1 = (List<MealTo>) request.getAttribute("meals");
        DateTimeFormatter formatter = (DateTimeFormatter) request.getAttribute("formatter");
        for (MealTo meal : meals1) {
            boolean isExcess = meal.isExcess();
    %>
    <tr style="color: <%=isExcess ? "red" : "green"%>">
        <td>
            <%=meal.getDateTime().format(formatter)%>
        </td>
        <td>
            <%=meal.getDescription()%>
        </td>
        <td>
            <%=meal.getCalories()%>
        </td>
    </tr>
    <%
        }
    %>
</table>
<button onclick="window.history.back()" type="button">Cancel</button>
</body>
</html>