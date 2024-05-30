package database;

import models.Customer;
import models.GiftCard;
import models.Order;
import models.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Database {

    private static Database database;
    private final Map<Long, Customer> customerMap;
    private final Map<Long, GiftCard> giftCardMap;
    private final Map<Long, Transaction> transactionMap;
    private final Map<Long, Order> orderMap;

    private static long nextOrderId = 0;

    private Database() {
        customerMap = new HashMap<>();
        giftCardMap = new HashMap<>();
        transactionMap = new HashMap<>();
        orderMap = new HashMap<>();
    }

    public static Database getInstance() {
        if(database == null) {
            database = new Database();
        }
        return database;
    }

    // ============== CUSTOMER METHODS ==============
    public long getNextCustomerId() {
        return customerMap.size() + 1;
    }

    public void addCustomer(Customer customer) {
        customerMap.put(customer.getCustomerId(), customer);
    }

    public Customer getCustomer(long customerId) {
        if(!customerMap.containsKey(customerId)) {
            System.out.println("Customer with id: " + customerId + ", Not Found");
            return null;
        }
        return customerMap.get(customerId);
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
        giftCardMap.put(giftCard.getCardNumber(), giftCard);
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
        transactionMap.put(transaction.getTransactionId(), transaction);
    }

    public void viewAllTransactions() {
        System.out.printf("%-15s %-15s %-15s %-15s%n", "Transaction Id", "Card Id", "Amount", "Transaction Type");
        for(long transactionId: transactionMap.keySet()) {
            Transaction transaction = transactionMap.get(transactionId);
            System.out.println(transaction);
        }
        System.out.println();
    }
    // ==============================================

    // =============== ORDER METHODS ================
    public long getNextOrderId() {
        return ++nextOrderId;
    }

    public void addOrder(Order order) {
        orderMap.put(order.getOrderId(), order);
    }

    public void removeOrder(long orderId) {
        if(!orderMap.containsKey(orderId)) {
            throw new NoSuchElementException("Order with id: " + orderId + ", Not Found");
        }
        orderMap.remove(orderId);
    }

    public void viewAllOrders() {
        System.out.printf("%-15s %-15s %-15s %-15s%n", "Order Id", "Customer Id", "Card Id", "Amount");
        for(long transactionId: transactionMap.keySet()) {
            Transaction transaction = transactionMap.get(transactionId);
            System.out.println(transaction);
        }
        System.out.println();
    }
    // ==============================================
}