package org.blockchain;

import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static org.junit.jupiter.api.Assertions.*;

public class BlockchianTest {
    @Test
    public void creation() {
        Blockchian blockchian = null;
        blockchian = new Blockchian();
        assertNotNull(blockchian);
    }

    @Test
    public void addBlock() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Blockchian blockchian = null;
        blockchian = new Blockchian();
        assertNotNull(blockchian);

        Block block = BlockTest.createBlock(0, blockchian.getNext_zeros());
        assertNotNull(block);
        assertFalse(block.verify());
        assertFalse(blockchian.addBlock(block));
        while (!block.verify()) {
            block.setNonce(block.getNonce() + 1);
        }
        assertTrue(blockchian.addBlock(block));
    }
}