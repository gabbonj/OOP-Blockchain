package org.events;

import org.actors.Wallet;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreationTest {
    @Test
    public void creationTest() {
        Wallet w = null;
        try {
            w = new Wallet();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(w);
        Creation c = new Creation(new Date(System.currentTimeMillis()), w);
    }
}