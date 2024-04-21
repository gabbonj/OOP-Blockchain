package org.blockchain;

import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static org.junit.jupiter.api.Assertions.*;

public class BlockchainTest {
    @Test
    public void creation() {
        Blockchain blockchain = null;
        blockchain = new Blockchain();
        assertNotNull(blockchain);
    }

    @Test
    public void addBlock() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Blockchain blockchain = null;
        blockchain = new Blockchain();
        assertNotNull(blockchain);

        Block block = BlockTest.createBlock(0, blockchain.getNext_zeros());
        assertNotNull(block);
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

        assertEquals(8, blockchain.activeWallets().size());
    }
}