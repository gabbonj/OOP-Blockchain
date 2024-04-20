package org.actors;

import org.events.Transaction;
import org.junit.Test;

import java.security.*;


public class WalletTest {
    @Test
    public void createWallet() {
        try {
            Wallet w = new Wallet();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Wallet created successfully");
    }

    @Test
    public void createTransaction() {
        Wallet pino = null;
        Wallet giovanni = null;
        try {
            pino = new Wallet();
            System.out.println("First wallet created successfully");
            giovanni = new Wallet();
            System.out.println("Second wallet created successfully");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }

        try {
            Transaction t = pino.createTransaction(giovanni, 10);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Transaction created successfully");
    }
}