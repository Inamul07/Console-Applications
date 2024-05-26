package products;

public class Book {

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
