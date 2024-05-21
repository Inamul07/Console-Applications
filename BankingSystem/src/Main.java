import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    static Database database;
    static Account currentAccount;
    static Scanner sc;

    private static boolean loginPrompt() {
        System.out.print("Enter Account Number: ");
        int accountNumber = Integer.parseInt(sc.nextLine());
        Account account;
        try {
            account = database.getAccount(accountNumber);
            if(account == null) {
                throw new NullPointerException("Account is Null");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        System.out.print("Enter User Pin: ");
        int userPin = Integer.parseInt(sc.nextLine());
        if(userPin != account.getUserPin()) {
            System.out.println("Invalid Credentials");
            return false;
        }
        currentAccount = account;
        return true;
    }

    private static boolean signupPrompt() {
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter User PIN: ");
        int userPin = Integer.parseInt(sc.nextLine());

        Account newAccount = new Account(name, userPin);
        if(!database.createAccount(newAccount)) {
            return false;
        }
        currentAccount = newAccount;
        return true;
    }

    private static void showOptions() {
        while(true) {
            System.out.println("1. Check User Details \n2. Transaction \n3. Check Balance \n4. Deposit \n5. Withdraw");
            int choice = Integer.parseInt(sc.nextLine());
            if(choice == 1) {
                System.out.println(currentAccount);
            } else if(choice == 2) {
                boolean success = beginTransaction();
                if(!success) {
                    System.out.println("Transaction Failed");
                    return;
                }
            } else if(choice == 3) {
                System.out.println(currentAccount.getBalance());
            } else if(choice == 4) {
                System.out.print("Enter Amount: ");
                double amount = Double.parseDouble(sc.nextLine());
                if(currentAccount.deposit(amount)) {
                    database.updateAccount(currentAccount);
                    System.out.println("Deposit Successful");
                }
            } else if(choice == 5) {
                System.out.print("Enter Amount: ");
                double amount = Double.parseDouble(sc.nextLine());
                if(currentAccount.withdraw(amount)) {
                    database.updateAccount(currentAccount);
                    System.out.println("Withdraw Successful");
                }
            } else break;
        }
    }

    private static boolean beginTransaction() {
        System.out.print("Enter Recipient Account Number: ");
        int recipientAccNum = Integer.parseInt(sc.nextLine());
        System.out.print("Enter Amount: ");
        int amount = Integer.parseInt(sc.nextLine());

        try {
            Transaction transactionThread = new Transaction(currentAccount.getAccountNumber(), recipientAccNum, amount, database);
            transactionThread.start();
            transactionThread.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static void executeApplication(Database database) {
        sc = new Scanner(System.in);

        System.out.println("1. Login \n2. Signup \n3. Exit");
        int choice = Integer.parseInt(sc.nextLine());
        boolean authorized = false;
        if (choice == 1) {
            authorized = loginPrompt();
        } else if (choice == 2) {
            authorized = signupPrompt();
        } else if(choice == 3) {
            return;
        }

        if (!authorized || currentAccount == null) {
            System.out.println("Not Authorized");
            return;
        }
        showOptions();
    }

    private static void executeThreadExample(Database database) throws SQLException, ClassNotFoundException {

        Transaction transactionThread2 = new Transaction(398397534, 459158514, 250, database);
        Transaction transactionThread1 = new Transaction(459158514, 398397534, 250, database);

        transactionThread2.start();
        transactionThread1.start();

        try {
            transactionThread1.join();
            transactionThread2.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        database = Database.getDB("postgres");

         try {
             executeApplication(database);
             // executeThreadExample(database);
         } catch (Exception e) {
             System.out.println(e.getMessage());
         }

        database.printAllAccounts();
        database.closeConnection();

    }
}