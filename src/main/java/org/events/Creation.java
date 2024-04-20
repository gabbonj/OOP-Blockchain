package org.events;

import org.actors.Wallet;

import java.security.Signature;
import java.util.Date;

public class Creation extends Event{
    Wallet created;

    public Creation(Date date, Wallet created) {
        super(date);
        this.created = created;
    }
}
