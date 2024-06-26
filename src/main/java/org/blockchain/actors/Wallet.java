package org.blockchain.actors;

import org.blockchain.blockchain.Block;
import org.blockchain.blockchain.Blockchain;
import org.blockchain.events.Transaction;
import org.blockchain.events.Event;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Objects;

public class Wallet {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private Blockchain personalBlockchain;

    public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "SunRsaSign");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        KeyPair pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
        personalBlockchain = new Blockchain();
    }

    public Blockchain getPersonalBlockchain() {
        return personalBlockchain;
    }

    private void setPersonalBlockchain(Blockchain personalBlockchain) {
        this.personalBlockchain = personalBlockchain;
    }

    public void pullFromCore(Core core) {
        if (getPersonalBlockchain().getUpdated().before(core.getBlockchain().getUpdated()) || getPersonalBlockchain().getBlocks().isEmpty()) {
            setPersonalBlockchain(core.getBlockchain().clone());
        }
    }

    public void pullFromCore(Core core, boolean forced) {
        if (forced) {
            setPersonalBlockchain(core.getBlockchain().clone());
        } else {
            pullFromCore(core);
        }
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

    public Block mine() {
        ArrayList<Event> pending = personalBlockchain.getPending();
        ArrayList<Event> content = new ArrayList<>() {};
        for (int i = 0; i < Math.min(pending.size(), personalBlockchain.getMax_block_events()); i++) {
            content.add(pending.get(i));
        }

        Block block = new Block(0, personalBlockchain.getNext_zeros(), content, this);
        if (!block.verifyTransactions()) {
            throw new RuntimeException("Invalid transaction inside the pending events");
        }
        while (!block.verifyHash()) {
            block.setNonce(block.getNonce() + 1);
        }
        return block;
    }

    public boolean mineOnBlockchain() {
        Block block = mine();
        return personalBlockchain.addBlock(block);
    }

    @Override
    public String toString() {
        return String.valueOf(getPublicKey().hashCode());
    }
}
