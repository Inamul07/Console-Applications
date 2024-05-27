package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Section<T> implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String sectionName;
    private final List<T> products;

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
