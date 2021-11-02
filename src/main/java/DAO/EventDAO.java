package DAO;

import models.Event;
import models.EventType;
import models.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static DAO.DBUtils.getConnection;

public class EventDAO {

    public Event createEvent(Event event) {
        String creating = "INSERT INTO EVENTS (name, type, date_start, date_end, is_public) VALUES(?,?,?,?,?)";
        Event newEvent = null;
        try (PreparedStatement stat = getConnection().prepareStatement(creating)) {
            stat.setString(1, event.getName());
            stat.setObject(2, event.getType(), Types.OTHER); // enum
            stat.setDate(3, Date.valueOf(event.getStart()));
            stat.setDate(4, Date.valueOf(event.getEnd()));
            stat.setBoolean(5, event.isPublic());

            stat.executeUpdate();
            newEvent = findEventByName(event.getName()).get();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return newEvent;
    }

    public void deleteEvent(Event event) {
        String deletion = "DELETE FROM EVENTS WHERE id=?";

        try (PreparedStatement stat = getConnection().prepareStatement(deletion)) {
            stat.setInt(1, event.getId());
            stat.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public Optional<Event> findEventByID(int id) {
        String selection = "SELECT id, name, type, date_start, date_end, is_public FROM EVENTS WHERE id=?";
        Optional<Event> event = Optional.empty();

        try (PreparedStatement stat = getConnection().prepareStatement(selection)) {
            stat.setInt(1, id);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                event = Optional.of(createEventByResultSet(rs));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return event;
    }

    public Optional<Event> findEventByName(String name) {
        String selection = "SELECT id, name, type, date_start, date_end, is_public FROM EVENTS WHERE name=?";
        Optional<Event> event = Optional.empty();

        try (PreparedStatement stat = getConnection().prepareStatement(selection)) {
            stat.setString(1, name);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                event = Optional.of(createEventByResultSet(rs));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return event;
    }

    public List<Event> getEventsByMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);

        List<Event> events = new ArrayList<>();

        String query = "SELECT id, name, type, date_start, date_end, is_public FROM EVENTS " +
                "WHERE (date_start BETWEEN ? and ?) OR (date_end BETWEEN ? and ?)";

        try (PreparedStatement stat = getConnection().prepareStatement(query)) {
            stat.setDate(1, Date.valueOf(start));
            stat.setDate(2, Date.valueOf(end));
            stat.setDate(3, Date.valueOf(start));
            stat.setDate(4, Date.valueOf(end));

            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                events.add(createEventByResultSet(rs));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return events;
    }

    public List<Event> getPublicEvents() {
        List<Event> events = new ArrayList<>();

        String query = "SELECT id, name, type, date_start, date_end, is_public FROM EVENTS WHERE is_public=?";

        try (PreparedStatement stat = getConnection().prepareStatement(query)) {
            stat.setBoolean(1, true);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                events.add(createEventByResultSet(rs));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return events;
    }

    public List<Event> getAllEventsOfUserByUserId(int userId) {
        String selection = "SELECT event_id FROM USERS_EVENTS WHERE user_id=?";
        List<Integer> idOfEvents = new ArrayList<>();

        try (PreparedStatement stat = getConnection().prepareStatement(selection)) {
            stat.setInt(1, userId);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                idOfEvents.add(rs.getInt("event_id"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        List<Event> events = new ArrayList<>();
        idOfEvents.forEach(id -> findEventByID(id).ifPresent(events::add));
        return events;
    }

    public void createRow(User user, Event event) {
        String creating = "INSERT INTO users_events (user_id, event_id) VALUES(?,?)";

        try (PreparedStatement stat = getConnection().prepareStatement(creating)) {
            stat.setInt(1, user.getId());
            stat.setInt(2, event.getId());

            stat.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public Event createEventByResultSet(ResultSet rs) throws SQLException {
        return new Event(
                rs.getInt("id"),
                rs.getString("name"),
                Enum.valueOf(EventType.class, rs.getString("type")),
                rs.getDate("date_start").toLocalDate(),
                rs.getDate("date_end").toLocalDate(),
                rs.getBoolean("is_public")
        );
    }
}
