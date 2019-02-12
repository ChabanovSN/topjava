<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>Users</title>
    <style>
        <%@include file="style.css"%>
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Список еды</h2>
<table>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    </thead>
    <c:forEach items="${mealList}" var="meal">
        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealTo"/>
          <tr class="${meal.excess ? 'exceed' : 'norm'}">

            <td>
                <%=TimeUtil.farmDate(meal.getDateTime())%>
            </td>
            <td>
                    ${meal.description}
            </td>
            <td>
                    ${meal.calories}
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>