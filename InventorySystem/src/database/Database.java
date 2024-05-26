package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database database;
    private static Connection connection;

    private Database() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres");
            System.out.println("Connection Successful");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Database getInstance() {
        if(database == null) {
            database = new Database();
        }
        return database;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
