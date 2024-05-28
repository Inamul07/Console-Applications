package models;

import database.Database;

import java.io.Serializable;

public class GiftCard implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long cardNumber;
    private final long customerId;
    private final int pin;
    private double cardBalance;
    private boolean isActive;
    private boolean isBlocked;
    private int rewardPoints;
    private GiftCardType cardType;

    public GiftCard(long customerId, int pin, double cardBalance) {
        Database database = Database.getInstance();
        this.cardNumber = database.getNextGiftCardId();
        this.customerId = customerId;
        this.pin = pin;
        this.cardBalance = cardBalance;
        this.isActive = true;
        this.isBlocked = false;
        this.rewardPoints = 0;
        cardType = GiftCardType.SILVER;
        database.addGiftCard(this);
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public long getCustomerId() {
        return customerId;
    }

    public double getCardBalance() {
        return cardBalance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setCardBalance(double cardBalance) {
        if(!isActive) {
            throw new IllegalStateException("Gift Card is Closed");
        }
        this.cardBalance = cardBalance;
    }

    public void setActive(boolean active) {
        if(!isActive) {
            throw new IllegalStateException("Gift Card is Closed");
        }
        isActive = active;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public boolean checkPin(int pin) {
        return this.pin == pin;
    }

    public GiftCardType getCardType() {
        return cardType;
    }

    public void setCardType(GiftCardType cardType) {
        this.cardType = cardType;
    }

    public void updateCardType() {
        if(cardType == GiftCardType.SILVER) {
            setCardType(GiftCardType.GOLD);
        } else if (cardType == GiftCardType.GOLD) {
            setCardType(GiftCardType.PLATINUM);
        }
    }

    public void topUp(double amount) {
        if(!isActive) {
            throw new IllegalStateException("Gift Card is Closed");
        }
        if(isBlocked()) {
            throw new IllegalStateException("Card is blocked");
        }
        cardBalance += amount;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s", cardNumber,
                customerId,
                pin,
                cardBalance,
                isActive? "Active": "Closed",
                isBlocked? "Yes": "No",
                rewardPoints,
                cardType.toString());
    }
}
