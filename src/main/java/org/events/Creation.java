package org.events;

import org.actors.Wallet;

import java.security.Signature;
import java.util.Date;
import java.util.Objects;

public class Creation extends Event{
    private final Wallet created;

    public Creation(Date date, Wallet created) {
        super(date);
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Creation creation)) return false;
        return Objects.equals(getCreated(), creation.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCreated());
    }

    public Wallet getCreated() {
        return created;
    }
}
