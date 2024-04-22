package org.actors;

import org.events.Event;
import org.events.Transaction;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Date;
import java.util.Objects;

public class Wallet {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        KeyPair pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wallet wallet)) return false;
        return Objects.equals(privateKey, wallet.privateKey) && Objects.equals(getPublicKey(), wallet.getPublicKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(privateKey, getPublicKey());
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public Transaction createTransaction(Wallet to, float amount) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String message = Float.toString(amount) + to.getPublicKey();
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(bytes);
        byte[] digitalSignature = signature.sign();

        return new Transaction(this, to, amount, digitalSignature);
    }
}
