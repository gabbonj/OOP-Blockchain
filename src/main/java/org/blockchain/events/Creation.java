package org.blockchain.events;

import org.blockchain.actors.Wallet;

import java.util.Date;
import java.util.Objects;

public class Creation extends Event {
    private final Wallet created;

    public Creation(Wallet created) {
        super();
        this.created = created;
    }

    public Creation(Date date, Wallet created) {
        super(date);
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Creation creation)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getCreated(), creation.getCreated());
    }

    @Override
    public String toString() {
        return "Creation{" +
                "created=" + created.toString() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCreated());
    }

    @Override
    public Creation clone() {
        return new Creation(new Date(getDate().getTime()), getCreated());
    }

    public Wallet getCreated() {
        return created;
    }
}
