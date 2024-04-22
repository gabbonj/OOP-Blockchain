package org.actors;

import org.blockchain.Blockchain;
import org.blockchain.BlockchainTest;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static org.junit.jupiter.api.Assertions.*;

class CoreTest {
    public static Core creteCore() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Blockchain blockchain = BlockchainTest.createBlockChain();
        Core core = null;
        core = new Core(blockchain);
        assertNotNull(core);
        return core;
    }

    @Test
    public void creation() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Core core = creteCore();
    }
    @Test
    public void updateWallets() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Blockchain blockchain = BlockchainTest.createBlockChain();
        Core core = null;
        core = new Core(blockchain);
        assertNotNull(core);

        core.updateWallets();
        assertEquals(core.getBlockchain().activeWallets(), core.getWallets());
    }
}