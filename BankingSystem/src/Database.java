import java.io.*;
import java.sql.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private final Connection connection;

    private final Map<Integer, Account> accountMap;

    private Database(String dbName) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName);
        System.out.println("Connection Successful");

        accountMap = new HashMap<>();
        loadDataToMap();
    }

    public static Database getDB(String dbName) throws SQLException, ClassNotFoundException {
        return new Database(dbName);
    }

    // ===================== SERIALIZATION & DESERIALIZATION ============================
    private String serialize(Object object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);

        byte[] byteArray = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(byteArray);
    }

    private Object deserialize(String serializedString) {
        try {
            byte[] byteArray = Base64.getDecoder().decode(serializedString);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            return objectInputStream.readObject();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // ================== SQL EXECUTION ==========================
    private void executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // =================== ACCOUNT OPERATIONS ======================
    public boolean createAccount(Account account) {
        try {
            String serializedAccount = serialize(account);
            executeUpdate("INSERT INTO accounts ( account_number, account ) VALUES ( " + account.getAccountNumber() + ", '" + serializedAccount + "' );");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        accountMap.put(account.getAccountNumber(), account);
        return true;
    }

    public Account getAccount(int accountNumber) {
        if(!accountMap.containsKey(accountNumber)) {
            System.out.println("Account Not Found");
            return null;
        }
        return accountMap.get(accountNumber);
    }

    public boolean updateAccount(Account account) {

        if(account == null) {
            System.out.println("Account is null");
            return false;
        }

        accountMap.put(account.getAccountNumber(), account);
        return true;
    }

    public void printAllAccounts() {
        for(Account account: accountMap.values()) {
            System.out.println(account);
        }
    }

    // =============== DATA LOAD & STORE ========================
    private void loadDataToMap() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM accounts;");
            while(resultSet.next()) {
                String serializedAccount = resultSet.getString("account");
                Account account = (Account) deserialize(serializedAccount);
                if(account == null) {
                    throw new NullPointerException("Account is null");
                }
                accountMap.put(account.getAccountNumber(), account);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadDataToDB() throws IOException {
        for(int accountNumber: accountMap.keySet()) {
            Account account = accountMap.get(accountNumber);

            String serializedAccount = serialize(account);
            executeUpdate("UPDATE accounts SET account = '" + serializedAccount + "' WHERE account_number = " + accountNumber + ";");
        }
    }

    public void closeConnection() {
        try {
            loadDataToDB();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
