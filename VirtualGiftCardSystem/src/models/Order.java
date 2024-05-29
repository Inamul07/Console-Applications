package models;

import database.Database;

public class Order {
    private final long orderId;
    private final long customerId;
    private final long cardId;
    private final double amount;

    public Order(long customerId, long cardId, double amount) {
        this.orderId = Database.getInstance().getNextOrderId();
        this.customerId = customerId;
        this.cardId = cardId;
        this.amount = amount;
        Database.getInstance().addOrder(this);
    }

    public long getOrderId() {
        return orderId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public long getCardNumber() {
        return cardId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-15s %-15s", orderId, customerId, cardId, amount);
    }
}
