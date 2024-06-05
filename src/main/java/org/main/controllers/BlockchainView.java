package org.main.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML
    private Button CreateWalletButton;
    @FXML
    private ListView<String> PendingList;

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

    private void updatePending() {
        PendingList.setItems(FXCollections.observableArrayList(
                blockchain.getPending().stream()
                        .filter(Objects::nonNull)
                        .map(Event::toString)
                        .collect(Collectors.toList())
        ));
    }

    private void update() {
        updateWallets();
        updatePending();
    }

    private void createWallet() throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, InvalidKeyException {
        Wallet w = new Wallet();
        Creation c = new Creation(w);
        blockchain.addPending(c);
        update();
    }

    public void initialize() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        CreateWalletButton.setOnAction(actionEvent -> {
            try {
                createWallet();
            } catch (NoSuchAlgorithmException | NoSuchProviderException | SignatureException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });

        createGenesis();
        update();
    }
}
