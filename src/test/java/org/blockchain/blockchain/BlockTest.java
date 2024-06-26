package org.blockchain.blockchain;

import org.blockchain.actors.Wallet;
import org.blockchain.actors.WalletTest;
import org.blockchain.events.Creation;
import org.blockchain.events.Event;
import org.blockchain.events.Transaction;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {
    public static Block createBlock(int nonce, int zeros) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet w1 = WalletTest.createWallet();
        Wallet w2 = WalletTest.createWallet();
        Wallet w3 = WalletTest.createWallet();
        Wallet w4 = WalletTest.createWallet();

        Creation c1 = new Creation(w1);
        Creation c2 = new Creation(w2);
        Creation c3 = new Creation(w3);
        Creation c4 = new Creation(w4);
        Transaction t1 = w1.createTransaction(w4, 10f);
        Transaction t2 = w2.createTransaction(w3, 5f);
        Transaction t3 = w3.createTransaction(w2, 1f);
        Transaction t4 = w4.createTransaction(w1, 15f);

        ArrayList<Event> e = new ArrayList<Event>() {{
            add(c1);
            add(c2);
            add(c3);
            add(c4);
            add(t1);
            add(t2);
            add(t3);
            add(t4);
        }};

        return new Block(0, zeros, e, w1);
    }

    @Test
    void creation() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Block block = createBlock(0, 10);
    }

    @Test
    void verifyHash() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Block block = createBlock(0, 3);
        while (!block.verifyHash()) {
            block.setNonce(block.getNonce() + 1);
        }
        assertTrue(block.verifyHash());
    }

    @Test
    void verifyTransactions() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Block block = createBlock(0, 3);
        assertTrue(block.verifyTransactions());
    }

    @Test
    void verify() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Block block = createBlock(0, 3);
        assertFalse(block.verify());
        while (!block.verify()) {
            block.setNonce(block.getNonce() + 1);
        }
    }

    @Test
    public void testClone() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Block block = createBlock(0, 3);
        Block clone = block.clone();
        assertEquals(block, clone);
    }
}