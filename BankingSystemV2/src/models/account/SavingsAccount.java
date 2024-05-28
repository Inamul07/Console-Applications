package models.account;

import database.Database;

public class SavingsAccount extends Account {

    private final long minBalance;

    public SavingsAccount(int bankId, double balance) {
        super(bankId, balance, AccountType.SAVINGS);
        if(balance < 1000) {
            throw new IllegalArgumentException("Minimum Amount is 1000");
        }
        this.minBalance = 1000;
    }

    @Override
    public void deposit(double amount) {
        deposit(amount, true);
    }

    @Override
    public void withdraw(double amount) {
        withdraw(amount, true);
    }

    @Override
    public void deposit(double amount, boolean updateTransaction) {
        synchronized(this) {
            setBalance(getBalance() + amount);
            if(updateTransaction) {
                addBankTransaction("Deposit", amount);
            }
            Database.getInstance().updateAccount(this);
        }
    }

    @Override
    public void withdraw(double amount, boolean updateTransaction) {
        synchronized(this) {
            if(amount < 0) {
                throw new IllegalArgumentException("Amount cannot be negative");
            }
            double newBalance = getBalance() - amount;
            if(newBalance < minBalance) {
                throw new IllegalArgumentException();
            }
            setBalance(newBalance);
            if(updateTransaction) {
                addBankTransaction("Withdraw", amount);
            }
            Database.getInstance().updateAccount(this);
        }
    }
}
