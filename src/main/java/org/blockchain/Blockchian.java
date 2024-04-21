package org.blockchain;

import org.events.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class Blockchian {
    private final int max_tokens = 1000000;
    private final int max_block_events = 15;
    int next_zeros;
    private final LinkedList<Block> blocks;
    private final ArrayList<Event> pending;
    private Date updated;

    public Blockchian() {
        RandomGenerator generator = RandomGenerator.getDefault();
        next_zeros = generator.nextInt(2, 6);
        blocks = new LinkedList<Block>();
        pending = new ArrayList<Event>();
        updated = new Date();
    }

    public static boolean verifyBlockchain(Blockchian blockchian) {
        for (Block b : blockchian.getBlocks()) {
            if (!b.verify()) {
                return false;
            }
        }
        return true;
    }

    public LinkedList<Block> getBlocks() {
        return blocks;
    }

    public ArrayList<Event> getPending() {
        return pending;
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

    public int getMax_tokens() {
        return max_tokens;
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
        if (!(o instanceof Blockchian that)) return false;
        return getMax_tokens() == that.getMax_tokens() && getMax_block_events() == that.getMax_block_events() && getNext_zeros() == that.getNext_zeros() && Objects.equals(getBlocks(), that.getBlocks()) && Objects.equals(getPending(), that.getPending()) && Objects.equals(getUpdated(), that.getUpdated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMax_tokens(), getMax_block_events(), getNext_zeros(), getBlocks(), getPending(), getUpdated());
    }

    public boolean addBlock(Block block) {
        if (!block.verify() || block.getEvents().size() > getMax_block_events()) {
            return false;
        }
        setUpdated(new Date());
        RandomGenerator generator = RandomGenerator.getDefault();
        next_zeros = generator.nextInt(2, 6);
        return true;
    }
}
