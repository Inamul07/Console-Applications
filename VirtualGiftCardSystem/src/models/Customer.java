package models;

import database.Database;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Customer {

    private final long customerId;
    private double balance;
    private final Map<Long, GiftCard> giftCards;
    private final Map<Long, Transaction> transactions;

    public Customer(double balance) {
        Database database = Database.getInstance();
        this.customerId = database.getNextCustomerId();
        this.balance = balance;
        giftCards = new HashMap<>();
        transactions = new HashMap<>();
        database.addCustomer(this);
    }

    public long getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long createGiftCard(int pin, double amount) {
        if(balance - amount < 0) {
            throw new IllegalArgumentException("Balance will become negative");
        }
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be Negative");
        }
        GiftCard giftCard = new GiftCard(customerId, pin, amount);
        giftCards.put(giftCard.getCardNumber(), giftCard);
        setBalance(balance - amount);
        return giftCard.getCardNumber();
    }

    public void topUpGiftCard(long cardNumber, double amount) {
        if(!giftCards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Gift Card with id: " + cardNumber + ", Not Found");
        }
        if(balance - amount < 0) {
            throw new IllegalArgumentException("Balance will become negative");
        }
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be Negative");
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        giftCard.topUp(amount);
        setBalance(balance - amount);
    }

    public void closeGiftCard(long cardNumber, int pin) {
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
        setBalance(balance + cardBalance);
    }

    public void viewAllGiftCards() {
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s%n", "Card Id", "Customer Id", "Pin", "Balance", "Status", "Is Blocked", "Reward Points", "Card Type");
        for(long cardNumber: giftCards.keySet()) {
            GiftCard giftCard = giftCards.get(cardNumber);
            System.out.println(giftCard);
        }
        System.out.println();
    }

    public void purchaseItem(long cardNumber, int pin, double amount) {
        if(!giftCards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Gift Card with id: " + cardNumber + ", Not Found");
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(!giftCard.checkPin(pin)) {
            throw new IllegalArgumentException("Pin is Wrong");
        }
        if(giftCard.getCardBalance() - amount < 0) {
            throw new IllegalArgumentException("Balance will become negative");
        }
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be Negative");
        }
        if(giftCard.isBlocked()) {
            throw new IllegalStateException("Card is blocked");
        }
        giftCard.setCardBalance(giftCard.getCardBalance() - amount);
        if(amount >= 500) {
            giftCard.setRewardPoints(giftCard.getRewardPoints() + 50);
            if(giftCard.getRewardPoints() == 200) {
                giftCard.updateCardType();
                giftCard.setRewardPoints(0);
            }
        }
        Transaction transaction = new Transaction(cardNumber, amount);
        transactions.put(transaction.getTransactionId(), transaction);
    }

    public void viewAllTransactions() {
        System.out.printf("%-15s %-15s %-15s%n", "Transaction Id", "Card Number", "Amount");
        for(long transactionId: transactions.keySet()) {
            Transaction transaction = transactions.get(transactionId);
            System.out.println(transaction);
        }
    }

    public void blockCard(long cardNumber) {
        if(!giftCards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Gift Card with id: " + cardNumber + ", Not Found");
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(giftCard.isBlocked()) {
            throw new IllegalStateException("Card is already blocked");
        }
        giftCard.setBlocked(true);
    }

    public void unBlockCard(long cardNumber) {
        if(!giftCards.containsKey(cardNumber)) {
            throw new NoSuchElementException("Gift Card with id: " + cardNumber + ", Not Found");
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(!giftCard.isBlocked()) {
            throw new IllegalStateException("Card is not blocked");
        }
        giftCard.setBlocked(false);
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s", customerId, balance);
    }
}