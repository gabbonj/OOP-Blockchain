package org.blockchain.actors;

import org.blockchain.events.Creation;
import org.blockchain.events.Transaction;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class WalletTest {
    public static Wallet createWallet() {
        Wallet w;
        try {
            w = new Wallet();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(w);
        return w;
    }

    @Test
    public void walletCreation() {
        Wallet w = createWallet();
    }

    @Test
    public void createTransaction() {
        Wallet pino = createWallet();
        Wallet giovanni = createWallet();

        try {
            Transaction t = pino.createTransaction(giovanni, 10);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void pullFromCore() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet w = createWallet();
        Core core = CoreTest.creteCore();
        w.pullFromCore(core);
        assertEquals(w.getPersonalBlockchain().getBlocks(), core.getBlockchain().getBlocks());
    }

    @Test
    public void mine() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet w = createWallet();
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
        Wallet w = createWallet();
        Core core = CoreTest.creteCore();
        w.pullFromCore(core);

        Set<Wallet> wallets = core.getWallets();
        Creation c = new Creation(w);
        assertTrue(core.addPending(c));
        assertTrue(core.addPending(w.createTransaction(wallets.iterator().next(), 10)));

        assertTrue(w.mineOnBlockchain());
    }
}