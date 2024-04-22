package org.actors;

import org.blockchain.Blockchain;

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

    public Set<Wallet> getWallets() {
        return wallets;
    }
}
