package databse;

import models.account.Account;
import models.bank.Bank;
import models.bank.Branch;
import models.bank.Headquarters;
import utils.Serialization;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static Database database;

    private final Connection connection;
    private final Map<Integer, Account> accountMap;
    private final Map<Integer, Bank> bankMap;

    private Database() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres");
            System.out.println("Connection Successful");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        this.accountMap = new HashMap<>();
        this.bankMap = new HashMap<>();
        loadDataToMap();
    }

    public static Database getInstance() {
        try {
            if(database == null) {
                database = new Database();
            }
            return database;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            loadDataToDB();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // ============================= CRUD OPERATIONS ====================================
    // ACCOUNT OPERATIONS
    public void createAccount(Account account) {
        try {
            String serializedAccount = Serialization.serialize(account);
            executeUpdate("INSERT INTO accounts ( account_number, account ) VALUES ( " + account.getAccountNumber() + ", '" + serializedAccount + "' );");
        } catch (SQLException e) {
            throw new RuntimeException("Cannot insert values into table. " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Serialization Fault. (Bank Data) \n" + e.getMessage());
        }
        accountMap.put(account.getAccountNumber(), account);
    }

    public Account getAccount(int accountNumber) {
        if(!accountMap.containsKey(accountNumber)) {
            throw new NullPointerException("Account Not found");
        }
        return accountMap.get(accountNumber);
    }

    public void updateAccount(Account account) {
        if(account == null) {
            throw new NullPointerException("Account is null.");
        }
        accountMap.put(account.getAccountNumber(), account);
    }

    public void viewAllAccounts() {
        System.out.printf("%-15s %-15s %-15s\n", "Account Number", "Balance", "Account Type");
        for(Account account: accountMap.values()) {
            System.out.println(account);
        }
    }

    // BANK OPERATIONS
    public void insertBank(Bank bank) {
        try {
            String serializedBank = Serialization.serialize(bank);
            executeUpdate("INSERT INTO banks ( bank_id, bank ) VALUES ( " + bank.getBankId() + ", '" + serializedBank + "' );");
            bankMap.put(bank.getBankId(), bank);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot insert values into table. " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Serialization Fault. (Bank Data) \n" + e.getMessage());
        }
    }

    public Bank getBank(int bankId) {
        if(!bankMap.containsKey(bankId)) {
            throw new NullPointerException("Bank not found");
        }
        return bankMap.get(bankId);
    }
    // ==================================================================================

    // ============================= DATA LOAD & STORE ==================================
    private void loadDataToDB() throws IOException, SQLException {
        for(int accountNumber: accountMap.keySet()) {
            Account account = accountMap.get(accountNumber);

            String serializedAccount = Serialization.serialize(account);
            executeUpdate("UPDATE accounts SET account = '" + serializedAccount + "' WHERE account_number = " + accountNumber + ";");
        }

        for(int bankId: bankMap.keySet()) {
            Bank bank = bankMap.get(bankId);

            String serializedBank;
            if(bank instanceof Headquarters) {
                Headquarters bankHQ = (Headquarters) bank;
                serializedBank = Serialization.serialize(bankHQ);
            } else {
                Branch branch = (Branch) bank;
                serializedBank = Serialization.serialize(branch);
            }
            executeUpdate("UPDATE banks SET bank = '" + serializedBank + "' WHERE bank_id = " + bankId + ";");
        }
    }

    private void loadDataToMap() throws SQLException {
        ResultSet bankSet = executeQuery("SELECT * FROM banks;");
        if(bankSet == null) {
            throw new NullPointerException(("Failed to fetch data"));
        }
        while(bankSet.next()) {
            String serializedBank = bankSet.getString("bank");
            Bank bank = (Bank) Serialization.deserialize(serializedBank);
            if(bank == null) {
                throw new NullPointerException("Bank is null");
            }
            bankMap.put(bank.getBankId(), bank);
        }

        ResultSet accountSet = executeQuery("SELECT * FROM accounts;");
        if(accountSet == null) {
            throw new NullPointerException("Failed to fetch data");
        }
        while(accountSet.next()) {
            String serializedAccount = accountSet.getString("account");
            Account account = (Account) Serialization.deserialize(serializedAccount);
            if(account == null) {
                throw new NullPointerException("Account is null");
            }
            accountMap.put(account.getAccountNumber(), account);
        }
    }
    // ================================================================================

    // ============================ SQL Helpers =======================================
    private void executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    private ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
    // ===============================================================================
}
