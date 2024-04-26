package org.events;

import java.util.Date;
import java.util.Objects;

public class Event {
    private final Date date;

    public Event() {
        date = new Date();
    }

    public Event(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return Objects.equals(getDate(), event.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate());
    }

    @Override
    public Event clone() {
        return new Event(new Date(getDate().getTime()));
    }
}
