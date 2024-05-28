package models.transaction;


import database.Database;

public class BankTransaction extends Transaction {

    private final String description;
    private final double balanceLeft;

    public BankTransaction(String description, double amount, double balanceLeft) {
        super(amount);
        this.description = description;
        this.balanceLeft = balanceLeft;
    }

    public String getDescription() {
        return description;
    }

    public double getBalanceLeft() {
        return balanceLeft;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-15s %-15s", getTransactionId(), description, getAmount(), balanceLeft);
    }
}
