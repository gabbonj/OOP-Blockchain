package org.actors;

import org.events.Creation;
import org.events.Transaction;
import org.junit.Test;

import java.security.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


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
            giovanni = new Wallet();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(pino);
        assertNotNull(giovanni);

        try {
            Transaction t = pino.createTransaction(giovanni, 10);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Transaction created successfully");
    }

    @Test
    public void pullFromCore() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet w = null;
        try {
            w = new Wallet();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(w);
        Core core = CoreTest.creteCore();
        w.pullFromCore(core);
    }

    @Test
    public void mine() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet w = null;
        try {
            w = new Wallet();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(w);
        Core core = CoreTest.creteCore();
        w.pullFromCore(core);

        Set<Wallet> wallets = core.getWallets();
        Creation c = new Creation(w);
        assertTrue(core.addPending(c));
        assertTrue(core.addPending(w.createTransaction(wallets.iterator().next(), 10)));

        assertTrue(w.mine().verify());
    }

    @Test
    public void mineOnBlockchain() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet w = null;
        try {
            w = new Wallet();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(w);
        Core core = CoreTest.creteCore();
        w.pullFromCore(core);

        Set<Wallet> wallets = core.getWallets();
        Creation c = new Creation(w);
        assertTrue(core.addPending(c));
        assertTrue(core.addPending(w.createTransaction(wallets.iterator().next(), 10)));

        assertTrue(w.mineOnBlockchain());
    }
}