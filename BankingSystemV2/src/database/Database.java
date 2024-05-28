package database;

import models.account.Account;
import models.account.GiftCard;
import models.bank.Bank;
import models.bank.Branch;
import models.bank.Headquarters;
import models.transaction.CardTransaction;
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
    private final Map<Integer, GiftCard> giftCardMap;
    private final Map<Integer, CardTransaction> cardTransactionMap;

    private Database() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/banking_system_v2");
            System.out.println("Connection Successful");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        accountMap = new HashMap<>();
        bankMap = new HashMap<>();
        giftCardMap = new HashMap<>();
        cardTransactionMap = new HashMap<>();
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

    public int getNextAccountNumber() {
        return accountMap.size() + 1;
    }

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
        System.out.println();
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

    // ============ GIFT CARD METHODS ===============
    public int getNextGiftCardId() {
        return giftCardMap.size() + 1;
    }

    public void addGiftCard(GiftCard giftCard) {
        try {
            String serializedGiftCard = Serialization.serialize(giftCard);
            executeUpdate("INSERT INTO gift_cards ( card_id, gift_card ) VALUES ( " + giftCard.getCardNumber() + ", '" + serializedGiftCard + "' );");
            giftCardMap.put(giftCard.getCardNumber(), giftCard);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGiftCard(GiftCard giftCard) {
        giftCardMap.put(giftCard.getCardNumber(), giftCard);
    }

    public void viewAllGiftCards() {
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s%n", "Card Id", "Customer Id", "Pin", "Balance", "Status", "Is Blocked", "Reward Points", "Card Type");
        for(int cardNumber: giftCardMap.keySet()) {
            GiftCard giftCard = giftCardMap.get(cardNumber);
            System.out.println(giftCard);
        }
        System.out.println();
    }
    // ==============================================

    // ============ TRANSACTION METHODS =============
    public int getNextTransactionId() {
        return cardTransactionMap.size() + 1;
    }

    public void addTransaction(CardTransaction transaction) {
        try {
            String serializedTransaction = Serialization.serialize(transaction);
            executeUpdate("INSERT INTO transactions ( transaction_id, transaction ) VALUES ( " + transaction.getTransactionId() + ", '" + serializedTransaction + "' );");
            cardTransactionMap.put(transaction.getTransactionId(), transaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void viewAllCardTransactions() {
        System.out.printf("%-15s %-15s %-15s%n", "Transaction Id", "Card Id", "Amount");
        for(int transactionId: cardTransactionMap.keySet()) {
            CardTransaction transaction = cardTransactionMap.get(transactionId);
            System.out.println(transaction);
        }
        System.out.println();
    }
    // ==============================================

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

        for(int cardNumber : giftCardMap.keySet()) {
            GiftCard giftCard = giftCardMap.get(cardNumber);

            String serializedGiftCard = Serialization.serialize(giftCard);
            executeUpdate("UPDATE gift_cards SET gift_card = '" + serializedGiftCard + "' WHERE card_id = " + cardNumber + ";");
        }

        for(int transactionId : cardTransactionMap.keySet()) {
            CardTransaction transaction = cardTransactionMap.get(transactionId);

            String serializedCustomer = Serialization.serialize(transaction);
            executeUpdate("UPDATE transactions SET transaction = '" + serializedCustomer + "' WHERE transaction_id = " + transactionId + ";");
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

        ResultSet giftCardSet = executeQuery("SELECT * FROM gift_cards;");
        if(giftCardSet == null) {
            throw new RuntimeException("Cannot retrieve data from DB");
        }
        while(giftCardSet.next()) {
            String serializedGiftCard = giftCardSet.getString("gift_card");
            GiftCard giftCard = (GiftCard) Serialization.deserialize(serializedGiftCard);
            if(giftCard == null) {
                throw new NullPointerException("Customer is null");
            }
            giftCardMap.put(giftCard.getCardNumber(), giftCard);
        }

        ResultSet transactionSet = executeQuery("SELECT * FROM transactions;");
        if(transactionSet == null) {
            throw new RuntimeException("Cannot retrieve data from DB");
        }
        while(transactionSet.next()) {
            String serializedTransaction = transactionSet.getString("transaction");
            CardTransaction transaction = (CardTransaction) Serialization.deserialize(serializedTransaction);
            if(transaction == null) {
                throw new NullPointerException("Customer is null");
            }
            cardTransactionMap.put(transaction.getTransactionId(), transaction);
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
