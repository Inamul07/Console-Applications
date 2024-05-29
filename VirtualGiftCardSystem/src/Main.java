import database.Database;
import models.Customer;

import java.util.Scanner;

public class Main {

    static Scanner sc;
    static Database database;

    public static void createGiftCardPrompt(Customer customer) {
        System.out.println();

        System.out.print("Create a 4 digit pin: ");
        int pin = sc.nextInt();
        if(pin < 1000 || pin > 9999) {
            System.out.println("PIN should only have 4 digits");
        }

        System.out.print("Enter Amount to transfer into card: ");
        double amount = sc.nextDouble();

        long cardNumber = customer.createGiftCard(pin, amount);
        if(cardNumber == 0) return;
        System.out.println("Card Created.");
        System.out.println("Your Card Number is " + cardNumber);
    }

    public static void purchaseItemPrompt(Customer customer) {
        System.out.println();

        System.out.print("Enter Gift Card Number: ");
        long cardId = sc.nextLong();

        System.out.print("Enter pin: ");
        int pin = sc.nextInt();

        System.out.print("Enter Total Amount of Items: ");
        double amount = sc.nextDouble();

        long transactionId = customer.purchaseItem(cardId, pin, amount);
        if(transactionId == 0) return;

        System.out.println("Purchase Successful; Order id: " + transactionId);
    }

    public static void returnItemPrompt(Customer customer) {
        System.out.println();

        System.out.print("Enter Order Id: ");
        long orderId = sc.nextLong();

        customer.returnItem(orderId);
    }

    public static void topUpGiftCardPrompt(Customer customer) {
        System.out.println();

        System.out.print("Enter Gift Card Number: ");
        long cardId = sc.nextLong();

        System.out.print("Enter Amount to Top Up: ");
        double amount = sc.nextDouble();

        customer.topUpGiftCard(cardId, amount);
    }

    public static void closeGiftCardPrompt(Customer customer) {
        System.out.println();

        System.out.print("Enter Gift Card Number: ");
        long cardId = sc.nextLong();

        System.out.print("Enter pin: ");
        int pin = sc.nextInt();

        customer.closeGiftCard(cardId, pin);
    }

    public static void blockGiftCardPrompt(Customer customer) {
        System.out.println();

        System.out.print("Enter Gift Card Number: ");
        long cardId = sc.nextLong();

        customer.blockCard(cardId);
    }

    public static void unblockGiftCardPrompt(Customer customer) {
        System.out.println();

        System.out.print("Enter Gift Card Number: ");
        long cardId = sc.nextLong();

        customer.unBlockCard(cardId);
    }

    public static void depositPrompt(Customer customer) {
        System.out.println();

        System.out.print("Enter Amount to Deposit: ");
        double amount = sc.nextDouble();

        customer.depositAmount(amount);
        System.out.println("Deposit Successful");
    }

    public static void createAccountPrompt() {
        System.out.println();
        System.out.print("Enter Amount to Deposit: ");
        double amount = sc.nextDouble();
        Customer customer = new Customer(amount);

        System.out.println("Account created.");
        System.out.println("Your Id is " + customer.getCustomerId());
    }

    public static void loginPrompt() {
        System.out.print("Enter customer id: ");
        long customerId = sc.nextLong();

        Customer customer = database.getCustomer(customerId);
        if(customer == null) return;

        while(true) {
            System.out.print("1. Create Gift Card\n2. Purchase Item\n3. Return Item\n4. Top up Gift Card\n5. Close Gift Card\n6. Block Gift Card\n7. Unblock Gift Card\n8. View All Gift Cards\n9. View All Transactions\n10. View All Orders\n11. Deposit\n12. Go Back\nEnter ur choice: ");
            int choice = sc.nextInt();
            if(choice <= 0 || choice > 11) break;
            if(choice == 1) {
                createGiftCardPrompt(customer);
                System.out.println();
            } else if(choice == 2) {
                purchaseItemPrompt(customer);
                System.out.println();
            } else if(choice == 3) {
                returnItemPrompt(customer);
            } else if(choice == 4) {
                topUpGiftCardPrompt(customer);
                System.out.println();
            } else if(choice == 5) {
                closeGiftCardPrompt(customer);
                System.out.println();
            } else if(choice == 6) {
                blockGiftCardPrompt(customer);
                System.out.println();
            } else if(choice == 7) {
                unblockGiftCardPrompt(customer);
                System.out.println();
            } else if(choice == 8) {
                customer.viewAllGiftCards();
            } else if(choice == 9) {
                customer.viewAllTransactions();
            } else if(choice == 10) {
                customer.viewAllOrders();
            } else {
                depositPrompt(customer);
                System.out.println();
            }
        }
    }

    private static void executePrompt() {
        while(true) {
            System.out.print("1. Create Account\n2. Log in\n3. View Account Summary\n4. View Gift card Summary\n5. View Transaction History\n6. Exit\nEnter ur choice: ");
            int choice = sc.nextInt();
            if(choice <= 0 || choice > 5) break;
            if(choice == 1) {
                createAccountPrompt();
                System.out.println();
            } else if(choice == 2) {
                loginPrompt();
                System.out.println();
            } else if(choice == 3) {
                database.viewAllCustomers();
            } else if(choice == 4) {
                database.viewAllGiftCards();
            } else {
                database.viewAllTransactions();
            }
        }
    }

    public static void main(String[] args) {

        database = Database.getInstance();
        sc = new Scanner(System.in);

        executePrompt();

    }
}