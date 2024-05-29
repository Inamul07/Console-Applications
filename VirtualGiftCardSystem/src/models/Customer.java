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
            System.out.println("Balance will become negative");
            return 0;
        }
        if(amount < 0) {
            System.out.println("Amount cannot be Negative");
            return 0;
        }
        GiftCard giftCard = new GiftCard(customerId, pin, amount);
        giftCards.put(giftCard.getCardNumber(), giftCard);
        setBalance(balance - amount);
        return giftCard.getCardNumber();
    }

    public void topUpGiftCard(long cardNumber, double amount) {
        if(!giftCards.containsKey(cardNumber)) {
            System.out.println("Gift Card with id: " + cardNumber + ", Not Found");
            return;
        }
        if(balance - amount < 0) {
            System.out.println("Balance will become negative");
            return;
        }
        if(amount < 0) {
            System.out.println("Amount cannot be Negative");
            return;
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        giftCard.topUp(amount);
        setBalance(balance - amount);
    }

    public void closeGiftCard(long cardNumber, int pin) {
        if(!giftCards.containsKey(cardNumber)) {
            System.out.println("Gift Card with id: " + cardNumber + ", Not Found");
            return;
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(!giftCard.checkPin(pin)) {
            System.out.println("Pin is Wrong");
            return;
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
            System.out.println("Gift Card with id: " + cardNumber + ", Not Found");
            return;
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(!giftCard.checkPin(pin)) {
            System.out.println("Pin is Wrong");
            return;
        }
        if(giftCard.getCardBalance() - amount < 0) {
            System.out.println("Balance will become negative");
            return;
        }
        if(amount < 0) {
            System.out.println("Amount cannot be Negative");
            return;
        }
        if(giftCard.isBlocked()) {
            System.out.println("Card is blocked");
            return;
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
            System.out.println("Gift Card with id: " + cardNumber + ", Not Found");
            return;
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(giftCard.isBlocked()) {
            System.out.println("Card is already blocked");
            return;
        }
        giftCard.setBlocked(true);
    }

    public void unBlockCard(long cardNumber) {
        if(!giftCards.containsKey(cardNumber)) {
            System.out.println("Gift Card with id: " + cardNumber + ", Not Found");
            return;
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(!giftCard.isBlocked()) {
            System.out.println("Card is not blocked");
            return;
        }
        giftCard.setBlocked(false);
    }

    public void depositAmount(double amount) {
        setBalance(getBalance() + amount);
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s", customerId, balance);
    }
}