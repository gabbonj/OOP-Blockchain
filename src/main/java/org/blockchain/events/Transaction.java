package org.blockchain.events;

import org.blockchain.actors.Wallet;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Transaction extends Event {
    private final Wallet to;
    private final float amount;
    private final byte[] signture;
    private final Wallet from;

    public Transaction(Wallet from, Wallet to, float amount, byte[] signature) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        super();
        if (!verifyTransaction(from, to, amount, signature)) {
            throw new SignatureException("Invalid Signature");
        }

        this.from = from;
        this.to = to;
        this.amount = amount;
        this.signture = signature;
    }

    public Transaction(Date date, Wallet from, Wallet to, float amount, byte[] signature) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        super(date);
        if (!verifyTransaction(from, to, amount, signature)) {
            throw new SignatureException("Invalid Signature");
        }
        this.to = to;
        this.amount = amount;
        this.signture = signature;
        this.from = from;
    }

    public static boolean verifyTransaction(Wallet from, Wallet to, float amount, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(from.getPublicKey());
        String message = Float.toString(amount) + to.getPublicKey();
        verifier.update(message.getBytes(StandardCharsets.UTF_8));

        return verifier.verify(signature);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Float.compare(getAmount(), that.getAmount()) == 0 && Objects.equals(getTo(), that.getTo()) && Arrays.equals(getSignture(), that.getSignture()) && Objects.equals(getFrom(), that.getFrom());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getTo(), getAmount(), getFrom());
        result = 31 * result + Arrays.hashCode(getSignture());
        return result;
    }

    @Override
    public Transaction clone() {
        Transaction transaction;
        try {
            transaction = new Transaction(new Date(getDate().getTime()), getFrom(), getTo(), getAmount(), getSignture());
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return transaction;
    }

    public Wallet getFrom() {
        return from;
    }

    public Wallet getTo() {
        return to;
    }

    public float getAmount() {
        return amount;
    }

    public byte[] getSignture() {
        return signture;
    }

    public boolean verify() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return verifyTransaction(from, to, amount, signture);
    }
}
