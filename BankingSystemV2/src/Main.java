import database.Database;
import models.account.*;
import models.bank.Headquarters;

public class Main {

    private static void createHQAndAccounts() {
        Headquarters headquarters = new Headquarters(1, "Bank-1");

        SavingsAccount savingsAccount = (SavingsAccount) headquarters.openAccount(AccountType.SAVINGS, 6000);
        CurrentAccount currentAccount = (CurrentAccount) headquarters.openAccount(AccountType.CURRENT, 6000);
        LoanAccount loanAccount = (LoanAccount) headquarters.openAccount(AccountType.LOAN, 6000);

        System.out.println(savingsAccount);
        System.out.println(currentAccount);
        System.out.println(loanAccount);
    }

    private static void executeThreadExample(Database database) {
        SavingsAccount savingsAccount = (SavingsAccount) database.getAccount(6);
        CurrentAccount currentAccount = (CurrentAccount) database.getAccount(12);

        savingsAccount.transfer(12, 100);
        currentAccount.transfer(6, 100);

        savingsAccount.viewBankTransactions();
        currentAccount.viewBankTransactions();

        System.out.println(savingsAccount);
        System.out.println(currentAccount);
    }

    public static void main(String[] args) {

        Database database = Database.getInstance();

//        createHQAndAccounts();

//        executeThreadExample(database);

        database.viewAllCardTransactions();
        database.viewAllGiftCards();
        database.viewAllAccounts();

        database.closeConnection();
    }
}