package models.bank;

import database.Database;
import models.account.*;

import java.io.Serializable;
import java.util.*;

public abstract class Bank implements Serializable {

    private static final long serialVersionUID = 7L;

    private final int bankId;
    private final String bankName;
    private final Map<Integer, Account> accountMap = new HashMap<>();

    public Bank(int bankId, String bankName) {
        this.bankId = bankId;
        this.bankName = bankName;
        Database.getInstance().insertBank(this);
    }

    public int getBankId() {
        return bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public Account openAccount(AccountType accountType, double initialAmount) {
        Account account;
        if(accountType == AccountType.SAVINGS) {
            account = new SavingsAccount(bankId, initialAmount);
        } else if(accountType == AccountType.CURRENT) {
            account = new CurrentAccount(bankId, initialAmount);
        } else {
            account = new LoanAccount(bankId, initialAmount);
        }
        accountMap.put(account.getAccountNumber(), account);
        Database.getInstance().createAccount(account);
        return account;
    }

    public Account getAccount(int accountNumber) {
        if(!accountMap.containsKey(accountNumber)) {
            throw new NoSuchElementException("Account not found");
        }
        return accountMap.get(accountNumber);
    }

    public void viewAllAccounts() {
        System.out.printf("%-15s %-15s %-15s\n", "Account Number", "Balance", "Account Type");
        for(Account account: accountMap.values()) {
            System.out.println(account);
        }
        System.out.println();
    }

    public void viewAccountsWithMaxBalance() {
        double maxBalance = Double.MIN_VALUE;
        for(int accountNumber: accountMap.keySet()) {
            maxBalance = Math.max(maxBalance, accountMap.get(accountNumber).getBalance());
        }

        System.out.printf("%-15s %-15s %-15s\n", "Account Number", "Balance", "Account Type");
        List<Account> richAccounts = new ArrayList<>();
        for(int accountNumber: accountMap.keySet()) {
            if(accountMap.get(accountNumber).getBalance() == maxBalance) {
                System.out.println(accountMap.get(accountNumber));
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%-15s %-15s", bankId + "", bankName);
    }
}
