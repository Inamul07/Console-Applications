package models;

import database.Database;

public class Transaction {

    private final long transactionId;
    private final long cardNumber;
    private final double amount;

    public Transaction(long customerId, double amount) {
        Database database = Database.getInstance();
        this.transactionId = database.getNextTransactionId();
        this.cardNumber = customerId;
        this.amount = amount;
        database.addTransaction(this);
    }

    public long getTransactionId() {
        return transactionId;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-15s", transactionId, cardNumber, amount);
    }
}
