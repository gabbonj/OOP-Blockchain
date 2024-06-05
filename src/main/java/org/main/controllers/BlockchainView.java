package org.main.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.blockchain.actors.Core;
import org.blockchain.actors.Wallet;
import org.blockchain.blockchain.Block;
import org.blockchain.blockchain.Blockchain;
import org.blockchain.events.Creation;
import org.blockchain.events.Event;
import org.blockchain.events.Transaction;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
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
    @FXML
    private TextField ToField;
    @FXML
    private TextField AmountFileld;
    @FXML
    private Button SendButton;
    @FXML
    private TextField EventNumberFiled;
    @FXML
    private Button SimulateNoMineButton;
    @FXML
    private Button SimulateMineButton;


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

    private String walletFormat(Wallet wallet) {
        StringBuilder stringBuilder = new StringBuilder();

        if (wallet == myWallet) {
            stringBuilder.append("{ ").append(wallet.toString()).append(" }");
        } else {
            stringBuilder.append(wallet.toString());
        }
        stringBuilder.append(" - ").append(core.getBlockchain().walletBalance(wallet));

        return stringBuilder.toString();
    }

    private void updateWallets() {
        WalletsList.setItems(FXCollections.observableArrayList(
                core.getBlockchain().activeWallets().stream()
                        .filter(Objects::nonNull)
                        .map(this::walletFormat)
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

    private int getEventNumber() {
        int number;
        try {
            number = Integer.parseInt(EventNumberFiled.getText());
        } catch (NumberFormatException e) {
            number = 0;
        }

        return number;
    }

    private class CreateWalletButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                createWallet();
            } catch (NoSuchAlgorithmException | NoSuchProviderException | SignatureException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class MineButtonHandler implements  EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (!core.getBlockchain().getPending().isEmpty()) {
                myWallet.pullFromCore(core);
                myWallet.mineOnBlockchain();
                core.addMinedBlock(myWallet.getPersonalBlockchain().getBlocks().getLast());
                update();
            }
        }
    }

    private class SendButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            Wallet to = null;
            for (Wallet wallet : core.getWallets()) {
                if (Objects.equals(wallet.toString(), ToField.getText())) {
                    to = wallet;
                    break;
                }
            }

            if (to == null || AmountFileld.getText().isEmpty()) {
                return;
            }

            float amount;
            try {
                amount = Float.parseFloat(AmountFileld.getText());
            } catch (NumberFormatException e) {
                return;
            }

            Transaction t;
            if (amount < core.getBlockchain().walletBalance(myWallet)) {
                try {
                    t = myWallet.createTransaction(to, amount);
                    core.addPending(t);
                    core.updateWallets();
                } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
                    throw new RuntimeException(e);
                }
            }
            update();
        }
    }

    private Wallet getRandomWallet() {
        Random random = new Random();
        ArrayList<Wallet> wallets = new ArrayList<>(core.getWallets());
        return wallets.get(random.nextInt(wallets.size()));
    }

    private void randomTransactions() {
        int events = getEventNumber();
        Random random = new Random();

        for (int i = 0; i < events; i++) {
            Wallet wallet1;
            Wallet wallet2;
            do {
                wallet1 = getRandomWallet();
                wallet2 = getRandomWallet();
            } while (wallet1.equals(wallet2) || core.getBlockchain().walletBalance(wallet1) == 0);

            Transaction transaction;
            try {
                transaction = wallet1.createTransaction(wallet2, random.nextFloat(core.getBlockchain().walletBalance(wallet1)));
                core.addPending(transaction);
                core.updateWallets();
            } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class SimulateNoMineButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            randomTransactions();
            update();
        }
    }

    private class SimulateMineButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            randomTransactions();
            while (!core.getBlockchain().getPending().isEmpty()) {
                Wallet wallet = getRandomWallet();
                wallet.pullFromCore(core);
                wallet.mineOnBlockchain();
                core.addMinedBlock(wallet.getPersonalBlockchain().getBlocks().getLast());
                update();
            }
            update();
        }
    }

    public void initialize() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        CreateWalletButton.setOnAction(new CreateWalletButtonHandler());
        UpdateButton.setOnAction(actionEvent -> update());
        MineButton.setOnAction(new MineButtonHandler());
        SendButton.setOnAction(new SendButtonHandler());
        SimulateNoMineButton.setOnAction(new SimulateNoMineButtonHandler());
        SimulateMineButton.setOnAction(new SimulateMineButtonHandler());

        createGenesis();
        update();
    }
}
