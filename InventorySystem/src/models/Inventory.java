package models;

import database.Database;
import products.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Inventory implements Serializable {

    private static final long serialVersionUID = 2L;

    private final int inventoryId;
    private final Map<String, Section<?>> sections;
    private LocalDateTime lastModified;

    public Inventory() {
        sections = new HashMap<>();
        Database database = Database.getInstance();
        inventoryId = database.getNewInventoryId();
        database.addInventory(this);
        lastModified = LocalDateTime.now();
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void addSection(Section<?> section) {
        sections.put(section.getSectionName(), section);
        lastModified = LocalDateTime.now();
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

    public void viewListOfContents() {
        for(String sectionName: sections.keySet()) {
            Section<?> section = sections.get(sectionName);
            section.viewItems();
        }
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

}
