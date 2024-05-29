package models;

import database.Database;

public class Transaction {

    private final long transactionId;
    private final long cardNumber;
    private final double amount;
    private final boolean isDebit;

    public Transaction(long cardNumber, double amount, boolean isDebit) {
        Database database = Database.getInstance();
        this.transactionId = database.getNextTransactionId();
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.isDebit = isDebit;
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

    public String getTransactionType() {
        return isDebit? "DEBIT": "CREDIT";
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-15s %-15s", transactionId, cardNumber, amount, getTransactionType());
    }
}
