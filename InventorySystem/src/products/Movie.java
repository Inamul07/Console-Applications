package products;

import java.io.Serializable;

public class Movie implements Serializable {
    private final String movieName;
    private final String directorName;
    private final int releaseYear;
    private final String genre;

    public Movie(String movieName, String directorName, int releaseYear, String genre) {
        this.movieName = movieName;
        this.directorName = directorName;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "{Movie: " + movieName + ", " + directorName + ", " + releaseYear +  ", " + genre + "}";
    }
}
