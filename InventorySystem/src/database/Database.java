package database;

import models.Inventory;
import utils.Serialization;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Database {
    private static Database database;
    private static Connection connection;

    private final Map<Integer, Inventory> inventoryMap;

    private Database() throws SQLException, IOException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres");
            System.out.println("Connection Successful");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        inventoryMap = new HashMap<>();
        loadDataToMap();
    }

    public static Database getInstance() {
        try {
            if(database == null) {
                database = new Database();
            }
            return database;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            loadDataToDB();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addInventory(Inventory inventory) {
        inventoryMap.put(inventory.getInventoryId(), inventory);
        try {
            String serializedInventory = Serialization.serialize(inventory);
            executeUpdate("INSERT INTO inventories ( inventory_id, inventory ) VALUES ( " + inventory.getInventoryId() + ", '" + serializedInventory + "' );");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Inventory getInventory(int inventoryId) {
        if(!inventoryMap.containsKey(inventoryId)) {
            throw new NoSuchElementException("Inventory Not Found");
        }
        return inventoryMap.get(inventoryId);
    }

    public int getNewInventoryId() {
        return inventoryMap.size() + 1;
    }

    private void loadDataToDB() throws IOException, SQLException {
        for(int inventoryId: inventoryMap.keySet()) {
            Inventory inventory = inventoryMap.get(inventoryId);

            String serializedInventory = Serialization.serialize(inventory);
            executeUpdate("UPDATE inventories SET inventory = '" + serializedInventory + "' WHERE inventory_id = " + inventoryId + ";");
        }
    }

    private void loadDataToMap() throws SQLException, IOException {
        ResultSet inventorySet = executeQuery("SELECT * FROM inventories;");
        if(inventorySet == null) {
            throw new NullPointerException(("Failed to fetch data"));
        }
        while(inventorySet.next()) {
            int inventoryId = inventorySet.getInt("inventory_id");
            String serializedInventory = inventorySet.getString("inventory");
            Inventory inventory = (Inventory) Serialization.deserialize(serializedInventory);
            if(inventory == null) {
                throw new NullPointerException("Inventory is null");
            }
            inventoryMap.put(inventoryId, inventory);
        }
    }

    private void executeUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    private ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }
}
