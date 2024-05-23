package models.account;

import databse.Database;

public class CurrentAccount extends Account {

    private final double minBalance;

    public CurrentAccount(int bankId, double balance) {
        super(bankId, balance, AccountType.CURRENT);
        this.minBalance = 0;
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
        synchronized (this) {
            setBalance(getBalance() + amount);
            if(updateTransaction) {
                addTransaction("Deposit", amount);
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
                throw new IllegalArgumentException("Minimum Balance should be " + minBalance);
            }
            setBalance(newBalance);
            if(updateTransaction) {
                addTransaction("Withdraw", amount);
            }
            Database.getInstance().updateAccount(this);
        }
    }
}
