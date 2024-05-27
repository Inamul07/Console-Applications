package models;

import database.Database;
import products.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Inventory implements Serializable {

    private static final long serialVersionUID = 2L;

    private final int inventoryId;
    private final Map<String, Section<?>> sections;
    private String lastModified;
    transient private final DateTimeFormatter formatter;

    public Inventory() {
        sections = new HashMap<>();
        Database database = Database.getInstance();
        inventoryId = database.getNewInventoryId();
        database.addInventory(this);
        formatter = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss a");
        lastModified = LocalDateTime.now().format(formatter);
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void addSection(Section<?> section) {
        sections.put(section.getSectionName(), section);
        lastModified = LocalDateTime.now().format(formatter);
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

    public String getLastModified() {
        return lastModified;
    }

}
