package services;

import DAO.EventDAO;
import models.Event;
import models.User;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class EventService {
    EventDAO eventDAO = new EventDAO();

    public Event createEvent(User user, Event event) {
        Event newEvent = eventDAO.createEvent(event);
        Set<User> users = newEvent.getUsers();

        if (event.isPublic()) {
            users.addAll(new UserService().getAllUsers());
        } else {
            users.add(user);
        }

        users.forEach(u -> eventDAO.createRow(u, newEvent));
        return newEvent;
    }

    public void deleteEvent(Event event) {
        eventDAO.deleteEvent(event);
    }

    public Event findEventById(int id) {
        return eventDAO.findEventByID(id).get();
    }

    public List<Event> getEventsByMonth(int year, int month) {
        return eventDAO.getEventsByMonth(year, month);
    }

    public List<Event> getPublicEvents() {
        return eventDAO.getPublicEvents();
    }

    public List<Event> getAllEventsOfUserByUserId(int userId) {
        return eventDAO.getAllEventsOfUserByUserId(userId);
    }

    public void createRow(User user, Event event) {
        eventDAO.createRow(user, event);
    }

    public SortedMap<User, List<List<Event>>> getUserEvents(int year, int month) {
        SortedMap<User, List<List<Event>>> calendar = new TreeMap<>();

        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        LocalDate firstOfMonth = LocalDate.of(year, month, 1);
        LocalDate lastOfMonth = firstOfMonth.plusMonths(1).minusDays(1);

        List<User> allUsers = new UserService().getAllUsers();
        List<Event> allEventsInMonth = getEventsByMonth(year, month);

        allUsers.forEach(user -> {
            List<List<Event>> days = new ArrayList<>();
            for (int i = 0; i < daysInMonth; i++) {
                days.add(new ArrayList<>());
            }
            calendar.put(user, days);
        });

        calendar.forEach((user, days) -> {
            List<Event> allEventsOfUser = getAllEventsOfUserByUserId(user.getId());
            allEventsOfUser.forEach(event -> {

                if (allEventsInMonth.contains(event)) {
                    if (event.isOneDayEvent()) {
                        days.get(event.getStart().getDayOfMonth() - 1).add(event);
                    } else {
                        if (event.getStart().isBefore(firstOfMonth) && event.getEnd().isAfter(lastOfMonth)) {
                            for (int i = 0; i < daysInMonth; i++) {
                                days.get(i).add(event);
                            }
                        } else if (event.getStart().isBefore(firstOfMonth)) {
                            for (int i = 0; i < event.getEnd().getDayOfMonth(); i++) {
                                days.get(i).add(event);
                            }
                        } else if (event.getEnd().isAfter(lastOfMonth)) {
                            for (int i = event.getStart().getDayOfMonth() - 1; i < lastOfMonth.getDayOfMonth(); i++) {
                                days.get(i).add(event);
                            }
                        } else {
                            for (int i = event.getStart().getDayOfMonth() - 1; i < event.getEnd().getDayOfMonth(); i++) {
                                days.get(i).add(event);
                            }
                        }
                    }
                }
            });
        });
        return calendar;
    }
}
