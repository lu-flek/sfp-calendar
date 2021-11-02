package controllers;

import models.Event;
import models.User;
import services.EventService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.SortedMap;

public class CalendarController extends HttpServlet implements Controller {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDate today;
        if (request.getSession().getAttribute("today") == null) {
            today = LocalDate.now();
        } else {
            today = LocalDate.parse(request.getSession().getAttribute("today").toString());
        }
        request.getSession().setAttribute("today", today);
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd = today.withDayOfMonth(today.lengthOfMonth());

        EventService service = new EventService();
        SortedMap<User, List<List<Event>>> getUserEvents = service.getUserEvents(today.getYear(), today.getMonthValue());

        request.setAttribute("getUserEvents", getUserEvents);
        request.setAttribute("today", today);
        request.setAttribute("monthStart", monthStart);
        request.setAttribute("monthEnd", monthEnd);

        showPage(request, response, "calendar.jsp");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        LocalDate today = LocalDate.parse(request.getParameter("today"));

        if (action.equals("previous")) {
            today = today.minusMonths(1);
            request.getSession().setAttribute("today", today);
        } else if (action.equals("next")) {
            today = today.plusMonths(1);
            request.getSession().setAttribute("today", today);
        }

        request.getRequestDispatcher("pages/calendar.jsp").forward(request, response);
    }
}
