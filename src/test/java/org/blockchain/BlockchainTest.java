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
    }
}