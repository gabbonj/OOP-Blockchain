package org.blockchain.events;

import org.blockchain.actors.Wallet;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {

    @Test
    public void testClone() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        Wallet from = new Wallet();
        Wallet to = new Wallet();
        Transaction transaction = from.createTransaction(to, 10);
        Transaction clone = transaction.clone();
        assertEquals(transaction, clone);
    }
}