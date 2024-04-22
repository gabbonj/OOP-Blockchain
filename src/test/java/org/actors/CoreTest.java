package org.actors;

import org.blockchain.Block;
import org.blockchain.Blockchain;
import org.blockchain.BlockchainTest;
import org.events.Creation;
import org.events.Event;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CoreTest {
    public static Core creteCore() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Blockchain blockchain = BlockchainTest.createBlockChain();
        Core core = null;
        core = new Core(blockchain);
        assertNotNull(core);
        return core;
    }

    public static Core creteCoreUpdated() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Core core = creteCore();
        for (Wallet wallet : core.getWallets()) {
            wallet.pullFromCore(core);
        }
        return core;
    }

    @Test
    public void creation() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Core core = creteCore();
    }

    @Test
    public void updateWallets() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Core core = creteCore();
        core.updateWallets();
        assertEquals(core.getBlockchain().activeWallets(), core.getWallets());
    }

    @Test
    void addPending() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Wallet w = WalletTest.createWallet();
        assertNotNull(w);
        Core core = CoreTest.creteCore();
        w.pullFromCore(core);

        Set<Wallet> wallets = core.getWallets();
        Creation c = new Creation(w);
        assertTrue(core.addPending(c));
        assertTrue(core.addPending(w.createTransaction(wallets.iterator().next(), 10)));
    }

    @Test
    public void addMinedBlock() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Core core = CoreTest.creteCore();

        Wallet w = WalletTest.createWallet();
        assertNotNull(w);
        assertNotEquals(core.getBlockchain(), w.getPersonalBlockchain());
        w.pullFromCore(core);
        assertEquals(core.getBlockchain(), w.getPersonalBlockchain());

        Creation c = new Creation(w);
        assertTrue(core.addPending(c));
        Block mined = w.mine();
        core.addMinedBlock(mined);
        for (Wallet wallet : core.getWallets()) {
            Block lastBlock = wallet.getPersonalBlockchain().getBlocks().getLast();
            Event lastEvent = lastBlock.getEvents().getLast();
            assertInstanceOf(Creation.class, lastEvent);
            assertEquals(w, ((Creation) lastEvent).getCreated());
        }
    }

    @Test
    public void checkTrust() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Core core = CoreTest.creteCoreUpdated();
        assertTrue(core.checkTrust());
        Core wrongCore = CoreTest.creteCore();
        assertFalse(wrongCore.checkTrust());
    }
}