package database;

import models.Customer;
import models.GiftCard;
import models.Transaction;
import utils.Serialization;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static Database database;
    private final Connection connection;
    private final Map<Long, Customer> customerMap;
    private final Map<Long, GiftCard> giftCardMap;
    private final Map<Long, Transaction> transactionMap;

    private Database() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/gift_card_system");
            System.out.println("Connection Successful");
        } catch (Exception e) {
            throw new RuntimeException("Error Connecting to the database");
        }
        customerMap = new HashMap<>();
        giftCardMap = new HashMap<>();
        transactionMap = new HashMap<>();
        loadDataToMap();
    }

    public static Database getInstance() {
       try {
           if(database == null) {
               database = new Database();
           }
           return database;
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    public void closeConnection() {
        try {
            loadDataToDB();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ============== CUSTOMER METHODS ==============
    public long getNextCustomerId() {
        return customerMap.size() + 1;
    }

    public void addCustomer(Customer customer) {
        try {
            String serializedCustomer = Serialization.serialize(customer);
            executeUpdate("INSERT INTO customers ( customer_id, customer ) VALUES ( " + customer.getCustomerId() + ", '" + serializedCustomer + "' );");
            customerMap.put(customer.getCustomerId(), customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void viewAllCustomers() {
        System.out.printf("%-15s %-15s%n", "Customer Id", "Balance");
        for(long customerId: customerMap.keySet()) {
            Customer customer = customerMap.get(customerId);
            System.out.println(customer);
        }
        System.out.println();
    }
    // ==============================================

    // ============ GIFT CARD METHODS ===============
    public long getNextGiftCardId() {
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

    public void viewAllGiftCards() {
        System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s%n", "Card Id", "Customer Id", "Pin", "Balance", "Status", "Is Blocked", "Reward Points", "Card Type");
        for(long cardNumber: giftCardMap.keySet()) {
            GiftCard giftCard = giftCardMap.get(cardNumber);
            System.out.println(giftCard);
        }
        System.out.println();
    }
    // ==============================================

    // ============ TRANSACTION METHODS =============
    public long getNextTransactionId() {
        return transactionMap.size() + 1;
    }

    public void addTransaction(Transaction transaction) {
        try {
            String serializedTransaction = Serialization.serialize(transaction);
            executeUpdate("INSERT INTO transactions ( transaction_id, transaction ) VALUES ( " + transaction.getTransactionId() + ", '" + serializedTransaction + "' );");
            transactionMap.put(transaction.getTransactionId(), transaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void viewAllTransactions() {
        System.out.printf("%-15s %-15s %-15s%n", "Transaction Id", "Card Id", "Amount");
        for(long transactionId: transactionMap.keySet()) {
            Transaction transaction = transactionMap.get(transactionId);
            System.out.println(transaction);
        }
        System.out.println();
    }
    // ==============================================

    // ================= LOAD & STORE ===============
    private void loadDataToMap() throws SQLException {
        ResultSet customerSet = executeQuery("SELECT * FROM customers;");
        if(customerSet == null) {
            throw new RuntimeException("Cannot retrieve data from DB");
        }
        while(customerSet.next()) {
            String serializedCustomer = customerSet.getString("customer");
            Customer customer = (Customer) Serialization.deserialize(serializedCustomer);
            if(customer == null) {
                throw new NullPointerException("Customer is null");
            }
            customerMap.put(customer.getCustomerId(), customer);
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
            Transaction transaction = (Transaction) Serialization.deserialize(serializedTransaction);
            if(transaction == null) {
                throw new NullPointerException("Customer is null");
            }
            transactionMap.put(transaction.getTransactionId(), transaction);
        }
    }

    private void loadDataToDB() throws IOException, SQLException {
        for(long customerId : customerMap.keySet()) {
            Customer customer = customerMap.get(customerId);

            String serializedCustomer = Serialization.serialize(customer);
            executeUpdate("UPDATE customers SET customer = '" + serializedCustomer + "' WHERE customer_id = " + customerId + ";");
        }

        for(long cardNumber : giftCardMap.keySet()) {
            GiftCard giftCard = giftCardMap.get(cardNumber);

            String serializedGiftCard = Serialization.serialize(giftCard);
            executeUpdate("UPDATE gift_cards SET gift_card = '" + serializedGiftCard + "' WHERE card_id = " + cardNumber + ";");
        }

        for(long transactionId : transactionMap.keySet()) {
            Transaction transaction = transactionMap.get(transactionId);

            String serializedCustomer = Serialization.serialize(transaction);
            executeUpdate("UPDATE transactions SET transaction = '" + serializedCustomer + "' WHERE transaction_id = " + transactionId + ";");
        }
    }
    // ==============================================

    // ============== SQL HELPERS ===================
    private void executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    private ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    // ==============================================
}