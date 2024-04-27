package org.blockchain.events;

import org.blockchain.actors.Wallet;
import org.blockchain.actors.WalletTest;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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