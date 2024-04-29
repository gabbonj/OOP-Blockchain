package org.blockchain.actors;

import org.blockchain.blockchain.Block;
import org.blockchain.blockchain.Blockchain;
import org.blockchain.events.Event;

import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class Core {
    private Blockchain blockchain;
    private Set<Wallet> wallets;
    public Core(Blockchain blockchain) {
        this.blockchain = blockchain;
        wallets = blockchain.activeWallets();
    }

    public void updateWallets() {
        wallets = blockchain.activeWallets();
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    private void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public Set<Wallet> getWallets() {
        return wallets;
    }

    public boolean addPending(Event e) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return getBlockchain().addPending(e);
    }

    public void addMinedBlock(Block block) {
        if (!getBlockchain().addBlock(block)) {
            return;
        }
        updateWallets();
        for (Wallet wallet : wallets) {
            wallet.getPersonalBlockchain().addBlock(block);
        }
    }

    public boolean checkTrust() {
        updateWallets();
        int trust = 0;
        for (Wallet wallet : wallets) {
            if (getBlockchain().equals(wallet.getPersonalBlockchain())) {
                trust++;
            }
        }
        return trust > (getWallets().size() / 2);
    }

    public Blockchain mostTrusted() {
        updateWallets();
        Optional<Wallet> maxWallet = Optional.empty();
        int maxTrust = 0;
        int trust;
        for (Wallet trusted : getWallets()) {
            trust = 0;
            for (Wallet truster : getWallets()) {
                if (trusted != truster && trusted.getPersonalBlockchain().equals(truster.getPersonalBlockchain())) {
                    trust++;
                }
            }
            if (trust > maxTrust) {
                maxTrust = trust;
                maxWallet = Optional.of(trusted);
            }
        }
        if (maxWallet.isPresent()) {
            return maxWallet.get().getPersonalBlockchain();
        } else {
            return getBlockchain();
        }
    }

    public void pullMostTrusted() {
        updateWallets();
        if (!checkTrust()) {
            setBlockchain(mostTrusted());
        }
    }
}
