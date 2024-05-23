package models.transaction;

import java.io.Serializable;

public class Transaction implements Serializable {
    private final int transactionId;
    private final String description;
    private final double amount;
    private final double balanceLeft;

    public Transaction(int transactionId, String description, double amount, double balanceLeft) {
        this.transactionId = transactionId;
        this.description = description;
        this.amount = amount;
        this.balanceLeft = balanceLeft;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceLeft() {
        return balanceLeft;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-15s %-15s",transactionId + "", description, amount + "", balanceLeft + "");
    }
}
