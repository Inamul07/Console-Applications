import java.sql.SQLException;

public class Transaction extends Thread {

    private final Database database;
    private final int transactionId;
    private final int fromAccount;
    private final int toAccount;
    private final double amount;

    Transaction(int fromAccountNumber, int toAccountNumber, double amount, Database database) throws SQLException, ClassNotFoundException {
        this.transactionId = (int) (Math.random() * 100000);
        this.database = database;
        this.fromAccount = fromAccountNumber;
        this.toAccount = toAccountNumber;
        this.amount = amount;
    }

    public void run() {
        try {
            Account from = database.getAccount(fromAccount);
            Account to = database.getAccount(toAccount);

            Account account1, account2;

            if(from.getAccountNumber() < to.getAccountNumber()) {
                account1 = from;
                account2 = to;
            } else {
                account1 = to;
                account2 = from;
            }

            synchronized(account1) {
                boolean withdrawSuccess = from.withdraw(amount);
                if(withdrawSuccess) {
                    System.out.println(Thread.currentThread().getName() + " is handling the transaction.");
                    Thread.sleep(1000);
                    synchronized(account2) {
                        boolean depositSuccess = to.deposit(amount);
                        if(depositSuccess) {
                            System.out.println("Transaction Successful");
                            database.updateAccount(to);
                            database.updateAccount(from);
                        } else {
                            from.deposit(amount);
                        }
                    }
                } else {
                    System.out.println("Transaction Failed.");
                }
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
