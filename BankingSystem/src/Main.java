import databse.Database;
import models.account.*;
import models.bank.Bank;
import models.bank.Headquarters;

public class Main {

    private static void createHQAndAccounts() {
        Headquarters headquarters = new Headquarters(1, "Bank-1");

        SavingsAccount savingsAccount = (SavingsAccount) headquarters.openAccount(AccountType.SAVINGS, 1500);
        CurrentAccount currentAccount = (CurrentAccount) headquarters.openAccount(AccountType.CURRENT, 500);

        System.out.println(savingsAccount);
        System.out.println(currentAccount);
    }

    private static void executeThreadExample(Database database) {
        SavingsAccount savingsAccount = (SavingsAccount) database.getAccount(6);
        CurrentAccount currentAccount = (CurrentAccount) database.getAccount(12);

        savingsAccount.transfer(12, 100);
        currentAccount.transfer(6, 100);

        savingsAccount.viewTransactions();
        currentAccount.viewTransactions();

        System.out.println(savingsAccount);
        System.out.println(currentAccount);
    }

    public static void main(String[] args) {

        Database database = Database.getInstance();

//        createHQAndAccounts();

//        executeThreadExample(database);

        Bank bank = database.getBank(1);
        bank.viewAllAccounts();

        Account account = bank.getAccount(6);

        account.viewTransactions();

        database.closeConnection();
    }
}