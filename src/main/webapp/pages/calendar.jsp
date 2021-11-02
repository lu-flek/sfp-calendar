<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.TextStyle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Map" %>
<%@ page import="models.User" %>
<%@ page import="models.EventType" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Event" %>
<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- JSTL -->
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <title>Calendar</title>

    <style>
        .VACATION {
            background-color: #f0ad4e ;
        }

        .PUBLIC_HOLIDAY {
            background-color: #5cb85c;
        }

        .DAY_OFF {
            background-color: #d9534f ;
        }

        .BIRTHDAY {
            background-color: #0275d8;
        }

        .OTHER {
            background-color: #5bc0de;
        }
    </style>
</head>

<body>

<nav class="navbar navbar-expand-lg bg-light">
    <a class="navbar-brand text-info" href="/calendar">Calendar</a>
    <ul class="navbar-nav ml-auto">
        <li class="nav-item">
            <form method="post" action="/logout">
                <button type="submit" class="btn btn-info">Logout</button>
            </form>
        </li>
    </ul>
</nav>
<%
    LocalDate today = (LocalDate) request.getAttribute("today");
    LocalDate monthStart = (LocalDate) request.getAttribute("monthStart");
    LocalDate monthEnd = (LocalDate) request.getAttribute("monthEnd");

    Map<User, List<List<Event>>> getUserEvents = (Map<User, List<List<Event>>>) request.getAttribute("getUserEvents");
%>
<form method="post" action="/calendar">
    <div class="container">
        <div class="row justify-content-md-center">
            <input type="hidden" name="today" value=${today}>
            <div class="col-md-auto">
                <button type="submit" class="btn btn-outline-success" name="action" value="previous">Previous</button>
            </div>
            <div class="col-md-auto">
                <div class="text-center"><%= today.getMonth().getDisplayName(TextStyle.FULL, Locale.US)%>
                </div>
                <div class="text-center"><%= today.getYear()%>
                </div>
            </div>
            <div class="col-md-auto">
                <button type="submit" class="btn btn-outline-success" name="action" value="next">Next</button>
            </div>
        </div>
    </div>
</form>

<div>
    <table class="mw-100 p-3 table table-bordered table-condensed table-sm">
        <tr>
            <td></td>
            <%for (LocalDate iterate = monthStart; iterate.isBefore(monthEnd.plusDays(1)); iterate = iterate.plusDays(1)) {%>
            <td class="text-center">
                <%=iterate.getDayOfMonth()%><br/>
                <%=iterate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US)%>
            </td>
            <%}%>
        </tr>
        <% for (User user : getUserEvents.keySet()) {%>
        <tr>
            <td><%=user.getName()%>
            </td>
            <% List<List<Event>> days = getUserEvents.get(user);
                for (int i = 0; i < days.size(); i++) {
                    List<Event> events = days.get(i);
                    if (events.size() > 0) {
                        StringBuilder builder = new StringBuilder();
                        for (Event event : events) {
                            builder.append(event.getName()).append("\n");
                        }%>
            <td class="<%=events.get(0).getType()%>" title="<%=builder%>"></td>
            <%} else {%>
            <td></td>
            <%}%>
            <%}%>
        </tr>
        <%}%>
    </table>
</div>


<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous">
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous">
</script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
        integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous">
</script>
</body>

</html>