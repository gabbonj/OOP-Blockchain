package org.main;

import org.blockchain.actors.Wallet;
import org.blockchain.events.Transaction;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, InvalidKeyException {
        Wallet pino = new Wallet();
        Wallet giovanni = new Wallet();
        Transaction t = pino.createTransaction(giovanni, 10);
    }
}