package models.transaction;

import database.Database;

import java.io.Serializable;

public abstract class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int transactionId;
    private final double amount;

    public Transaction(double amount) {
        this.transactionId = Database.getInstance().getNextTransactionId();
        this.amount = amount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

}
