package models.account;

import databse.Database;

public class LoanAccount extends Account {

    public LoanAccount(int bankId, double balance) {
        super(bankId, balance, AccountType.LOAN);
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
                addTransaction("Deposit", amount);
            }
            Database.getInstance().updateAccount(this);
        }
    }

    @Override
    public void withdraw(double amount, boolean updateTransaction) {
        synchronized(this) {
            if (amount < 0) {
                throw new IllegalArgumentException("Amount cannot be negative");
            }
            setBalance(getBalance() - amount);
            if(updateTransaction) {
                addTransaction("Withdraw", amount);
            }
            Database.getInstance().updateAccount(this);
        }
    }
}
