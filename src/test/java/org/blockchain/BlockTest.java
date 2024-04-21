package org.blockchain;

import org.actors.Wallet;
import org.events.Creation;
import org.events.Event;
import org.events.Transaction;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {
    public static Block createBlock(int nonce, int zeros) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet w1 = null;
        Wallet w2 = null;
        Wallet w3 = null;
        Wallet w4 = null;
        try {
            w1 = new Wallet();
            w2 = new Wallet();
            w3 = new Wallet();
            w4 = new Wallet();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(w1);
        assertNotNull(w2);
        assertNotNull(w3);
        assertNotNull(w4);

        Date now = new Date();

        Creation c1 = new Creation(now, w1);
        Creation c2 = new Creation(now, w2);
        Creation c3 = new Creation(now, w3);
        Creation c4 = new Creation(now, w4);
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
    void creation() {
        try {
            Block block = createBlock(0, 10);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void verifyHash() {
        Block block = null;
        try {
            block = createBlock(0, 3);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(block);
        assertFalse(block.verifyHash());
        while (!block.verifyHash()) {
            block.setNonce(block.getNonce() + 1);
        }
        System.out.println("Hash verified");
    }

    @Test
    void verifyTransactions() {
        Block block = null;
        try {
            block = createBlock(0, 3);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(block);
        assertTrue(block.verifyTransactions());
    }

    @Test
    void verify() {
        Block block = null;
        try {
            block = createBlock(0, 3);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(block);
        assertFalse(block.verify());
        while (!block.verify()) {
            block.setNonce(block.getNonce() + 1);
        }
    }
}