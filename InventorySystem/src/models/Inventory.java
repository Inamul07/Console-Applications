package models;

import database.Database;
import products.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Inventory implements Serializable {

    private final int inventoryId;
    private final Map<String, Section<?>> sections;

    public Inventory() {
        sections = new HashMap<>();
        Database database = Database.getInstance();
        inventoryId = database.getNewInventoryId();
        database.addInventory(this);
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void addSection(Section<?> section) {
        sections.put(section.getSectionName(), section);
    }

    public Section<?> getSection(String sectionName) {
        if(!sections.containsKey(sectionName)) {
            throw new NoSuchElementException("Section " + sectionName + ", Not Found");
        }
        return sections.get(sectionName);
    }

    public void viewListOfSections() {
        System.out.print("[ ");
        for(String section: sections.keySet()) {
            System.out.print(section + ", ");
        }
        System.out.println("] ");
    }

}
