package products;

import java.io.Serializable;

public class Book implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String bookName;
    private final String authorName;
    private final int publishedYear;

    public Book(String bookName, String authorName, int publishedYear) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.publishedYear = publishedYear;
    }

    @Override
    public String toString() {
        return "{Book: " + bookName + ", " + authorName + ", " + publishedYear + "}";
    }

}
