package org.main.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.blockchain.actors.Wallet;
import org.blockchain.blockchain.Block;
import org.blockchain.blockchain.Blockchain;
import org.blockchain.events.Creation;
import org.blockchain.events.Event;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlockchainView {
    public Blockchain blockchain;

    @FXML
    private ListView<String> WalletsList;

    private void createGenesis() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        blockchain = new Blockchain();

        Wallet w1 = new Wallet();
        Creation c=  new Creation(w1);
        ArrayList<Event> e = new ArrayList<>() {{
            add(c);
        }};
        Block genesis = new Block(0, blockchain.getNext_zeros(), e, w1);
        for (Event event : genesis.getEvents()) {
            blockchain.addPending(event);
        }
        while (!genesis.verify()) {
            genesis.setNonce(genesis.getNonce() + 1);
        }
        blockchain.addBlock(genesis);
    }

    private void updateWallets() {
        WalletsList.setItems(FXCollections.observableArrayList(
                blockchain.activeWallets().stream()
                        .filter(Objects::nonNull)
                        .map(Wallet::toString)
                        .collect(Collectors.toList())
        ));
    }

    private void update() {
        updateWallets();
    }

    public void initialize() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        createGenesis();
        update();
    }
}
