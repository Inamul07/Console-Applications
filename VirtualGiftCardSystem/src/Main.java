import database.Database;
import models.Customer;

public class Main {

    private static void executeTaskTestCases(Database database) {
        // Task 1
        Customer customer1 = new Customer(10000);
        Customer customer2 = new Customer(20000);
        Customer customer3 = new Customer(30000);

        database.viewAllCustomers();

        // Task 2
        long cardNum1 = customer1.createGiftCard(1111, 2500);
        long cardNum2 = customer1.createGiftCard(2222, 2500);
        long cardNum3 = customer2.createGiftCard(3333, 5000);
        long cardNum4 = customer2.createGiftCard(1234, 0);

        database.viewAllGiftCards();
        database.viewAllCustomers();

        // Task 3
        customer1.topUpGiftCard(cardNum1, 1000);
        customer2.topUpGiftCard(cardNum4, 5000);

        database.viewAllGiftCards();
        database.viewAllCustomers();

        // Task 4
        customer2.closeGiftCard(cardNum4, 1234);

        database.viewAllGiftCards();
        database.viewAllCustomers();

        // Task 5
        customer1.purchaseItem(cardNum2, 2222, 1000);
        customer1.purchaseItem(cardNum2, 2222, 1000);
        customer2.purchaseItem(cardNum3, 3333, 2000);

        database.viewAllGiftCards();
        database.viewAllCustomers();
        database.viewAllTransactions();

        // Task 6
        customer2.blockCard(cardNum4);

        customer2.purchaseItem(cardNum3, 3333, 500);
        customer2.purchaseItem(cardNum3, 3333, 500);
        customer2.purchaseItem(cardNum3, 3333, 500);

        database.viewAllGiftCards();
        database.viewAllCustomers();
    }

    private static void executeSampleTestCases(Database database) {
        // TestCase
        Customer customer1 = new Customer(7000);
        Customer customer2 = new Customer(8000);
        Customer customer3 = new Customer(9000);

        long cardNum1 = customer1.createGiftCard(1111, 6000);

        long cardNum2 = customer2.createGiftCard(2222, 3000);
        long cardNum3 = customer2.createGiftCard(2222, 3000);

        long cardNum4 = customer3.createGiftCard(3333, 2000);
        long cardNum5 = customer3.createGiftCard(3333, 2000);
        long cardNum6 = customer3.createGiftCard(3333, 2000);

        customer1.purchaseItem(cardNum1, 1111, 1000);
        customer2.purchaseItem(cardNum2, 2222, 1000);
        customer2.purchaseItem(cardNum3, 2222, 1000);
        customer3.purchaseItem(cardNum4, 3333, 1000);
        customer3.purchaseItem(cardNum5, 3333, 1000);
        customer3.purchaseItem(cardNum6, 3333, 1000);

        customer1.blockCard(cardNum1);
        customer2.blockCard(cardNum2);
        customer2.blockCard(cardNum3);
        customer3.blockCard(cardNum4);
        customer3.blockCard(cardNum5);
        customer3.blockCard(cardNum6);

        customer1.unBlockCard(cardNum1);
        customer2.unBlockCard(cardNum2);
        customer2.unBlockCard(cardNum3);
        customer3.unBlockCard(cardNum4);
        customer3.unBlockCard(cardNum5);
        customer3.unBlockCard(cardNum6);

        customer1.purchaseItem(cardNum1, 1111, 1000);
        customer2.purchaseItem(cardNum2, 2222, 1000);
        customer2.purchaseItem(cardNum3, 2222, 1000);
        customer3.purchaseItem(cardNum4, 3333, 1000);
        customer3.purchaseItem(cardNum5, 3333, 1000);
        customer3.purchaseItem(cardNum6, 3333, 1000);

        customer1.topUpGiftCard(cardNum1, 1000);
        customer2.topUpGiftCard(cardNum2, 1000);
        customer2.topUpGiftCard(cardNum3, 1000);
        customer3.topUpGiftCard(cardNum4, 1000);
        customer3.topUpGiftCard(cardNum5, 1000);
        customer3.topUpGiftCard(cardNum6, 1000);

        customer1.closeGiftCard(cardNum1, 1111);
        customer2.closeGiftCard(cardNum2, 2222);
        customer2.closeGiftCard(cardNum3, 2222);
        customer3.closeGiftCard(cardNum4, 3333);
        customer3.closeGiftCard(cardNum5, 3333);
        customer3.closeGiftCard(cardNum6, 3333);

        long cardNum7 = customer1.createGiftCard(1111, 5000);

        database.viewAllCustomers();
        database.viewAllGiftCards();
        database.viewAllTransactions();
    }

    public static void main(String[] args) {

        Database database = Database.getInstance();

         executeTaskTestCases(database);
        // executeSampleTestCases(database);
    }
}