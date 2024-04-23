package org.events;

import java.util.Date;

public class Event {
    private Date date;

    public Date getDate() {
        return date;
    }

    public Event() {
        date = new Date();
    }
}
