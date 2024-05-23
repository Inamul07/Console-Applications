package models.account;

import databse.Database;
import models.transaction.Transaction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Account implements Serializable {

    private static final long serialVersionUID = 7L;

    private final int accountNumber;
    private double balance;
    private final int bankId;
    private final Map<Integer, Transaction> transactions;
    private final String accountType;

    public Account(int bankId, double balance, AccountType accountType) {
        if(balance < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.accountNumber = (int) (Math.random() * 100);
        this.balance = balance;
        transactions = new HashMap<>();
        this.bankId = bankId;
        this.accountType = accountType.toString();
        addTransaction("Initial Deposit", balance);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public int getBankId() {
        return bankId;
    }

    protected void addTransaction(String description, double amount) {
        Transaction transaction = new Transaction(transactions.size() + 1, description, amount, getBalance());
        transactions.put(transaction.getTransactionId(), transaction);
    }

    public void viewTransactions() {
        System.out.printf("%-15s %-15s %-15s %-15s\n", "Transaction", "Description", "Amount", "Balance");
        for(int transactionId: transactions.keySet()) {
            System.out.println(transactions.get(transactionId));
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s %-15s", accountNumber + "", balance + "", accountType);
    }

    abstract public void deposit(double amount);
    abstract public void withdraw(double amount);
    abstract public void deposit(double amount, boolean updateTransaction);
    abstract public void withdraw(double amount, boolean updateTransaction);

    public void transfer(int accountNumber, double amount) {
        if(this.accountNumber == accountNumber) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }
        Account account = this;
        Account recipientAccount = Database.getInstance().getAccount(accountNumber);

        Account firstLock, secondLock;

        if(this.accountNumber < accountNumber) {
            firstLock = this;
            secondLock = recipientAccount;
        } else {
            firstLock = recipientAccount;
            secondLock = this;
        }

        Thread transactionThread = new Thread(() -> {
            synchronized (firstLock) {
                synchronized (secondLock) {
                    account.withdraw(amount, false);
                    recipientAccount.deposit(amount, false);
                    Database database = Database.getInstance();
                    database.updateAccount(firstLock);
                    database.updateAccount(secondLock);
                    account.addTransaction("Sent to " + recipientAccount.getAccountNumber(), amount);
                    recipientAccount.addTransaction("Received from " + account.getAccountNumber(), amount);
                }
            }
        });
        transactionThread.start();
    }
}
