package org.main.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.blockchain.actors.Core;
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
    private Core core;
    private Wallet myWallet;

    @FXML
    private ListView<String> WalletsList;
    @FXML
    private Button CreateWalletButton;
    @FXML
    private ListView<String> PendingList;
    @FXML
    private Button UpdateButton;
    @FXML
    private ListView<String>BlocksList;
    @FXML
    private Button MineButton;

    private void createGenesis() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        Blockchain blockchain = new Blockchain();

        myWallet = new Wallet();
        Creation c =  new Creation(myWallet);
        ArrayList<Event> e = new ArrayList<>() {{
            add(c);
        }};
        Block genesis = new Block(0, blockchain.getNext_zeros(), e, myWallet);
        for (Event event : genesis.getEvents()) {
            blockchain.addPending(event);
        }
        while (!genesis.verify()) {
            genesis.setNonce(genesis.getNonce() + 1);
        }
        blockchain.addBlock(genesis);

        core = new Core(blockchain);
    }

    private void updateWallets() {
        WalletsList.setItems(FXCollections.observableArrayList(
                core.getBlockchain().activeWallets().stream()
                        .filter(Objects::nonNull)
                        .map(wallet -> {
                            if (wallet == myWallet) {
                                return "{ " + wallet.toString() + " }";
                            } else {
                                return wallet.toString();
                            }
                        })
                        .collect(Collectors.toList())
        ));
    }

    private void updatePending() {
        PendingList.setItems(FXCollections.observableArrayList(
                core.getBlockchain().getPending().stream()
                        .filter(Objects::nonNull)
                        .map(Event::toString)
                        .collect(Collectors.toList())
        ));
    }

    private void updadateBlocks() {
        BlocksList.setItems(FXCollections.observableArrayList(
            core.getBlockchain().getBlocks().stream()
                    .filter(Objects::nonNull)
                    .map(Block::toString)
                    .collect(Collectors.toList())
        ));
    }

    private void update() {
        updateWallets();
        updatePending();
        updadateBlocks();
    }

    private void createWallet() throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, InvalidKeyException {
        Wallet w = new Wallet();
        Creation c = new Creation(w);
        core.addPending(c);
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

        UpdateButton.setOnAction(actionEvent -> update());

        MineButton.setOnAction(actionEvent -> {
            if (!core.getBlockchain().getPending().isEmpty()) {
                myWallet.pullFromCore(core);
                myWallet.mineOnBlockchain();
                core.addMinedBlock(myWallet.getPersonalBlockchain().getBlocks().getLast());
                update();
            }
        });
        createGenesis();
        update();
    }
}
