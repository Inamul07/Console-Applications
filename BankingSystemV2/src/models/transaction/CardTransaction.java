package models.transaction;


import database.Database;

public class CardTransaction extends Transaction {

    private final int cardNumber;

    public CardTransaction(int customerId, double amount) {
        super(amount);
        this.cardNumber = customerId;
        Database.getInstance().addTransaction(this);
    }

    public int getCardNumber() {
        return cardNumber;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-15s", getTransactionId(), cardNumber, getAmount());
    }
}
