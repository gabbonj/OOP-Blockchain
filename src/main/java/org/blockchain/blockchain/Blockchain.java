package org.blockchain.blockchain;

import org.blockchain.actors.Wallet;
import org.blockchain.events.Creation;
import org.blockchain.events.Event;
import org.blockchain.events.Transaction;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

public class Blockchain {
    final float blockPerEra = 200f;
    final float rewardFirstEra = 50f;
    final float halvingFactor = 2f;
    private final int max_block_events = 15;
    int next_zeros;
    private LinkedList<Block> blocks;
    private ArrayList<Event> pending;
    private Date updated;

    public Blockchain() {
        RandomGenerator generator = RandomGenerator.getDefault();
        next_zeros = generator.nextInt(2, 6);
        blocks = new LinkedList<>();
        pending = new ArrayList<>();
        updated = new Date();
    }

    public static boolean verifyBlockchain(Blockchain blockchain) {
        for (Block b : blockchain.getBlocks()) {
            if (!b.verify()) {
                return false;
            }
        }
        return true;
    }

    public static Set<Wallet> activeBlockchainWallets(Blockchain blockchain) {
        Set<Wallet> wallets = new HashSet<>();
        for (Block block : blockchain.getBlocks()) {
            for (Event e : block.getEvents()) {
                if (e instanceof Creation creation) {
                    wallets.add((creation.getCreated()));
                }
            }
        }
        return wallets;
    }

    public LinkedList<Block> getBlocks() {
        return blocks;
    }

    private void setBlocks(LinkedList<Block> blocks) {
        this.blocks = blocks;
    }

    public ArrayList<Event> getPending() {
        return pending;
    }

    private void setPending(ArrayList<Event> pending) {
        this.pending = pending;
    }

    public Date getUpdated() {
        return updated;
    }

    private void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int getNext_zeros() {
        return next_zeros;
    }

    private void setNext_zeros(int next_zeros) {
        this.next_zeros = next_zeros;
    }

    public int getMax_block_events() {
        return max_block_events;
    }

    public boolean verify() {
        return verifyBlockchain(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Blockchain that)) return false;
        return getMax_block_events() == that.getMax_block_events() && getNext_zeros() == that.getNext_zeros() && Objects.equals(getBlocks(), that.getBlocks()) && Objects.equals(getPending(), that.getPending()) && Objects.equals(getUpdated(), that.getUpdated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockPerEra, rewardFirstEra, halvingFactor, getMax_block_events(), getNext_zeros(), getBlocks(), getPending(), getUpdated());
    }

    @Override
    public Blockchain clone() {
        Blockchain blockchain = new Blockchain();
        blockchain.setNext_zeros(getNext_zeros());
        blockchain.setUpdated(new Date(getUpdated().getTime()));
        LinkedList<Block> blocksClone = new LinkedList<>();
        for (Block block : getBlocks()) {
            blocksClone.add(block.clone());
        }
        blockchain.setBlocks(blocksClone);
        ArrayList<Event> pendingClone = new ArrayList<>();
        for (Event event : getPending()) {
            pendingClone.add(event.clone());
        }
        blockchain.setPending(pendingClone);
        return blockchain;
    }

    public boolean addBlock(Block block) {
        if (!verify()) {
            return false;
        }
        if (block.getEvents().size() > getMax_block_events()) {
            return false;
        }
        for (Event event : block.getEvents()) {
            if (!getPending().contains(event)) {
                return false;
            }
        }
        for (Event event : block.getEvents()) {
            getPending().remove(event);
        }
        if (!block.verify() || block.getEvents().size() > getMax_block_events()) {
            return false;
        }
        blocks.add(block);
        setUpdated(new Date());
        RandomGenerator generator = RandomGenerator.getDefault();
        next_zeros = generator.nextInt(2, 6);
        return true;
    }

    public Set<Wallet> activeWallets() {
        return activeBlockchainWallets(this);
    }

    public boolean addPending(Event event) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (event instanceof Transaction transaction) {
            if (!transaction.verify()) {
                return false;
            }
        }
        setUpdated(new Date());
        getPending().add(event);
        return true;
    }

    private float reward(int blockIndex) {
        return (float) (rewardFirstEra / Math.pow(halvingFactor, Math.floor(blockIndex / blockPerEra)));
    }

    private static void updateBalance(Map<Wallet, Float> balances, Wallet key, Float change) {
        if (balances.containsKey(key)) {
            balances.put(key, balances.get(key) + change);
        }
    }

    private static void updateBalanceTransaction(Map<Wallet, Float> balances, Transaction transaction) {
        updateBalance(balances, transaction.getFrom(), -transaction.getAmount());
        updateBalance(balances, transaction.getTo(), transaction.getAmount());
    }

    public Map<Wallet, Float> balances() {
        Map<Wallet, Float> balances = activeWallets().stream().collect(Collectors.toMap(wallet -> wallet, wallet -> 0f));
        int index = 1;
        for (Block block : getBlocks()) {
            updateBalance(balances, block.getMiner(), reward(index));
            for (Event event : block.getEvents()) {
                if (event instanceof Transaction transaction && transaction.getAmount() <= balances.get(transaction.getFrom())) {
                    updateBalanceTransaction(balances, transaction);
                }
            }
            index++;
        }
        return balances;
    }

    public float walletBalance(Wallet wallet) {
        return balances().get(wallet);
    }
}
