package org.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    @Test
    public void testClone() throws CloneNotSupportedException {
        Event event = new Event();
        Event clone = event.clone();
        assertEquals(event, clone);
    }
}