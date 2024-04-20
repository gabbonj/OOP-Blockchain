package org.events;

import org.actors.Wallet;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class Transaction {
    Wallet from;
    Wallet to;
    float amount;
    byte[] signture;

    public Transaction(Wallet from, Wallet to, float amount, byte[] signature) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(from.getPublicKey());
        verifier.update(Float.toString(amount).getBytes(StandardCharsets.UTF_8));

        if (!verifier.verify(signature)) {
            throw new SignatureException("Invalid Signature");
        }

        this.from = from;
        this.to = to;
        this.amount = amount;
        this.signture = signature;
    }
}
