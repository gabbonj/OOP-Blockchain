package org.blockchain.blockchain;

import org.blockchain.actors.Core;
import org.blockchain.actors.CoreTest;
import org.blockchain.actors.Wallet;
import org.blockchain.actors.WalletTest;
import org.blockchain.events.Creation;
import org.blockchain.events.Event;
import org.blockchain.events.Transaction;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
        for (Event event : block1.getEvents()) {
            blockchain.addPending(event);
        }
        while (!block1.verify()) {
            block1.setNonce(block1.getNonce() + 1);
        }
        assertTrue(blockchain.addBlock(block1));

        Block block2 = BlockTest.createBlock(0, blockchain.getNext_zeros());
        assertNotNull(block2);
        for (Event event : block2.getEvents()) {
            blockchain.addPending(event);
        }
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
        for (Event event : block.getEvents()) {
            blockchain.addPending(event);
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
    public void walletBalance() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        Core core = CoreTest.creteCoreUpdated();
        core.updateWallets();
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        Creation creation1 = new Creation(wallet1);
        Creation creation2 = new Creation(wallet2);
        Transaction transaction = wallet1.createTransaction(wallet2, 30);
        core.addPending(creation1);
        core.addPending(creation2);
        core.addPending(transaction);
        wallet1.pullFromCore(core);
        Block mined = wallet1.mine();
        core.addMinedBlock(mined);
        core.updateWallets();
        Map<Wallet, Float> balance = core.getBlockchain().balances();
        float balace1 = core.getBlockchain().walletBalance(wallet1);
        float balace2 = core.getBlockchain().walletBalance(wallet2);
        assertEquals(0, core.getBlockchain().getPending().size());
        assertEquals(20, balace1);
        assertEquals(30, balace2);
    }
}