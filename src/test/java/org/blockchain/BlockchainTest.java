package org.blockchain;

import org.actors.Core;
import org.actors.CoreTest;
import org.actors.Wallet;
import org.actors.WalletTest;
import org.events.Creation;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BlockchainTest {
    static public Blockchain createBlockChain() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Blockchain blockchain = null;
        blockchain = new Blockchain();
        assertNotNull(blockchain);

        Block block1 = BlockTest.createBlock(0, blockchain.getNext_zeros());
        assertNotNull(block1);
        assertFalse(block1.verify());
        assertFalse(blockchain.addBlock(block1));
        while (!block1.verify()) {
            block1.setNonce(block1.getNonce() + 1);
        }
        assertTrue(blockchain.addBlock(block1));

        Block block2 = BlockTest.createBlock(0, blockchain.getNext_zeros());
        assertNotNull(block2);
        assertFalse(block2.verify());
        assertFalse(blockchain.addBlock(block2));
        while (!block2.verify()) {
            block2.setNonce(block2.getNonce() + 1);
        }
        assertTrue(blockchain.addBlock(block2));

        return blockchain;
    }

    public static Blockchain createPendingBlockchain() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Blockchain blockchain = createBlockChain();
        Wallet wallet = WalletTest.createWallet();
        Creation creation = new Creation(wallet);
        blockchain.addPending(creation);
        return blockchain;
    }

    @Test
    public void creation() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Blockchain blockchain = createBlockChain();
    }

    @Test
    public void addBlock() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Blockchain blockchain = null;
        blockchain = new Blockchain();
        assertNotNull(blockchain);

        Block block = BlockTest.createBlock(0, blockchain.getNext_zeros());
        assertFalse(block.verify());
        assertFalse(blockchain.addBlock(block));
        while (!block.verify()) {
            block.setNonce(block.getNonce() + 1);
        }
        assertTrue(blockchain.addBlock(block));
        assertEquals(1, blockchain.getBlocks().size());
    }

    @Test
    public void activeWallets() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Blockchain blockchain = createBlockChain();
        assertEquals(8, blockchain.activeWallets().size());
    }

    @Test
    public void walletBalance() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Core core = CoreTest.creteCoreUpdatedPending();
        core.updateWallets();
        Wallet wallet = core.getWallets().iterator().next();
        Block mined = wallet.mine();
        core.addMinedBlock(mined);
        Map<Wallet, Float> balance = core.getBlockchain().balances();
        float balace = core.getBlockchain().walletBalance(wallet);
        assertEquals(balace, 50);
    }
}