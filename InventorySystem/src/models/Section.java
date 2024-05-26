package models;

import java.util.ArrayList;
import java.util.List;

public class Section<T> {
    private String sectionName;
    private List<T> products;

    public Section(String sectionName) {
        this.sectionName = sectionName;
        products = new ArrayList<>();
    }

    public String getSectionName() {
        return sectionName;
    }

    public void addItem(T item) {
        products.add(item);
    }

    public void viewItems() {
        for(T product: products) {
            System.out.println(product);
        }
    }
}
