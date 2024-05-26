package models;

import products.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Inventory {

    private final Map<String, Section<?>> sections;

    public Inventory() {
        sections = new HashMap<>();
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
        for(String section: sections.keySet()) {
            System.out.print(section + ", ");
        }
    }

}
