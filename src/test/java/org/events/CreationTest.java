package org.events;

import org.actors.Wallet;
import org.actors.WalletTest;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreationTest {
    @Test
    public void creationTest() {
        Wallet w = WalletTest.createWallet();
        Creation c = new Creation(w);
        assertEquals(w, c.getCreated());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Wallet wallet = WalletTest.createWallet();
        Creation creation = new Creation(wallet);
        Creation clone = creation.clone();
        assertEquals(creation, clone);
    }
}