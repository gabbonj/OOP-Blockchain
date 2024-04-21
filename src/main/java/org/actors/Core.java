package org.actors;

import org.blockchain.Blockchain;

import java.util.ArrayList;
import java.util.Set;

public class Core {
    private Blockchain blockchain;
    private Set<Wallet> wallets;

    public Core(Blockchain blockchain) {
        this.blockchain = blockchain;
    }
}
