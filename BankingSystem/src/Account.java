import java.io.Serializable;

public class Account implements Serializable {

    private static final long serialVersionUID = 7L;

    private int accountNumber;
    private String userName;
    private int userPin;
    private double balance;

    Account(String userName, int userPin) {
        this.accountNumber = (int) (Math.random() * 1000000000);
        this.userName = userName;
        this.userPin = userPin;
        this.balance = 0.0;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserPin() {
        return userPin;
    }

    public void setUserPin(int userPin) {
        this.userPin = userPin;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", userName='" + userName + '\'' +
                ", balance=" + balance +
                '}';
    }

    public synchronized boolean deposit(double amount) {
        balance += amount;
        return true;
    }

    public synchronized boolean withdraw(double amount) {
        if(balance < amount) {
            System.out.println("Insufficient Balance");
            return false;
        }
        balance -= amount;
        return true;
    }
}
