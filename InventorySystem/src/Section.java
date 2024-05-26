import java.util.List;

public class Section<T> {
    private String sectionName;
    private List<T> products;

    public Section(String sectionName) {
        this.sectionName = sectionName;
    }

    public void addItem(T item) {
        products.add(item);
    }
}
