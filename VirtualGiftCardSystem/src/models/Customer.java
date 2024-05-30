package models;

import database.Database;

import java.util.HashMap;
import java.util.Map;

public class Customer {

    private final long customerId;
    private double balance;
    private final Map<Long, GiftCard> giftCards;
    private final Map<Long, Transaction> transactions;
    private final Map<Long, Order> ordersMap;

    public Customer(double balance) {
        Database database = Database.getInstance();
        this.customerId = database.getNextCustomerId();
        this.balance = balance;
        giftCards = new HashMap<>();
        transactions = new HashMap<>();
        ordersMap = new HashMap<>();
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

    private void addTransaction(long cardNumber, double amount, boolean isDebit) {
        Transaction transaction = new Transaction(cardNumber, amount, isDebit);
        transactions.put(transaction.getTransactionId(), transaction);
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

        addTransaction(giftCard.getCardNumber(), amount, false);
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
        try {
            giftCard.topUp(amount);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return;
        }
        setBalance(balance - amount);
        addTransaction(giftCard.getCardNumber(), amount, false);
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
        try {
            giftCard.setCardBalance(0);
            giftCard.setActive(false);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return;
        }
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

    public long purchaseItem(long cardNumber, int pin, double amount) {
        if(!giftCards.containsKey(cardNumber)) {
            System.out.println("Gift Card with id: " + cardNumber + ", Not Found");
            return 0;
        }
        GiftCard giftCard = giftCards.get(cardNumber);
        if(!giftCard.checkPin(pin)) {
            System.out.println("Pin is Wrong");
            return 0;
        }
        if(giftCard.getCardBalance() - amount < 0) {
            System.out.println("Balance will become negative");
            return 0;
        }
        if(amount < 0) {
            System.out.println("Amount cannot be Negative");
            return 0;
        }

        try {
            giftCard.setCardBalance(giftCard.getCardBalance() - amount);
            if(amount >= 500) {
                giftCard.setRewardPoints(giftCard.getRewardPoints() + 50);
                if(giftCard.getRewardPoints() == 200) {
                    giftCard.updateCardType();
                    giftCard.setRewardPoints(0);
                }
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return 0;
        }
        addTransaction(cardNumber, amount, true);

        Order order = new Order(customerId, cardNumber, amount);
        ordersMap.put(order.getOrderId(), order);
        return order.getOrderId();
    }

    public void returnItem(long orderId) {
        if(!ordersMap.containsKey(orderId)) {
            System.out.println("Order with id: " + orderId + ", Not Found");
            return;
        }
        Order prevOrder = ordersMap.get(orderId);
        double orderAmount = prevOrder.getAmount();

        GiftCard giftCard = giftCards.get(prevOrder.getCardNumber());

        try {
            giftCard.topUp(orderAmount);
        } catch (IllegalStateException e ) {
            System.out.println(e.getMessage());
            return;
        }

        if(orderAmount >= 500) {
            if(giftCard.getRewardPoints() == 0) {
                giftCard.setRewardPoints(200);
                giftCard.rollBackCardType();
            }
            giftCard.setRewardPoints(giftCard.getRewardPoints() - 50);
        }

        ordersMap.remove(orderId);
        Database.getInstance().removeOrder(orderId);
        addTransaction(giftCard.getCardNumber(), orderAmount, false);
        System.out.println("Return Successful");

    }

    public void viewAllTransactions() {
        System.out.printf("%-15s %-15s %-15s %-15s%n", "Transaction Id", "Card Number", "Amount", "Transaction Type");
        for(long transactionId: transactions.keySet()) {
            Transaction transaction = transactions.get(transactionId);
            System.out.println(transaction);
        }
        System.out.println();
    }

    public void viewAllOrders() {
        System.out.printf("%-15s %-15s %-15s %-15s%n", "Order Id", "Customer Id", "Card Id", "Amount");
        for(long orderId: ordersMap.keySet()) {
            Order order = ordersMap.get(orderId);
            System.out.println(order);
        }
        System.out.println();
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