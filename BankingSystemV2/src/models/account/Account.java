package models.account;

import database.Database;
import models.transaction.BankTransaction;
import models.transaction.CardTransaction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class Account implements Serializable {

    private static final long serialVersionUID = 7L;

    private final int accountNumber;
    private double balance;
    private final int bankId;
    private final Map<Integer, GiftCard> giftCards;
    private final Map<Integer, BankTransaction> bankTransactions;
    private final Map<Integer, CardTransaction> cardTransactions;
    private final String accountType;

    public Account(int bankId, double balance, AccountType accountType) {
        if(balance < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.accountNumber = Database.getInstance().getNextAccountNumber();
        this.balance = balance;
        this.bankTransactions = new HashMap<>();
        this.bankId = bankId;
        this.accountType = accountType.toString();
        this.cardTransactions = new HashMap<>();
        this.giftCards = new HashMap<>();
        addBankTransaction("Initial Deposit", balance);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public int getBankId() {
        return bankId;
    }

    protected void addBankTransaction(String description, double amount) {
        BankTransaction bankTransaction = new BankTransaction(description, amount, getBalance());
        bankTransactions.put(bankTransaction.getTransactionId(), bankTransaction);
    }

    public void viewBankTransactions() {
        System.out.printf("%-15s %-15s %-15s %-15s\n", "Transaction", "Description", "Amount", "Balance");
        for(int transactionId: bankTransactions.keySet()) {
            System.out.println(bankTransactions.get(transactionId));
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-15s", accountNumber, balance, accountType);
    }

    abstract public void deposit(double amount);
    abstract public void withdraw(double amount);
    abstract public void deposit(double amount, boolean updateTransaction);
    abstract public void withdraw(double amount, boolean updateTransaction);

    public void transfer(int accountNumber, double amount) {
        if(this.accountNumber == accountNumber) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }
        Account account = this;
        Account recipientAccount = Database.getInstance().getAccount(accountNumber);

        Account firstLock, secondLock;

        if(this.accountNumber < accountNumber) {
            firstLock = this;
            secondLock = recipientAccount;
        } else {
            firstLock = recipientAccount;
            secondLock = this;
        }

        Thread transactionThread = new Thread(() -> {
            synchronized (firstLock) {
                synchronized (secondLock) {
                    account.withdraw(amount, false);
                    recipientAccount.deposit(amount, false);
                    Database database = Database.getInstance();
                    database.updateAccount(firstLock);
                    database.updateAccount(secondLock);
                    account.addBankTransaction("Sent to " + recipientAccount.getAccountNumber(), amount);
                    recipientAccount.addBankTransaction("Received from " + account.getAccountNumber(), amount);
                }
            }
        });
        transactionThread.start();
    }

    public int createGiftCard(int pin, double amount) {
        withdraw(amount);
        GiftCard giftCard = new GiftCard(accountNumber, pin, amount);
        giftCards.put(giftCard.getCardNumber(), giftCard);
        return giftCard.getCardNumber();
    }

    public void topUpGiftCard(int cardNumber, double amount) {
        if(!giftCards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Gift Card with id: " + cardNumber + ", Not Found");
        }
        withdraw(amount);
        GiftCard giftCard = giftCards.get(cardNumber);
        giftCard.topUp(amount);
        Database.getInstance().updateGiftCard(giftCard);
    }

    public void closeGiftCard(int cardNumber, int pin) {
        if(!giftCards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Gift Card with id: " + cardNumber + ", Not Found");
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(!giftCard.checkPin(pin)) {
            throw new IllegalArgumentException("Pin is Wrong");
        }
        double cardBalance = giftCard.getCardBalance();
        giftCard.setCardBalance(0);
        giftCard.setActive(false);
        deposit(cardBalance);
        Database.getInstance().updateGiftCard(giftCard);
    }

    public void viewAllGiftCards() {
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s%n", "Card Id", "Customer Id", "Pin", "Balance", "Status", "Is Blocked", "Reward Points", "Card Type");
        for(int cardNumber: giftCards.keySet()) {
            GiftCard giftCard = giftCards.get(cardNumber);
            System.out.println(giftCard);
        }
        System.out.println();
    }

    public void purchaseItem(int cardNumber, int pin, double amount) {
        if(!giftCards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Gift Card with id: " + cardNumber + ", Not Found");
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(!giftCard.checkPin(pin)) {
            throw new IllegalArgumentException("Pin is Wrong");
        }

        giftCard.purchase(amount);

        Database.getInstance().updateGiftCard(giftCard);

        CardTransaction transaction = new CardTransaction(cardNumber, amount);
        cardTransactions.put(transaction.getTransactionId(), transaction);
    }

    public void blockCard(int cardNumber) {
        if(!giftCards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Gift Card with id: " + cardNumber + ", Not Found");
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(giftCard.isBlocked()) {
            throw new IllegalStateException("Card is already blocked");
        }
        giftCard.setBlocked(true);
        Database.getInstance().updateGiftCard(giftCard);
    }

    public void unBlockCard(int cardNumber) {
        if(!giftCards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Gift Card with id: " + cardNumber + ", Not Found");
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(!giftCard.isBlocked()) {
            throw new IllegalStateException("Card is not blocked");
        }
        giftCard.setBlocked(false);
        Database.getInstance().updateGiftCard(giftCard);
    }

    public void viewAllCardTransactions() {
        System.out.printf("%-15s %-15s %-15s%n", "Transaction Id", "Card Number", "Amount");
        for(int transactionId: cardTransactions.keySet()) {
            CardTransaction transaction = cardTransactions.get(transactionId);
            System.out.println(transaction);
        }
    }
}
