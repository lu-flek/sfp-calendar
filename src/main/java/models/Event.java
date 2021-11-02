package models;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Event {
    private int id;
    private String name;
    private EventType type;
    private LocalDate start;
    private LocalDate end;
    private boolean isPublic;
    private Set<User> users = new HashSet<>();

    public Event(int id, String name, EventType type, LocalDate start, LocalDate end, boolean isPublic) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.start = start;
        this.end = end;
        this.isPublic = isPublic;

        if (start.isAfter(end)) throw new IllegalArgumentException("The entered date is invalid");
    }

    public Event(String name, EventType type, LocalDate start, LocalDate end, boolean isPublic) {
        this.name = name;
        this.type = type;
        this.start = start;
        this.end = end;
        this.isPublic = isPublic;

        if (start.isAfter(end)) throw new IllegalArgumentException("The entered date is invalid");
    }

    public boolean isOneDayEvent() {
        return start.equals(end);
    }
}
