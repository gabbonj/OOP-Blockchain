package org.blockchain;

import org.actors.Wallet;
import org.events.Event;
import org.events.Transaction;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Objects;

public class Block {
    private final int zeros;
    private final ArrayList<Event> events;
    private final Wallet miner;
    private int nonce;

    public Block(int nonce, int zeros, ArrayList<Event> events, Wallet miner) {
        this.nonce = nonce;
        this.zeros = zeros;
        this.events = events;
        this.miner = miner;
    }

    public static boolean verifyBlockHash(Block block) {
        return String.valueOf(block.hashCode()).endsWith("0".repeat(block.getZeros()));
    }

    public static boolean verifyBlockTransactions(Block block) {
        for (Event e : block.getEvents()) {
            if (e instanceof Transaction) {
                try {
                    if (!((Transaction) e).verify()) {
                        return false;
                    }
                } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return true;
    }

    public static boolean verifyBlock(Block block) {
        return verifyBlockHash(block) && verifyBlockTransactions(block);
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public int getZeros() {
        return zeros;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block block)) return false;
        return getZeros() == block.getZeros() && getNonce() == block.getNonce() && Objects.equals(getEvents(), block.getEvents()) && Objects.equals(getMiner(), block.getMiner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getZeros(), getEvents(), getMiner(), getNonce());
    }

    @Override
    protected Block clone() {
        ArrayList<Event> eventsCopy = new ArrayList<>();
        for (Event event : getEvents()) {
            eventsCopy.add(event.clone());
        }
        return new Block(getNonce(), getZeros(), eventsCopy, getMiner());
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public Wallet getMiner() {
        return miner;
    }

    public boolean verifyHash() {
        return verifyBlockHash(this);
    }

    public boolean verifyTransactions() {
        return verifyBlockTransactions(this);
    }

    public boolean verify() {
        return verifyBlock(this);
    }
}
